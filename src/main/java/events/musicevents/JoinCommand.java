package events.musicevents;


import main.CommandSyntax;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand extends CommandSyntax {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (event.getAuthor().isBot()) return;
        String[] input = event.getMessage().getContentRaw().split(" ");
        if (!checkCommand(input[0],"join")) return;
        VoiceChannel channel = event.getMember().getVoiceState().getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (audioManager.isConnected()){
            event.getChannel().sendMessage("I'm already in a channel").queue();
            return;
        }
        if (!event.getMember().getVoiceState().inVoiceChannel()){
            event.getChannel().sendMessage("Please join channel first").queue();
            return;
        }

        audioManager.openAudioConnection(channel);
        event.getChannel().sendMessage("Joining your voice channel").queue();


    }
}
