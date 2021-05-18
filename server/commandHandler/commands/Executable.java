package server.commandHandler.commands;

import common.Query;
import common.Response;

/**
 * Interface for Server.CommandHandler.commands
 * **/
public interface Executable {
    /**
     * Executes the command
     * @return exit status of command
     * **/
    Response execute(Query query);
    /**
     * @return the name of command
     * **/
    String getName();
    /**
     * @return the description of command
     * **/
    String getDescription();
}
