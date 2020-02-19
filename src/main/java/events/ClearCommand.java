package events;

import main.Commandsyntax;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;


import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * !clear number
 * command to delete a number of messages (between 1 and 99) Messages at one call
 */
public class ClearCommand extends Commandsyntax {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        //checks if event got triggered by Bot
        if (event.getAuthor().isBot()) return;

        String[] input = event.getMessage().getContentRaw().split(" ");

        if (checkcommand(input[0],"clear")){
            //with no arg u just delete one message above
            if (input.length < 2) {
                deleteMessages(event.getChannel(), 2); //2 because amount hast to be between 2 and 100
                event.getGuild().getTextChannelById("676052219141029938").sendMessage(event.getAuthor().getName() +
                        " cleared in channel " + event.getChannel().getAsMention() + " 1 Message").queue();
            }
            else if (input.length == 2) {
                Pattern pattern = Pattern.compile("\\d+");
                if (!pattern.matcher(input[1]).matches()) {
                    event.getChannel().sendMessage("Only numbers between 2 and 100").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                    return;
                }
                int amount = Integer.parseInt(input[1]);
                if (amount > 100 || amount < 2){
                    event.getChannel().sendMessage("Only numbers between 2 and 100").queue();
                    return;
                }
                deleteMessages(event.getChannel(),amount+1);
                event.getGuild().getTextChannelById("676052219141029938").sendMessage(event.getAuthor().getName()+" cleared in channel "+event.getChannel().getAsMention()+" "+amount+" Messages").queue();
            }
            else{
                event.getChannel().sendMessage("Usage: !clear number").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            }

        }
    }

    private void deleteMessages (TextChannel channel, int amount){
        MessageHistory history = new MessageHistory(channel);
        List<Message> messages = history.retrievePast(amount).complete();
        channel.deleteMessages(messages).queue();

    }
}
