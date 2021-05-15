package main;


import events.*;
import events.musicevents.*;
import music.MusicReactionController;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

/**
 * Discord bot with some helpful commands
 */

public class DiscordBot {


    public static void main(String[] args) throws Exception {
        ArrayList<AllCommands> COMMANDS = new ArrayList<>();
        ListenerAdapter[] adapters = {new HelloThere(), new OnlineCommand(), new CommandNotFound(COMMANDS), new HelpCommand(COMMANDS)
                , new DateCommand(), new ClearCommand(), new MuteCommand(), new UnmuteCommand(), new JoinCommand(), new LeaveCommand(), new StopCommand(),
                new SkipCommand(), new PlayCommand(), new ShuffleCommand(), new PauseCommand(), new MusicReactionController()};

        JDABuilder jdaBuilder = JDABuilder.createDefault("Njc1MzA5Mjc3MTUzMzI5MTUy.Xj1Qxg.n9K0DsIBAucKek7jqHYAFz7cKN4");
        JDA jda = jdaBuilder.build();
        Thread t1 = new Thread(new Shut(jda));
        t1.start();
        for (ListenerAdapter listenerAdapters : adapters) {
            jda.addEventListener(listenerAdapters);
        }


        AllCommands online = new AllCommands("online", "shows people who are online");
        COMMANDS.add(online);


        AllCommands help = new AllCommands("help", "shows commands");
        COMMANDS.add(help);

        AllCommands date = new AllCommands("date", "shows date");
        COMMANDS.add(date);

        AllCommands clear = new AllCommands("clear", "clears a specified amount of messages");
        COMMANDS.add(clear);

        AllCommands mute = new AllCommands("mute", "mute someone (only Admin)");
        COMMANDS.add(mute);


        AllCommands unmute = new AllCommands("unmute", "unmute someone (only Admin)");
        COMMANDS.add(unmute);


        AllCommands join = new AllCommands("join", "Bot connects to your VoiceChannel");
        COMMANDS.add(join);


        AllCommands leave = new AllCommands("leave", "Bot leaves your VoiceChannel");
        COMMANDS.add(leave);


        AllCommands stop = new AllCommands("stop", "Stops the Bot from playing music. Also clears queue");
        COMMANDS.add(stop);


        AllCommands skip = new AllCommands("skip", "Skips song");
        COMMANDS.add(skip);


        AllCommands pause = new AllCommands("pause", "Pauses/Unpauses Song");
        COMMANDS.add(pause);


        AllCommands shuffle = new AllCommands("shuffle", "Shuffles queue");
        COMMANDS.add(shuffle);


        AllCommands play = new AllCommands("play", "Bot plays Song or Playlist");
        COMMANDS.add(play);
    }


}


