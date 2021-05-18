package server;

import server.commandHandler.utility.CommandHandler;
import server.commandHandler.utility.CommandManager;
import server.commandHandler.utility.DataBaseManager;
import server.commandHandler.utility.ServerOutput;
import server.queryReader.QueryReader;
import server.responseSender.ResponseSender;
import common.*;
import server.utility.Encryptor;
import server.utility.UserRegistrant;
import server.utility.UserValidator;
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
    private CommandManager commandManager;
    private Connection dataBaseConnection;
    private DataBaseManager dataBaseManager;
    private InputStream in;
    private QueryReader queryReader;
    private UserValidator userValidator;
    public QueryHandler(CommandManager commandManager,
                        ExecutorService fixedThreadPool,
                        ForkJoinPool forkJoinPool,
                        SocketChannel socketChannel,
                        Connection connection,
                        DataBaseManager dataBaseManager
    ){
        this.forkJoinPool = forkJoinPool;
        this.socketChannel = socketChannel;
        this.fixedThreadPool = fixedThreadPool;
        this.commandManager = commandManager;
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
            userValidator = new UserValidator(dataBaseConnection,dataBaseManager);
        } catch (NoSuchAlgorithmException e) {
            ServerOutput.warning("Setting user validator error");
            return;
        }
    }
    public void run(){
        boolean loggedIn = logIn(userValidator,queryReader,in);
        boolean isRunning = true;
        while(isRunning && loggedIn){

            Query query = queryReader.getQuery(in);
            if (query==null){
                break;
            }
            FutureTask<Boolean> futureLoggedIn = new FutureTask<>(new ValidateUserTask(query.getLogin(),query.getPassword(),userValidator));
            new Thread(futureLoggedIn).start();
            Future<Response> response = forkJoinPool.submit(new CommandHandler(query, commandManager));
            try {
                if(!futureLoggedIn.get()){
                    fixedThreadPool.execute(new ResponseSender(new Response("You are not logged in",true,Instruction.SIGN_IN), socketChannel));
                    loggedIn = futureLoggedIn.get();
                } else {

                    fixedThreadPool.execute(new ResponseSender(response.get(), socketChannel));
                    if (response.get().getInstruction().equals(Instruction.EXIT)) isRunning = false;
                }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
    }

    private boolean logIn(UserValidator userValidator, QueryReader queryReader, InputStream in){
        boolean loggedIn = false;
        while(!loggedIn){
            System.out.println(1);
            Query auth = queryReader.getQuery(in);
            if (auth.isSignUp()){
                Response authResponse;
                if(userValidator.validate(auth.getLogin(),auth.getPassword())) {
                    authResponse = new Response("This login already exists",true,Instruction.SIGN_UP);
                } else {
                    try {
                        UserRegistrant userRegistrant = new UserRegistrant(dataBaseConnection,dataBaseManager,new Encryptor());
                        if (!userRegistrant.register(auth.getLogin(),auth.getPassword()))System.out.println("Failed to register");
                        authResponse = new Response("Successfully signed up",false,Instruction.SIGN_IN);
                    } catch (NoSuchAlgorithmException e) {
                        ServerOutput.warning("Encryptor debil");
                        return false;
                    }
                }
                fixedThreadPool.execute(new ResponseSender(authResponse,socketChannel));
            } else {
                loggedIn = userValidator.validate(auth.getLogin(), auth.getPassword());
                Response authResponse = new Response(
                        loggedIn ?
                                "You are logged in" :
                                "Incorrect login or password",
                        !loggedIn,
                        loggedIn? Instruction.ASK_COMMAND :
                                Instruction.SIGN_IN
                );
                fixedThreadPool.execute(new ResponseSender(authResponse,socketChannel));
            }
        }
        return true;
    }
}
