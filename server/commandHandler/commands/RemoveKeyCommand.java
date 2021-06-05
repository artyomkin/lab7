package server.commandHandler.commands;

import server.commandHandler.utility.CollectionManager;
import common.*;
/**
 * Removes the flat from collections by its key
 * **/
public class RemoveKeyCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    public RemoveKeyCommand(CollectionManager collectionManager){
        super("remove_key","remove the element from collection by its key");
        this.collectionManager = collectionManager;
    }
    /**
     * Executes the command
     * @return exit status of command
     * **/
    @Override
    public Response execute(Query query) {
        Integer key;
        try{
            key = Integer.parseInt(query.getDTOCommand().getArgument());
        } catch(NumberFormatException e){
            return new Response("Specify a key",true,Instruction.ASK_COMMAND);
        }
        if(!collectionManager.contains(key)){
            return new Response("No such element found",true,Instruction.ASK_COMMAND);
        }
        if(!collectionManager.getElementByKey(key).getCreator().equals(query.getLogin()))
            return new Response("You have no permission to remove this",true,Instruction.ASK_COMMAND);
        if(!collectionManager.remove(key)) return new Response("Failed to remove the flat", true,Instruction.ASK_COMMAND);
        return new Response("Flat with ID " + key + " is removed",false,Instruction.ASK_COMMAND);


    }
}
