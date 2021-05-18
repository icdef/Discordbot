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


}
