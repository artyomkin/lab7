package server;

import server.commandHandler.commands.*;
import server.commandHandler.utility.*;
import server.queryReader.QueryReader;
import common.*;
import server.responseSender.SendingResponseTask;
import server.utility.Encryptor;
import server.utility.UserRegistrant;
import server.utility.UserAuthorizer;
import server.utility.ValidateUserTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.concurrent.*;

public class QueryHandler implements Runnable{
    private ForkJoinPool forkJoinPool;
    private SocketChannel socketChannel;
    private ExecutorService fixedThreadPool;
    private CollectionManager collectionManager;
    private Connection dataBaseConnection;
    private DataBaseManager dataBaseManager;
    private InputStream in;
    private QueryReader queryReader;
    private UserAuthorizer userAuthorizer;
    private CommandManager commandManager;
    public QueryHandler(CollectionManager collectionManager,
                        ExecutorService fixedThreadPool,
                        ForkJoinPool forkJoinPool,
                        SocketChannel socketChannel,
                        Connection connection,
                        DataBaseManager dataBaseManager
    )
    {
        this.forkJoinPool = forkJoinPool;
        this.socketChannel = socketChannel;
        this.fixedThreadPool = fixedThreadPool;
        this.collectionManager = collectionManager;
        this.dataBaseConnection = connection;
        this.dataBaseManager = dataBaseManager;

        try {
            in = socketChannel.socket().getInputStream();
        } catch (IOException e) {
            ServerOutput.warning("Socket exception");
            return;
        }
        queryReader = QueryReader.getInstance();
        try {
            userAuthorizer = new UserAuthorizer(dataBaseConnection,dataBaseManager,fixedThreadPool,socketChannel);
        } catch (NoSuchAlgorithmException e) {
            ServerOutput.warning("Setting user validator error");
            return;
        }
        commandManager = new CommandManager(
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
    }
    public void run(){
        boolean loggedIn;
        try{
            loggedIn = userAuthorizer.logIn(queryReader,in);
        } catch (NullPointerException e){
            return;
        }
        boolean isRunning = true;
        while(isRunning && loggedIn){
            Query query = queryReader.getQuery(in);
            if (query==null){
                break;
            }
            FutureTask<Boolean> futureLoggedIn = new FutureTask<>(new ValidateUserTask(query.getLogin(),query.getPassword(), userAuthorizer));
            new Thread(futureLoggedIn).start();
            Future<Response> response = forkJoinPool.submit(new CommandHandler(query, commandManager));
            try {
                if(!futureLoggedIn.get()){
                    fixedThreadPool.execute(new SendingResponseTask(new Response("You are not logged in",true,Instruction.SIGN_IN), socketChannel));

                    loggedIn = futureLoggedIn.get();
                } else {

                    fixedThreadPool.execute(new SendingResponseTask(response.get(), socketChannel));
                    if (response.get().getInstruction().equals(Instruction.EXIT)){
                        isRunning = false;
                        try {
                            Thread.sleep(1000);
                            socketChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
    }


}
