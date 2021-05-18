package server.commandHandler.commands;
import common.Instruction;
import common.Query;
import common.Response;
/**
 * Execute the script from file
 * **/
public class ExecuteScriptCommand extends AbstractCommand{

    public ExecuteScriptCommand(){
        super("execute_script","read and execute script from specified file. " +
                "Script contains same Server.CommandHandler.commands as user uses in interactive mode");
    }
    /**
     * Executes the command
     * **/
    @Override
    public Response execute(Query query) {
        if (query.getDTOCommand().getArgument().trim().isEmpty()) return new Response("Specify a filepath",true,Instruction.ASK_COMMAND);
        return new Response(query.getDTOCommand().getArgument(),false, Instruction.SCRIPT);
    }
}
