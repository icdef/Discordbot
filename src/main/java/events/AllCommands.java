package events;

import org.jetbrains.annotations.NotNull;

public class AllCommands implements Comparable<AllCommands> {
    private String command;
    private String description;

    public AllCommands(String command, String description){
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "!"+command+": "+description;
    }

    @Override
    public int compareTo(@NotNull AllCommands o) {
        return command.compareTo(o.command);
    }
}
