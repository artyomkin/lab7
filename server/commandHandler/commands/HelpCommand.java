package server.commandHandler.commands;

import server.commandHandler.utility.CommandManager;
import common.Instruction;
import common.Query;
import common.Response;
/**
 * Prints the information about available Server.CommandHandler.commands that command manager contains
 * **/
public class HelpCommand extends AbstractCommand{

    private CommandManager commandManager;

    public HelpCommand(CommandManager commandManager){
        super("help","prints the information about available commands");
        this.commandManager = commandManager;
    }
    /**
     * Executes the command
     * @return exit status of command
     * **/
    public Response execute(Query query){
        String res = "";
        commandManager.getStream()
                .forEach(command->updateResult(res,command.getName() + " " + command.getDescription() + "\n"));
        return new Response(res,false, Instruction.ASK_COMMAND);
    }
    private String updateResult(String res,String add){
        return res+add;
    }
}
