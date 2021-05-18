package server.commandHandler.utility;

import common.dataTransferObjects.CommandTransferObject;
import server.commandHandler.commands.*;

import java.util.*;
import java.util.stream.Stream;
import common.*;
/**
 * Stores and manages the Server.CommandHandler.commands
 * **/
public class CommandManager {

    private Map<String,Executable> commands = Collections.synchronizedMap(new HashMap());
    private ArrayDeque<String> history = new ArrayDeque<String>(13);
    public CommandManager(Executable... commandsParam){
        for (Executable c : commandsParam){
            commands.put(c.getName(),c);
        }
        history.clear();
    }
    /**
     * Executes the command with specified name
     * @return exit status of executable command
     * **/
    public Response executeCommand(Query query){
        String key = query.getDTOCommand().getArgument();
        String command = query.getDTOCommand().getCommand();
        key = key.trim();
        query.setDTOCommand(new CommandTransferObject().setCommand(command).setArgument(key));
        history.add(command);
        ServerOutput.info("Recieved "+command+" "+key);
        Response response;
        if (commands.containsKey(command)){
            response = commands.get(command).execute(query);
            if (response.failed()){
                ServerOutput.warning("Failed to execute command "+command+" "+key);
            }
        }
        else{
            ServerOutput.warning("No such command "+command+" found");
            response = new Response("No such command "+command+" found",true,Instruction.ASK_COMMAND);
        }
        return response;
    }

    public void addCommand(Executable command){
        commands.put(command.getName(),command);
    }
    public Stream<Executable> getStream(){
        return this.commands.values().stream();
    }
    public ArrayDeque<String> getHistory(){return this.history;};


}
