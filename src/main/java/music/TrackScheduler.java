package music;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private  AudioPlayer player;
    private  BlockingQueue<AudioTrack> queue;
    private TextChannel channel;


    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }
    public TrackScheduler(AudioPlayer player, TextChannel channel){
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.channel = channel;

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
            channel.sendMessage("Adding to queue: " +track.getInfo().title).queue();
            queue.offer(track);
            return;
        }

        channel.sendMessage("Playing now: "+track.getInfo().title).queue();
    }

    public void queuePlaylist(AudioTrack track, AudioPlaylist playlist,boolean once) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            if (once)
                channel.sendMessage("Adding "+playlist.getTracks().size()+" Songs").queue();
            queue.offer(track);
            return;
        }

        channel.sendMessage("Adding "+playlist.getTracks().size()+" Songs\nPlaying now: "+track.getInfo().title).queue();
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void ShuffleQueue(){
        List<AudioTrack> tracks = new ArrayList<>(queue);
        Collections.shuffle(tracks);
        queue = new LinkedBlockingQueue<>(tracks);
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack(boolean skipped) {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        AudioTrack nexttrack = queue.peek();
        if (nexttrack != null) {
            if(!skipped)
                channel.sendMessage("Playing now: " + nexttrack.getInfo().title).queue();
            else
                channel.sendMessage("Skipped Song\nPlaying now: " + nexttrack.getInfo().title).queue();
        }
        player.startTrack(queue.poll(), false);
    }




    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            nextTrack(false);
        }
    }

}
