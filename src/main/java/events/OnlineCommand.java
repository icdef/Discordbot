package events;

import main.Commandsyntax;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;


import java.util.List;

/**
 * !online
 * Displays all members whoes Status is Online
 */
public class OnlineCommand extends Commandsyntax {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){

        List<Member> members = event.getGuild().getMembers();
        int nrmembers = event.getGuild().getMembers().size()-1; //wegen bot
        int onlinemembers = 0;
        String[] input = event.getMessage().getContentRaw().split(" ");
        if (checkcommand(input[0],"online")&&!event.getMember().getUser().isBot()){
            StringBuilder memberstring = new StringBuilder();
            for (Member m : members){
                if ((!m.getUser().isBot()) && m.getOnlineStatus() == OnlineStatus.ONLINE) {
                    onlinemembers++;
                    if (m.getNickname() != null) {
                        memberstring.append(m.getNickname());
                    }
                    else
                        memberstring.append(m.getUser().getName());
                    memberstring.append("\n");
                }
            }
            String membernumber = "There are "+nrmembers+" Users on the Server and "+onlinemembers+" are online:\n";
            StringBuilder output = new StringBuilder();
            output.append(membernumber);
            output.append(memberstring);
            event.getChannel().sendMessage(output).queue();


        }
    }
}
