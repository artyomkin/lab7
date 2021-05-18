package server.commandHandler.commands;

import server.commandHandler.taskClasses.Flat;
import server.commandHandler.utility.CollectionManager;

import java.util.ArrayList;
import java.util.Iterator;
import common.*;
/**
 * Removes all flats that are more than specified one
 * **/
public class RemoveGreaterCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    public RemoveGreaterCommand(CollectionManager collectionManager){
        super("remove_greater","removes all elements that more than specified");
        this.collectionManager = collectionManager;
    }
    /**
     * Executes the command
     * @return exit status of command
     * **/
    @Override
    public Response execute(Query query){
        if (query.getStage().equals(Stage.BEGINNING)){
            return new Response("",false,Instruction.ASK_FLAT, Stage.ENDING, query);
        } else {
            ArrayList<Integer> flatsToRemove = new ArrayList<>();
            collectionManager.getStream()
                    .filter(flat -> flat.compareTo(new Flat(query.getDTOFlat()))>0)
                    .filter(flat -> flat.getCreator().equals(query.getLogin()))
                    .forEach(flat -> flatsToRemove.add(flat.getID()));
            boolean removingSuccessful = flatsToRemove.stream()
                    .map(ID->collectionManager.remove(ID))
                    .anyMatch(executed->!executed);
            return new Response(removingSuccessful ? "Removing successful" : "Nothing was removed",!removingSuccessful, Instruction.ASK_COMMAND);
        }

    }
}
