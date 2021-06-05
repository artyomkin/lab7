package server.commandHandler.commands;

import server.commandHandler.taskClasses.Flat;
import server.commandHandler.utility.CollectionManager;
import common.*;
/**
 * Updates the element by its key
 * **/
public class UpdateCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    public UpdateCommand(CollectionManager collectionManager){
        super("update","updates the element with specified id");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command
     * @return exit status of command
     * **/
    @Override
    public Response execute(Query query){
        if (query.getStage().equals(Stage.BEGINNING)) {
            Integer key;
            try {
                key = Integer.parseInt(query.getDTOCommand().getArgument());
            } catch (NumberFormatException e){
                return new Response("Incorrect key",true,Instruction.ASK_COMMAND);
            }
            if (!collectionManager.contains(key))
                return new Response("No flat with specified key found",true,Instruction.ASK_COMMAND);
            if(!collectionManager.getElementByKey(key).getCreator().equals(query.getLogin()))
                return new Response("You have no permission to update it",true,Instruction.ASK_COMMAND);

            return new Response("",false,Instruction.ASK_FLAT,Stage.ENDING,query);
        } else {
            Integer key = Integer.parseInt(query.getDTOCommand().getArgument());
            Flat flat = new Flat(query.getDTOFlat());
            flat.setId(key);
            boolean success = collectionManager.replace(key, flat);
            return new Response(success ? "Updating successful" : "Flat wasn't replaced",!success,Instruction.ASK_COMMAND);
        }
    }
}
