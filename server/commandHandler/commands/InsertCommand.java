package server.commandHandler.commands;

import common.Stage;
import server.commandHandler.taskClasses.Flat;
import server.commandHandler.utility.CollectionManager;
import common.Instruction;
import common.Query;
import common.Response;
/**
 * Inserts new element with specified key into collection
 * **/
public class InsertCommand extends AbstractCommand{

    private CollectionManager collectionManager;
    public InsertCommand(CollectionManager collectionManager){
        super("insert", "insert new element with specified key");
        this.collectionManager = collectionManager;
    }
    /**
     * Executes the command
     * @return exit status of command
     * **/
    @Override
    public Response execute(Query query){
        if (query.getStage().equals(Stage.BEGINNING)){
            if (query.getDTOCommand().getArgument().isEmpty())
                return new Response("Specify a key",true,Instruction.ASK_COMMAND);
            if (collectionManager.contains(Integer.parseInt(query.getDTOCommand().getArgument())))
                return new Response("Flat with specified key already exists", true, Instruction.ASK_COMMAND);
            return new Response("",false, Instruction.ASK_FLAT, Stage.ENDING, query);
        } else {
            Flat flat = new Flat(query.getDTOFlat());
            flat.setId(Integer.parseInt(query.getDTOCommand().getArgument()));
            System.out.println(query.getLogin());
            flat.setCreator(query.getLogin());
            boolean executed = collectionManager.insert(Integer.parseInt(query.getDTOCommand().getArgument()),flat);
            return new Response(executed ? "Insertion successful" : "Failed to insert entry into the database",!executed,Instruction.ASK_COMMAND);
        }
    }
}