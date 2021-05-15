package events.musicevents;


import main.CommandSyntax;
import music.PlayerManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;

public class PlayCommand extends CommandSyntax {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel channel = event.getMember().getVoiceState().getChannel();
        String[] input = event.getMessage().getContentRaw().split(" ");
        if (!checkCommand(input[0], "play")) return;
        if (input.length < 2){
            event.getChannel().sendMessage("Provide Link").queue();
            return;


        }

        String url = input[1];
        if (input.length > 2){
            event.getChannel().sendMessage("Usage: !play URL").queue();
            return;
        }

        if (!event.getMember().getVoiceState().inVoiceChannel()){
            event.getChannel().sendMessage("Please join channel first").queue();
            return;
        }


        if(!audioManager.isConnected()) {
            audioManager.openAudioConnection(channel);
            event.getChannel().sendMessage("Joining your voice channel").queue();
        }

        PlayerManager manager = PlayerManager.getInstance(event.getChannel());
        try {
            manager.loadandPlay(event.getChannel(), url);
        }
        catch (Exception ex) {
            event.getChannel().sendMessage(ex.getMessage()).queue();
        }

    }
}
