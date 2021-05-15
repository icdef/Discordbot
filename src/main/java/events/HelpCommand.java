package events;

import main.CommandSyntax;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;

/**
 * !help
 * Gets a list of all implemented Commands and their Information
 */

public class HelpCommand extends CommandSyntax {
    private ArrayList<AllCommands> ac;
    public HelpCommand (ArrayList<AllCommands> ac){
        this.ac = ac;
    }
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        StringBuilder out = new StringBuilder();
        Collections.sort(ac);
        String[] input = event.getMessage().getContentRaw().split(" ");
        if (checkCommand(input[0],"help")&&!event.getMember().getUser().isBot()){

                for (AllCommands ac: ac) {
                   out.append(ac.toString());
                   out.append("\n");
                }
                event.getChannel().sendMessage(out).queue();

        }
    }
}
