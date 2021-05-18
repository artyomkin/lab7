package server.commandHandler.commands;

import server.commandHandler.utility.CollectionManager;
import common.Instruction;
import common.Query;
import common.Response;
/**
 * Interrupt the programm
 * **/
public class ExitCommand extends AbstractCommand{
    public ExitCommand(){
        super("exit","stop the programme");
    }
    /**
     * Executes the command
     * @return exit status of command
     * **/
    @Override
    public Response execute(Query query) {
        return new Response("",false,Instruction.EXIT);
    }
}
