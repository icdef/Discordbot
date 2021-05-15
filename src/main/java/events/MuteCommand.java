package events;

import main.CommandSyntax;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;


import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * !mute @targets [reason]
 * Author command which can Servermute and text restrict another user.
 * Also writes a log with reason when given into a separate channel
 */

public class MuteCommand extends CommandSyntax {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (event.getAuthor().isBot()) return;

        String[] input = event.getMessage().getContentRaw().split(" ");
        if (!event.getMember().isOwner()){ //who is allowed to use
            if (!checkCommand(input[0],"mute"))
                return;
            event.getChannel().sendMessage("Only Owner can mute. Sorry star").queue();
            return;
        }

        if (checkCommand(input[0],"mute")){
            if (input.length < 2){ //user is missing
                sendErrorMessage(event.getChannel(),event.getMember());
                return;
            }
            if (event.getMessage().getMentionedMembers().isEmpty()){
                event.getChannel().sendMessage("U need to target the user").queue();
                return;
            }

            List<Member> target = event.getMessage().getMentionedMembers();
           // Role muted = event.getGuild().getRoleById("676042399981502474");
             Role muted = event.getGuild().getRolesByName("muted",true).get(0);
            if (muted == null)
                return;
            for (Member m : target) {
                event.getGuild().addRoleToMember(m, muted).queue();
                if (!m.isOwner())
                    m.mute(true).queue();
            }
            String reason = "";
            if (input.length > 2) {  // user specified a reason
                for (int i = 2; i < input.length; i++) {
                    reason += input[i] + " ";
                }

            }
            if (event.getGuild().getTextChannelById("676052219141029938") != null)
                log(target.get(0), event.getMember(), reason, event.getGuild().getTextChannelById("676052219141029938"));
        }


    }


    private void sendErrorMessage(TextChannel channel, Member member){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Invalid Usage!");
        builder.setAuthor(member.getUser().getName(),member.getUser().getAvatarUrl(),member.getUser().getAvatarUrl());
        builder.setColor(Color.RED);
        builder.setDescription("{} = Required, [] = Optional");
        builder.addField("Proper usage: !mute {@user} [reason]","",false);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(5, TimeUnit.SECONDS);

    }

    public void log(Member muted, Member muter, String reason, TextChannel channel){
        EmbedBuilder builder = new EmbedBuilder();
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
        String formatedDateTime = dateTime.format(format);
        builder.setTitle("Mute Report");
        builder.setColor(Color.BLUE);
        builder.addField("Muted User", muted.getAsMention(),false);//?
        builder.addField("Muter", muter.getAsMention(),false); //?
        builder.addField("Date",formatedDateTime,false);
        builder.addField("Reason",reason,false);
        channel.sendMessage(builder.build()).queue();
    }
}
