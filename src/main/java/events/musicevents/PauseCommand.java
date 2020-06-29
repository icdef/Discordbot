package events.musicevents;
import main.Commandsyntax;
import music.GuildMusicManager;
import music.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;

public class PauseCommand extends Commandsyntax {
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String[] input = event.getMessage().getContentRaw().split(" ");
        if (!checkcommand(input[0], "pause")) return;
        TextChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if(IsBotInChannel(event,audioManager))return;

        PlayerManager playerManager = PlayerManager.getInstance(event.getChannel());
        GuildMusicManager guildMusicManager = playerManager.getMusicManager(event.getGuild());
        if (guildMusicManager.player.getPlayingTrack() == null)
            return;
        if (guildMusicManager.player.isPaused()) {
            channel.sendMessage("Unpaused").queue();
            guildMusicManager.player.setPaused(false);
        }
        else {
            guildMusicManager.player.setPaused(true);
            channel.sendMessage("Paused").queue();
        }
    }
}
