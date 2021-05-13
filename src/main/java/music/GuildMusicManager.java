package music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Deque;
import java.util.Objects;
import java.util.Stack;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
    /**
     * Audio player for the guild.
     */
    public final AudioPlayer player;
    /**
     * Track scheduler for the player.
     */
    public final TrackScheduler scheduler;
    /**
     * Creates a player and a track scheduler.
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }
    public GuildMusicManager(AudioPlayerManager manager,TextChannel channel, Deque<Message> playMessages) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player,channel, playMessages);
        player.addListener(scheduler);

    }


    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuildMusicManager that = (GuildMusicManager) o;
        return Objects.equals(player, that.player) &&
                Objects.equals(scheduler, that.scheduler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, scheduler);
    }
}