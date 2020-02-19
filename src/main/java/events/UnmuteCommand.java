package events;

import main.Commandsyntax;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * !unmute @target
 * redoes the !mute command
 */
public class UnmuteCommand extends Commandsyntax {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (event.getAuthor().isBot()) return;


        String[] input = event.getMessage().getContentRaw().split(" ");
        if (!event.getMember().isOwner()){ //who is allowed to use
            if (!checkcommand(input[0],"unmute"))
                return;
            event.getChannel().sendMessage("Only Owner can Unmute. Sorry star").queue();
            return;
        }


        if (checkcommand(input[0],"unmute")) {
            if (input.length < 2) { //user is missing
                event.getChannel().sendMessage("Forgot to mark User").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                return;
            }
            if (event.getMessage().getMentionedMembers().isEmpty()){
                event.getChannel().sendMessage("U need to target the user").queue();
                return;
            }
                Member target = event.getMessage().getMentionedMembers().get(0);
                Role muted = event.getGuild().getRoleById("676042399981502474");
                target.mute(false).queue();
                event.getGuild().removeRoleFromMember(target,muted).queue();
                event.getGuild().getTextChannelById("676052219141029938").sendMessage(event.getAuthor().getName()+" unmuted "+
                        event.getMessage().getMentionedMembers().get(0).getUser().getAsMention()).queue();
        }



    }
}
