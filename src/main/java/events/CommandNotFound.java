package events;

import main.CommandSyntax;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
/**
 * When someone uses "!" as prefix and tries a command which is not implemented write a message into eventchannel
 * otherwise do nothing
 */

import java.util.ArrayList;

public class CommandNotFound extends CommandSyntax {
    private ArrayList<AllCommands> ac;
    public CommandNotFound (ArrayList<AllCommands> ac){
        this.ac = ac;
    }
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        boolean found = false;
        String[] input = event.getMessage().getContentRaw().split(" ");
        if (input.length == 0)
            return;
        if (input[0].charAt(0) != PREFIX)
            return;
        if  (!event.getMember().getUser().isBot()){
            for (AllCommands c: ac){
                if (c.getCommand().compareTo(input[0].substring(1)) == 0){ //look for command
                    found = true;
                }
            }
            if (!found)
                event.getChannel().sendMessage("Command not found. Press !help for help").queue();

        }
    }
}
