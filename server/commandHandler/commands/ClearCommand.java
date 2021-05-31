package server.commandHandler.commands;
import common.Instruction;
import common.Query;
import common.Response;
import server.commandHandler.utility.CollectionManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Clear whole collection by removing all elements
 * **/
public class ClearCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    public ClearCommand(CollectionManager collectionManager){
        super("clear", "clear the collection");
        this.collectionManager = collectionManager;
    }
    /**
     *  Executes the command
     * @return exit status of command
     * **/
    @Override
    public Response execute(Query query) {
        int executed =
        collectionManager.getStream()
                .filter(flat->flat.getCreator().equals(query.getLogin()))
                .map(flat->flat.getID())
                .map(id->collectionManager.remove(id))
                .collect(Collectors.toList()).size();
        return new Response("Removed " + executed + " elements", false, Instruction.ASK_COMMAND);
    }
}
