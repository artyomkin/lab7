package common.dataTransferObjects;


import java.io.Serializable;

public class CommandTransferObject  implements Serializable {
    private String command;
    private String argument;
    public CommandTransferObject(){
        this.argument = "";
        this.command = "";
    }

    public String getArgument() {
        return argument;
    }

    public String getCommand() {
        return command;
    }

    public CommandTransferObject setArgument(String argument) {
        this.argument = argument;
        return this;
    }

    public CommandTransferObject setCommand(String command) {
        this.command = command;
        return this;
    }
}
