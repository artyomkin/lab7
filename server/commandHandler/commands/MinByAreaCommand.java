package server.commandHandler.commands;

import common.Stage;
import server.commandHandler.taskClasses.Flat;
import server.commandHandler.utility.CollectionManager;

import java.util.Comparator;
import common.Instruction;
import common.Query;
import common.Response;
/**
 * Prints the flat with the least value of area
 * **/
public class MinByAreaCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    public MinByAreaCommand(CollectionManager collectionManager){
        super("min_by_area","print the element with minimal value of area");
        this.collectionManager = collectionManager;
    }
    /**
     * Executes the command
     * @return exit status of command
     * **/

    @Override
    public Response execute(Query query) {
        if (collectionManager.getLength()==0){
            return new Response("Collection is empty",true, Instruction.ASK_COMMAND);
        }

        String content = collectionManager
                .getStream()
                .min(Comparator.comparingLong(Flat::getArea))
                .get().toString();

        return new Response(content,false,Instruction.ASK_COMMAND, Stage.BEGINNING, query);

    }
}
