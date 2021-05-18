package server.commandHandler.commands;

import server.commandHandler.utility.CollectionManager;
import common.Instruction;
import common.Query;
import common.Response;
/**
 * Prints the information about the collection
 * **/
public class InfoCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    public InfoCommand(CollectionManager collectionManager){
        super("info", "prints the information about the collection");
        this.collectionManager = collectionManager;
    }
    /**
     * Executes the command
     * @return exit status of command
     * **/
    @Override
    public Response execute(Query query) {
        String content = "Information about the collection:\n" +
        "Type: " + collectionManager.getCollectionClass() + "\n" +
        "Created: " + collectionManager.getInitializationDate() + "\n" +
        "Number of elements: " + collectionManager.getLength() + "\n";
        return new Response(content,false, Instruction.ASK_COMMAND);
    }
}
