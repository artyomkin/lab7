package server.commandHandler.commands;

import server.commandHandler.taskClasses.Transport;
import server.commandHandler.utility.CollectionManager;
import common.Instruction;
import common.Query;
import common.Response;
/**
 * Prints all elements that have the same transport as specified one has
 * **/
public class FilterByTransportCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private String content;
    public FilterByTransportCommand(CollectionManager collectionManager){
        super("filter_by_transport", "print all elements value transport of which equals to specified one");
        this.collectionManager = collectionManager;
        this.content = "";
    }
    public void concatenateToContent(final String add){
        this.content+=add;
    }

    /**
     * Executes the command
     * @return exit status of command
     * **/
    @Override
    public Response execute(Query query) {
        Transport validatedTransport;
        try{
            validatedTransport = Transport.valueOf(query.getDTOCommand().getArgument());
        } catch (IllegalArgumentException e){
            return new Response("Incorrect transport name", true, Instruction.ASK_COMMAND);
        }

        collectionManager.getStream()
                .filter(flat->flat.getTransport().equals(validatedTransport))
                .forEachOrdered(flat->concatenateToContent(flat+"____________________\n"));
        return new Response(content.isEmpty() ? "No flat with specified transport found" : content, content.isEmpty(), Instruction.ASK_COMMAND);

    }
}
