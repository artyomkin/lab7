package server.commandHandler.utility;

import server.MainServer;

import java.util.logging.Logger;

public class ServerOutput {
    private static final Logger logger = Logger.getLogger(MainServer.class.getName());
    public static void info(String message){
        logger.info(message);
    }
    public static void warning(String message){
        logger.warning(message);
    }
}
