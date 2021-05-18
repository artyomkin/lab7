package server.commandHandler.commands;

import server.commandHandler.utility.CollectionManager;
import common.*;
/**
 * Prints all elements in collection
 * **/
public class ShowCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private String content;
    public ShowCommand(CollectionManager collectionManager){
        super("show","prints all elements");
        this.collectionManager = collectionManager;
        this.content = "";
    }

    /**
     * Executes the command
     * @return exit status of command
     * **/
    @Override
    public Response execute(Query query) {
        if (collectionManager.getLength()==0){
            return new Response("Collection is empty", false, Instruction.ASK_COMMAND);
        } else{
            collectionManager
                    .getStream()
                    .forEach(flat->content+=flat.toString()+"____________________\n");
            Response response = new Response(content, false, Instruction.ASK_COMMAND);
            content = "";
            return response;
        }
    }

}
