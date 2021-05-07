package main;


import events.*;
import events.musicevents.*;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.ArrayList;

/**
 * Discord bot with some helpful commands
 */

public class DiscordBot {


    public static void main(String[] args) throws Exception {
         ArrayList<AllCommands> COMMANDS = new ArrayList<>();
         JDABuilder jdaBuilder = JDABuilder.createDefault("");
         JDA jda = jdaBuilder.build();
        Thread t1 = new Thread(new Shut(jda));
        t1.start();

        jda.addEventListener(new HelloThere());

        AllCommands online = new AllCommands("online","shows people who are online");
        COMMANDS.add(online);
        jda.addEventListener(new OnlineCommand());

        jda.addEventListener(new CommandNotFound(COMMANDS));

        AllCommands help = new AllCommands("help","shows commands");
        COMMANDS.add(help);
        jda.addEventListener(new HelpCommand(COMMANDS));

        jda.addEventListener(new DateCommand());
        AllCommands date = new AllCommands("date","shows date");
        COMMANDS.add(date);

        AllCommands clear = new AllCommands("clear","clears a specified amount of messages");
        COMMANDS.add(clear);
        jda.addEventListener(new ClearCommand());

        jda.addEventListener(new MuteCommand());
        AllCommands mute = new AllCommands("mute","mute someone (only Admin)");
        COMMANDS.add(mute);

        jda.addEventListener(new UnmuteCommand());
        AllCommands unmute = new AllCommands("unmute","unmute someone (only Admin)");
        COMMANDS.add(unmute);


        jda.addEventListener(new JoinCommand());
        AllCommands join = new AllCommands("join","Bot connects to your VoiceChannel");
        COMMANDS.add(join);

        jda.addEventListener(new LeaveCommand());
        AllCommands leave = new AllCommands("leave","Bot leaves your VoiceChannel");
        COMMANDS.add(leave);

        jda.addEventListener(new StopCommand());
        AllCommands stop = new AllCommands("stop","Stops the Bot from playing music. Also clears queue");
        COMMANDS.add(stop);

        jda.addEventListener(new SkipCommand());
        AllCommands skip = new AllCommands("skip","Skips song");
        COMMANDS.add(skip);

        jda.addEventListener(new PauseCommand());
        AllCommands pause = new AllCommands("pause","Pauses/Unpauses Song");
        COMMANDS.add(pause);

        jda.addEventListener(new ShuffleCommand());
        AllCommands shuffle = new AllCommands("shuffle","Shuffles queue");
        COMMANDS.add(shuffle);

        jda.addEventListener(new PlayCommand());
        AllCommands play = new AllCommands("play","Bot plays Song or Playlist");
        COMMANDS.add(play);
    }


}


