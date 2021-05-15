package main;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;


public abstract class CommandSyntax extends ListenerAdapter {
    //command prefix
    public final char PREFIX = '!';

    public boolean checkCommand(String input, String command){
        return input.compareTo(PREFIX+command) == 0;
    }

    /**
     * checks if event got triggered by bot or if bot is in a voice channel
     * @param event
     * @param audioManager
     * @return boolean
     */
    public boolean IsBotInVoiceChannel(GuildMessageReceivedEvent event, AudioManager audioManager){

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



}
