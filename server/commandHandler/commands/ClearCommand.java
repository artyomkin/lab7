package server.commandHandler.commands;
import common.Instruction;
import common.Query;
import common.Response;
import server.commandHandler.utility.CollectionManager;
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
        collectionManager.getStream()
                .filter(flat->flat.getCreator().equals(query.getLogin()))
                .map(flat->flat.getID())
                .forEach(id->collectionManager.remove(id));
        return new Response("Collection is cleared",false, Instruction.ASK_COMMAND);
    }
}
