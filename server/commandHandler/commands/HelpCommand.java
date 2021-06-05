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
    private String content;

    public HelpCommand(CommandManager commandManager){
        super("help","prints the information about available commands");
        this.commandManager = commandManager;
    }
    /**
     * Executes the command
     * @return exit status of command
     * **/
    public Response execute(Query query){
        commandManager.getStream()
                .forEach(command->content+=command.getName()+' '+command.getDescription()+'\n');
        Response result = new Response(content,false, Instruction.ASK_COMMAND);
        content = "";
        return result;
    }
}
