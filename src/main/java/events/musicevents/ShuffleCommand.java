package events.musicevents;

import main.CommandSyntax;
import music.GuildMusicManager;
import music.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import utils.BotUtility;

import javax.annotation.Nonnull;

public class ShuffleCommand extends CommandSyntax {
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String[] input = event.getMessage().getContentRaw().split(" ");
        if (!checkCommand(input[0], "shuffle")) return;
        TextChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();
        if(BotUtility.IsBotInVoiceChannel(event,audioManager))return;

        PlayerManager playerManager = PlayerManager.getInstance(channel);
        GuildMusicManager guildMusicManager = playerManager.getMusicManager(event.getGuild());
        guildMusicManager.scheduler.shuffleQueue();

    }
}
