package main;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;


public abstract class Commandsyntax extends ListenerAdapter {
    //command prefix
    public char PREFIX = '!';

    public boolean checkcommand(String input,String command){
        return input.compareTo(PREFIX+command) == 0;
    }

    public boolean IsBotInChannel(GuildMessageReceivedEvent event, AudioManager audioManager){

        //checks if event got triggerd by bot
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
