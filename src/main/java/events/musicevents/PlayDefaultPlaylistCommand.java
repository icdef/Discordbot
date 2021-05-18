package events.musicevents;

import main.CommandSyntax;
import music.GuildMusicManager;
import music.PlayerManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import utils.BotUtility;

import javax.annotation.Nonnull;

public class PlayDefaultPlaylistCommand extends CommandSyntax {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String[] input = event.getMessage().getContentRaw().split(" ");
        if (checkCommand(input[0], "playlist")) {
            AudioManager audioManager = event.getGuild().getAudioManager();
            VoiceChannel channel = event.getMember().getVoiceState().getChannel();
            if (!event.getMember().getVoiceState().inVoiceChannel()){
                event.getChannel().sendMessage("Please join channel first").queue();
                return;
            }
            if(!audioManager.isConnected()) {
                audioManager.openAudioConnection(channel);
            }

            PlayerManager manager = PlayerManager.getInstance(event.getChannel());
            try {
                String url = BotUtility.defaultPlaylist;
                manager.loadandPlay(event.getChannel(), url);
            }
            catch (Exception ex) {
                event.getChannel().sendMessage(ex.getMessage()).queue();
            }



        }





    }

}
