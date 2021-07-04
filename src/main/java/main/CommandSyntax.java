package main;

import net.dv8tion.jda.api.hooks.ListenerAdapter;


public abstract class CommandSyntax extends ListenerAdapter {
    //command prefix
    public final char PREFIX = '!';

    public boolean checkCommand(String input, String command){
        return input.compareTo(PREFIX+command) == 0;
    }

}
