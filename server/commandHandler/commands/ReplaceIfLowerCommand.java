package server.commandHandler.commands;

import server.commandHandler.taskClasses.Flat;
import server.commandHandler.utility.CollectionManager;
import common.*;
/**
 * Replaces one flat with another if first flat is more than specified
 * **/
public class ReplaceIfLowerCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    public ReplaceIfLowerCommand(CollectionManager collectionManager){
        super("replace_if_lower","replace element by key if new element is less than previous one");
        this.collectionManager = collectionManager;
    }
    /**
     * Executes the command
     * @return exit status of command
     * **/

    @Override
    public Response execute(Query query){
        if (query.getStage().equals(Stage.BEGINNING)){
            Integer key;
            try{
                key = Integer.parseInt(query.getDTOCommand().getArgument());
            } catch(NumberFormatException e){
                return new Response("Specify a key",true,Instruction.ASK_COMMAND);
            }
            if (!collectionManager.contains(Integer.parseInt(query.getDTOCommand().getArgument()))){
                return new Response("No flat with specified key found", true, Instruction.ASK_COMMAND);
            }
            if (!collectionManager.getElementByKey(key).getCreator().equals(query.getLogin()))
                return new Response("You have no permission to replace it",true,Instruction.ASK_COMMAND);
            return new Response("",false, Instruction.ASK_FLAT,Stage.ENDING,query);
        } else {
            Integer key = Integer.parseInt(query.getDTOCommand().getArgument());
            Flat flat = new Flat(query.getDTOFlat());
            flat.setId(key);
            if (flat.compareTo(collectionManager.getElementByKey(key))<0){
                boolean success = collectionManager.replace(key,flat);
                return new Response(success ? "Flat successfully replaced" : "Flat wasn't replaced",!success,Instruction.ASK_COMMAND);
            }
            else {
                return new Response("Flat wasn't replaced",false,Instruction.ASK_COMMAND);
            }
        }
    }
}
