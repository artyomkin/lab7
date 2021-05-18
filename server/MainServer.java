package server;

import server.commandHandler.commands.*;
import server.commandHandler.utility.CollectionManager;
import server.commandHandler.utility.CommandManager;
import server.commandHandler.utility.DataBaseManager;
import server.commandHandler.utility.ServerOutput;
import server.connectionReciever.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;


public class MainServer {
    public static void main(String[] args){
        Connection dataBaseConnection = DatabaseConnection.getConnection();
        if (dataBaseConnection==null) return;
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CollectionManager collectionManager;
        DataBaseManager dataBaseManager = new DataBaseManager(dataBaseConnection);
        try {
            collectionManager = new CollectionManager(dataBaseManager);
        } catch (SQLException throwables) {
            ServerOutput.warning("Failed to load collection");
            return;
        }
        CommandManager commandManager = new CommandManager(
                new ShowCommand(collectionManager),
                new ReplaceIfLowerCommand(collectionManager),
                new ClearCommand(collectionManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new FilterByTransportCommand(collectionManager),
                new InfoCommand(collectionManager),
                new InsertCommand(collectionManager),
                new MinByAreaCommand(collectionManager),
                new RemoveAllByHouseCommand(collectionManager),
                new RemoveGreaterCommand(collectionManager),
                new RemoveKeyCommand(collectionManager),
                new UpdateCommand(collectionManager)
        );
        commandManager.addCommand(new HelpCommand(commandManager));
        commandManager.addCommand(new HistoryCommand(commandManager));
        new Server(commandManager).run(forkJoinPool,fixedThreadPool, dataBaseConnection, dataBaseManager);

    }
    
}
