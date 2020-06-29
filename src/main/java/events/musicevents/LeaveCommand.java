package events.musicevents;

import main.Commandsyntax;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;

public class LeaveCommand extends Commandsyntax {
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String[] input = event.getMessage().getContentRaw().split(" ");
        if (!checkcommand(input[0],"leave")) return;
        TextChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if(IsBotInChannel(event,audioManager))return;
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        if (!voiceChannel.getMembers().contains(event.getMember())){
            channel.sendMessage("You have to be in the Voicehannel to kick the Bot").queue();
            return;
        }
        audioManager.closeAudioConnection();
        channel.sendMessage("Leaving Channel").queue();
    }
}