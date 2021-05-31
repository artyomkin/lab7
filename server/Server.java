package server;

import server.commandHandler.utility.CollectionManager;
import server.commandHandler.utility.CommandManager;
import server.commandHandler.utility.DataBaseManager;
import server.connectionReciever.ClientConnection;
import server.connectionReciever.DatabaseConnection;

import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Server {

    private ClientConnection clientConnection;
    private CollectionManager collectionManager;

    public Server(CollectionManager collectionManager) {
        this.clientConnection = new ClientConnection(2232);
        this.collectionManager = collectionManager;
    }

    public void run(ForkJoinPool forkJoinPool,
                    ExecutorService fixedThreadPool,
                    Connection dataBaseConnection,
                    DataBaseManager dataBaseManager) {
         while(true){
            SocketChannel socketChannel = clientConnection.getConnection();

            new Thread(new QueryHandler(collectionManager, fixedThreadPool, forkJoinPool, socketChannel, dataBaseConnection, dataBaseManager)).start();
        }
    }
}
