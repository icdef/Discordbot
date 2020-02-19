package events;

import main.Commandsyntax;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * !date
 * Sends Date into event channel with !date
 */
public class DateCommand extends Commandsyntax {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (event.getAuthor().isBot()) return;
        String[] input = event.getMessage().getContentRaw().split(" ");
        if (checkcommand(input[0],"date") && !event.getMember().getUser().isBot()){
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
            String formatedDateTime = dateTime.format(format);
            event.getChannel().sendMessage("The date is "+formatedDateTime).queue();
        }
    }
}
