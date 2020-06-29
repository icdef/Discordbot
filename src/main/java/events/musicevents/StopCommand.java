package events.musicevents;

import main.Commandsyntax;
import music.GuildMusicManager;
import music.PlayerManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;

public class StopCommand extends Commandsyntax {
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String[] input = event.getMessage().getContentRaw().split(" ");
        if (!checkcommand(input[0], "stop")) return;

        AudioManager audioManager = event.getGuild().getAudioManager();

        if(IsBotInChannel(event,audioManager))return;
        PlayerManager playerManager = PlayerManager.getInstance(event.getChannel());
        GuildMusicManager guildMusicManager = playerManager.getMusicManager(event.getGuild());
        guildMusicManager.scheduler.getQueue().clear();
        guildMusicManager.player.stopTrack();
        guildMusicManager.player.setPaused(false);
    }
}
