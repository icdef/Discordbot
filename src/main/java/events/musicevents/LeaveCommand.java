package events.musicevents;

import main.CommandSyntax;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import utils.BotUtility;

import javax.annotation.Nonnull;

public class LeaveCommand extends CommandSyntax {
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String[] input = event.getMessage().getContentRaw().split(" ");
        if (!checkCommand(input[0],"leave")) return;
        TextChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if(BotUtility.IsBotInVoiceChannel(event,audioManager))return;
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        if (!voiceChannel.getMembers().contains(event.getMember())){
            channel.sendMessage("You have to be in the Voice channel to kick the Bot").queue();
            return;
        }
        audioManager.closeAudioConnection();
        channel.sendMessage("Leaving Channel").queue();
    }
}
