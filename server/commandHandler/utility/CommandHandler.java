package server.commandHandler.utility;

import java.util.concurrent.RecursiveTask;
import common.*;
public class CommandHandler extends RecursiveTask<Response> {
    private Query query;
    private CommandManager commandManager;

    public CommandHandler(Query query, CommandManager commandManager){
        this.query = query;
        this.commandManager = commandManager;
    }
    public Response compute(){
        return commandManager.executeCommand(query);
    }
}
