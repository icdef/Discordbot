package music;

import music.GuildMusicManager;
import music.PlayerManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Deque;
import java.util.Objects;

public class MusicReactionController extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event){
        PlayerManager manager = PlayerManager.getInstance(event.getTextChannel());
        Deque<Message> playMessages= manager.getPlayMessages();
        GuildMusicManager guildMusicManager = manager.getMusicManager(event.getGuild());
        if (Objects.requireNonNull(event.getUser()).isBot())
            return;
        if (playMessages.peek() == null)
            return;
        if (playMessages.peek().getIdLong() == event.getMessageIdLong()){

            switch (event.getReaction().getReactionEmote().getAsCodepoints().toUpperCase()){
                case "U+23EF":
                    guildMusicManager.player.setPaused(!guildMusicManager.player.isPaused());
                    break;
                case "U+23ED":
                    guildMusicManager.scheduler.nextTrack(true);
                    break;
                case "U+1F500":
                    guildMusicManager.scheduler.shuffleQueue();
                    break;
                case "U+23F9":
                    guildMusicManager.scheduler.stopAndReset();
                    break;

            }

        }


    }

    @Override
    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
        PlayerManager manager = PlayerManager.getInstance(event.getTextChannel());
        Deque<Message> playMessages= manager.getPlayMessages();
        GuildMusicManager guildMusicManager = manager.getMusicManager(event.getGuild());
        if (Objects.requireNonNull(event.getUser()).isBot())
            return;
        if (playMessages.peek() == null)
            return;
        if (playMessages.peek().getIdLong() == event.getMessageIdLong()){

            switch (event.getReaction().getReactionEmote().getAsCodepoints().toUpperCase()){
                case "U+23EF":
                    guildMusicManager.player.setPaused(!guildMusicManager.player.isPaused());
                    break;
                case "U+23ED":
                    guildMusicManager.scheduler.nextTrack(true);
                    break;
                case "U+1F500":
                    guildMusicManager.scheduler.shuffleQueue();
                    break;
                case "U+23F9":
                    guildMusicManager.scheduler.stopAndReset();
                    break;

            }

        }
    }
}
