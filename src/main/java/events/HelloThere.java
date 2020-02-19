package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Writing Hello name back when someone writes "hello" in the chat
 */
public class HelloThere extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){

        String[] input = event.getMessage().getContentRaw().split(" ");
        if (input[0].equalsIgnoreCase("Hello")){
            if (!event.getMember().getUser().isBot()) {
                if (event.getMember().getNickname() != null){
                    event.getChannel().sendMessage("Hello " + event.getMember().getNickname() + " !").queue();
                }
                else
                    event.getChannel().sendMessage("Hello " + event.getMember().getUser().getName() + " !").queue();
            }
        }
    }
}
