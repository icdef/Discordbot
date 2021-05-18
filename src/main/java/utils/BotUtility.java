package utils;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class BotUtility {


    private BotUtility(){

    }

    public static String defaultPlaylist = "https://www.youtube.com/playlist?list=PLuA0sYpDnkxH5Iy1_flQnq6mHWV_PbOfC";
    /**
     * checks if event got triggered by bot or if bot is in a voice channel
     * @param event
     * @param audioManager
     * @return boolean
     */
    public static boolean IsBotInVoiceChannel(GuildMessageReceivedEvent event, AudioManager audioManager){

        //checks if event got triggered by bot
        if (event.getAuthor().isBot())
            return true;
        //checks if bot is in a voice channel
        if (!audioManager.isConnected()) {
            event.getChannel().sendMessage("I'm not connected to a channel").queue();
            return true;
        }
        return false;
    }

    public static boolean isAutoShufflePlaylist(String s){
        return s.equals(defaultPlaylist);
    }
}
