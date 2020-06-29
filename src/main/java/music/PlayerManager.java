package music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private TextChannel channel;
    private final Map<Long,GuildMusicManager> musicManagers;


    public PlayerManager(TextChannel channel){
        this.playerManager = new DefaultAudioPlayerManager();
        this.musicManagers = new HashMap<>();
        AudioSourceManagers.registerRemoteSources(playerManager); //play remote files
        AudioSourceManagers.registerLocalSource(playerManager); //play local files
        this.channel = channel;
    }



    public synchronized GuildMusicManager getMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);
        if (musicManager == null){
            musicManager = new GuildMusicManager(playerManager, channel);
            musicManagers.put(guildId, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public void loadandPlay(TextChannel channel, String trackUrl){
        GuildMusicManager musicManager = getMusicManager(channel.getGuild());
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                play(musicManager,audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                boolean once = true;
                List<AudioTrack> audioTrackList = audioPlaylist.getTracks();
                for (AudioTrack track : audioTrackList){
                    if (once)
                        musicManager.scheduler.queuePlaylist(track,audioPlaylist,true);
                    else
                        musicManager.scheduler.queuePlaylist(track,audioPlaylist,false);
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
        return INSTANCE;
    }
}
