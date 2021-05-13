package music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;


public class PlayerManager {
    private static PlayerManager INSTANCE;
    private AudioPlayerManager audioPlayerManager;
    private TextChannel channel;
    private Map<Long,GuildMusicManager> musicManagers;
    private Deque<Message> playMessages = new ArrayDeque<>();


    private PlayerManager(TextChannel channel){
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        this.musicManagers = new HashMap<>();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager); //play remote files
        AudioSourceManagers.registerLocalSource(audioPlayerManager); //play local files
        this.channel = channel;
    }

    public Deque<Message> getPlayMessages() {
        return playMessages;
    }


    public synchronized GuildMusicManager getMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);
        if (musicManager == null){
            musicManager = new GuildMusicManager(audioPlayerManager, channel, playMessages);
            musicManagers.put(guildId, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public void loadandPlay(TextChannel channel, String trackUrl){
        GuildMusicManager musicManager = getMusicManager(channel.getGuild());
        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                play(musicManager,audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                boolean once = true;
                List<AudioTrack> audioTrackList = audioPlaylist.getTracks();
                for (AudioTrack track : audioTrackList){
                    musicManager.scheduler.queuePlaylist(track,audioPlaylist, once);
                    once = false;
                }

            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessage("Could not play "+e.getMessage()).queue();
            }
        });
    }

    private void play (GuildMusicManager musicManager, AudioTrack track){
        musicManager.scheduler.queue(track);
    }

    //so if there is one music playing and event triggers again you get the same instance and not a new one.
    public static synchronized PlayerManager getInstance(TextChannel channel){
        if (INSTANCE == null)
            INSTANCE = new PlayerManager(channel);
        INSTANCE.channel = channel;
        return INSTANCE;
    }

}
