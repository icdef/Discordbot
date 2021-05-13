package music;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private  AudioPlayer player;
    private  BlockingQueue<AudioTrack> queue;
    private TextChannel channel;
    private Deque<Message> playMessages;


    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }
    public TrackScheduler(AudioPlayer player, TextChannel channel, Deque<Message> playMessages){
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.channel = channel;
        this.playMessages = playMessages;

    }

    private void removeReactionsFromLatesMusicBotMessage(Deque<Message> stack) {
        if (stack.peek() != null) {
            Message removeIt = stack.pop();
            removeIt.clearReactions().queue();
        }
    }
    private void addReactionsToMessage(Message msg) {
        msg.addReaction("U+23EF").queue();
        msg.addReaction("U+23ED").queue();
        msg.addReaction("U+1F500").queue();
        msg.addReaction("U+23F9").queue();
    }
    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            removeReactionsFromLatesMusicBotMessage(playMessages);
            Message message1 = new MessageBuilder("Adding to queue: " +track.getInfo().title).build();
            channel.sendMessage(message1).queue(msg -> {addReactionsToMessage(msg); playMessages.push(msg);});
            queue.offer(track);
            return;
        }
        Message message = new MessageBuilder().append("Playing now: ").append(track.getInfo().title).build();
        channel.sendMessage(message).queue(msg -> {addReactionsToMessage(msg); playMessages.push(msg);});


    }

    public void queuePlaylist(AudioTrack track, AudioPlaylist playlist,boolean once) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        removeReactionsFromLatesMusicBotMessage(playMessages);
        if (!player.startTrack(track, true)) {
            if (once) {
                Message message = new MessageBuilder("Adding " + playlist.getTracks().size() + " Songs").build();
                channel.sendMessage(message).queue(msg -> {addReactionsToMessage(msg);playMessages.push(msg);});
            }
            queue.offer(track);
            return;
        }
        Message message = new MessageBuilder("Adding "+playlist.getTracks().size()+" Songs\nPlaying now: "+track.getInfo().title).build();
        channel.sendMessage(message).queue(msg -> {addReactionsToMessage(msg);playMessages.push(msg);});
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void shuffleQueue(){
        removeReactionsFromLatesMusicBotMessage(playMessages);
        Message message = new MessageBuilder("Queue shuffled").build();
        channel.sendMessage(message).queue(msg -> {addReactionsToMessage(msg);playMessages.push(msg);});
        List<AudioTrack> tracks = new ArrayList<>(queue);
        Collections.shuffle(tracks);
        queue = new LinkedBlockingQueue<>(tracks);
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack(boolean isSkipping) {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        AudioTrack nextTrack = queue.peek();
        removeReactionsFromLatesMusicBotMessage(playMessages);
        if (player.isPaused())
            player.setPaused(false);

        if (nextTrack != null) {
        Message message = new MessageBuilder().append("Playing now: ").append(nextTrack.getInfo().title).build();
        Message message1 = new MessageBuilder().append("Skipped Song\nPlaying now: ").append(nextTrack.getInfo().title).build();
            if(!isSkipping) {
                channel.sendMessage(message).queue(msg -> {addReactionsToMessage(msg); playMessages.push(msg);});

            }

            else {
                channel.sendMessage(message1).queue(msg -> {addReactionsToMessage(msg); playMessages.push(msg);});

            }
        }
        player.startTrack(queue.poll(), false);
    }




    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            nextTrack(false);
        }
    }
    public void stopAndReset(){
        queue.clear();
        playMessages.clear();
        player.stopTrack();
        player.setPaused(false);
        Message message = new MessageBuilder("Music stopped and queue cleared").build();
        channel.sendMessage(message).queue();
    }

}
