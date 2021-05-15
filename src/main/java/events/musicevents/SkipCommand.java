package events.musicevents;

import main.CommandSyntax;
import music.GuildMusicManager;
import music.PlayerManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;

public class SkipCommand extends CommandSyntax {
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String[] input = event.getMessage().getContentRaw().split(" ");
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (!checkCommand(input[0], "skip")) return;
        if(IsBotInVoiceChannel(event,audioManager))return;

        PlayerManager playerManager = PlayerManager.getInstance(event.getChannel());
        GuildMusicManager guildMusicManager = playerManager.getMusicManager(event.getGuild());
        guildMusicManager.scheduler.nextTrack(true);

    }
}
