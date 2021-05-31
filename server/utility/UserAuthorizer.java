package server.utility;

import common.Instruction;
import common.Query;
import common.Response;
import server.Server;
import server.commandHandler.utility.DataBaseManager;
import server.commandHandler.utility.ServerOutput;
import server.connectionReciever.DatabaseConnection;
import server.queryReader.QueryReader;
import server.responseSender.SendingResponseTask;

import java.io.InputStream;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.concurrent.ExecutorService;

public class UserAuthorizer {
    private Connection connection;
    private DataBaseManager dataBaseManager;
    private Encryptor encryptor;
    private ExecutorService fixedThreadPool;
    private SocketChannel socketChannel;

    public UserAuthorizer(Connection connection, DataBaseManager dataBaseManager, ExecutorService fixedThreadPool, SocketChannel socketChannel) throws NoSuchAlgorithmException {
        this.connection = connection;
        this.dataBaseManager = dataBaseManager;
        this.encryptor = Encryptor.getINSTANCE();
        this.fixedThreadPool = fixedThreadPool;
        this.socketChannel = socketChannel;
    }

    public boolean logIn(UserAuthorizer userAuthorizer, QueryReader queryReader, InputStream in) throws NullPointerException{
        boolean loggedIn = false;
        while(!loggedIn){
            Query auth = queryReader.getQuery(in);
            if (auth == null){
                throw new NullPointerException();
            }
            if (auth.isSignUp()){
                Response authResponse;
                if(userAuthorizer.validate(auth.getLogin(),auth.getPassword())) {
                    authResponse = new Response("This login already exists",true, Instruction.SIGN_UP);
                } else {
                    try {
                        UserRegistrant userRegistrant = new UserRegistrant(connection,dataBaseManager,new Encryptor());
                        if (!userRegistrant.register(auth.getLogin(),auth.getPassword())) {
                            authResponse = new Response("Failed to register", true, Instruction.SIGN_UP);
                        }
                        else{
                            System.out.println("Signed up");
                            authResponse = new Response("Successfully signed up",false,Instruction.SIGN_IN);

                        }
                    } catch (NoSuchAlgorithmException e) {
                        ServerOutput.warning("Encryptor exception");
                        return false;
                    }
                }
                fixedThreadPool.execute(new SendingResponseTask(authResponse,socketChannel));
            } else {
                loggedIn = userAuthorizer.validate(auth.getLogin(), auth.getPassword());
                Response authResponse = new Response(
                        loggedIn ?
                                "You are logged in" :
                                "Incorrect login or password",
                        !loggedIn,
                        loggedIn? Instruction.ASK_COMMAND :
                                Instruction.SIGN_IN
                );
                fixedThreadPool.execute(new SendingResponseTask(authResponse,socketChannel));

            }
        }
        return true;
    }

    public boolean validate(String login, String password){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE(login = ?)");
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) return false;
            String passwordHash = resultSet.getString("password");
            String salt = resultSet.getString("salt");
            return passwordHash.equals(encryptor.encrypt(password+salt));
        } catch (SQLException e){
            ServerOutput.warning("Failed to validate user");
            return false;
        }

    }


}
