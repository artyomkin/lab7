package clientPackage;

import clientPackage.console.UserInput;
import clientPackage.console.UserOutput;
import clientPackage.exceptions.ConnectionException;
import clientPackage.exceptions.EndOfFileException;
import clientPackage.inputHandler.Asker;
import clientPackage.inputHandler.Validator;
import clientPackage.utility.ConnectionReciever;
import clientPackage.utility.QuerySender;
import clientPackage.utility.ResponseReader;
import common.*;
import common.dataTransferObjects.*;

import java.io.*;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;


public class Client implements Runnable{

    private Validator validator;
    private ConnectionReciever connectionReciever;
    private Asker asker;
    private boolean isRunning;
    private Stack<String> callStack;
    private QuerySender querySender;
    private ResponseReader responseReader;
    private String login;
    private String password;

    public Client(String host, int port){
        this.connectionReciever = new ConnectionReciever(host, port);
        this.validator = new Validator();
        this.isRunning = true;
        this.callStack = new Stack();
        this.querySender = new QuerySender();
        this.responseReader = new ResponseReader();
    }

    public void run(){
        run("");
    }
    public boolean run(String filepath) {
        UserInput userInput;
        SocketChannel socketChannel = null;
        Pair<Selector,SocketChannel> connection = null;
        Selector selector = null;
        if (filepath == "") {
            userInput = new UserInput(new Scanner(System.in));
            this.asker = new Asker(userInput, validator);
        } else {
            try {
                userInput = new UserInput(new BufferedReader(new FileReader(filepath)));
                this.asker = new Asker(userInput, validator);
            } catch (FileNotFoundException e) {
                UserOutput.println("File " + filepath + " not found");
                return true;
            }
        }
        Pair<Selector, SocketChannel> p = null;
        if(!filepath.isEmpty()) p = logIn();
        else {
            try {
                p = connectionReciever.connectToServer();
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
        }
        selector = p.first;
        socketChannel = p.second;
        try {
            Query query = null;
            while (isRunning) {

                    selector.select();
                    Set keySet = selector.selectedKeys();
                    Iterator it = keySet.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = (SelectionKey) it.next();
                        it.remove();
                        if (key.isWritable()) {
                            if (query == null){
                                query = new Query()
                                        .setStage(Stage.BEGINNING)
                                        .setDTOCommand(asker.askValidatedCommand())
                                        .setLogin(login)
                                        .setPassword(password);

                            }
                            querySender.sendQuery(query, socketChannel);
                            System.out.println("Отправил запрос");
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        }
                        if (key.isReadable()) {
                            Response response = responseReader.getResponse(socketChannel);
                            System.out.println("Получил ответ");
                            if (response.getInstruction() != Instruction.SCRIPT && !response.getContent().isEmpty()) {
                                UserOutput.println(response.getContent());
                            }
                            query = handleResponse(response, socketChannel, selector).setLogin(login).setPassword(password);

                            socketChannel.register(selector, SelectionKey.OP_WRITE);

                        }
                    }
            }
            } catch(IOException e){
                UserOutput.println("Selector exception");
                return false;
            } catch(EndOfFileException e){
                return true;
            } catch(ConnectionException e){
                return false;
            }
            return true;
        }

    public Query handleResponse (Response response, SocketChannel socketChannel, Selector selector) throws EndOfFileException {

        Query query = new Query();
        query.setDTOCommand(response.getQuery().getDTOCommand());
        query.setStage(response.getStage());

        switch (response.getInstruction()){
            case ASK_COMMAND: {
                CommandTransferObject command = asker.askValidatedCommand();
                if(command!=null) return query.setDTOCommand(command);
                else return null;
            }
            case ASK_FLAT: {
                FlatTransferObject flat = asker.askValidatedFlat();
                if(flat!=null) return query.setDTOFlat(flat);
                else return null;
            }
            case ASK_COORDINATES: {
                CoordinatesTransferObject coords = asker.askValidatedCoordinates();
                if(coords!=null) return query.setDTOCoordinates(coords);
                else return null;
            }
            case ASK_HOUSE: {
                HouseTransferObject house = asker.askValidatedHouse();
                if(house!=null) return query.setDTOHouse(house);
                else return null;
            }
            case SCRIPT:{
                boolean recursion =
                        callStack.stream()
                            .anyMatch(filepath -> filepath.equals(response.getContent()));
                if (recursion){
                    UserOutput.println("Failed to execute because of recursion");
                    return null;
                }
                callStack.push(response.getContent());
                try {
                    socketChannel.register(selector,SelectionKey.OP_WRITE);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
                Asker saveAsker = asker;
                if(!run(response.getContent())){
                    isRunning = false;
                }
                callStack.pop();
                asker = saveAsker;
                if(isRunning){
                    return query.setDTOCommand(asker.askValidatedCommand());
                } else {
                    return null;
                }
            }
            case EXIT:{
                isRunning = false;
                return new Query();
            }
            case SIGN_IN:{
                try {
                     while(!signIn(selector,socketChannel));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ConnectionException e) {
                    e.printStackTrace();
                }
            }
            case SIGN_UP:{
                try{
                   while(!signUp(selector,socketChannel));
                } catch (IOException e){
                    e.printStackTrace();
                } catch (ConnectionException e){
                    e.printStackTrace();
                }
            }
            default: return null;
        }

    }
    public Pair<Selector,SocketChannel> logIn(){
        SocketChannel socketChannel = null;
        Pair<Selector,SocketChannel> connection;
        Selector selector;
        try{
            UserOutput.println("Connecting to server...");
            connection = connectionReciever.connectToServer();
            selector = connection.first;
            socketChannel = connection.second;
            boolean isSignUp = asker.isSignUp();
            if(isSignUp){
               while(!signUp(selector,socketChannel));
            }
            while(!signIn(selector,socketChannel));
            return connection;
        } catch (IOException | EndOfFileException | ConnectionException e){
            UserOutput.println("Connection error");
            return null;
        }
    }
    public boolean signUp(Selector selector,SocketChannel socketChannel) throws IOException, EndOfFileException, ConnectionException {

        selector.select();
        Set keySet = selector.selectedKeys();
        Iterator it = keySet.iterator();
        while(it.hasNext()){
            SelectionKey key = (SelectionKey) it.next();
            it.remove();
            if(key.isWritable()){
                String authPair[];
                UserOutput.println("Signing up...");
                authPair = asker.signUp();
                String preLogin = authPair[0];
                String prePassword = authPair[1];
                Query authQuery = new Query()
                        .setLogin(preLogin)
                        .setPassword(prePassword)
                        .setSignIn(false)
                        .setSignUp(true);
                querySender.sendQuery(authQuery,socketChannel);
                socketChannel.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                Response response = responseReader.getResponse(socketChannel);
                UserOutput.println(response.getContent());
                if (response.getInstruction().equals(Instruction.SIGN_IN)){
                    UserOutput.println("You are signed up!");
                    socketChannel.register(selector,SelectionKey.OP_WRITE);
                    return true;
                }
                socketChannel.register(selector,SelectionKey.OP_WRITE);
            }
        }
        return false;
    }

    public boolean signIn(Selector selector, SocketChannel socketChannel) throws IOException, EndOfFileException, ConnectionException {
        while(selector.select(500)==0);
        Set keySet = selector.selectedKeys();
        Iterator it = keySet.iterator();
        Query authQuery = null;
        while(it.hasNext()){
            SelectionKey key = (SelectionKey) it.next();
            it.remove();
            if(key.isWritable()){
                String[] authPair;
                UserOutput.println("Signing in...");
                authPair = asker.signIn();
                login = authPair[0];
                password = authPair[1];
                authQuery = new Query()
                        .setLogin(login)
                        .setPassword(password)
                        .setSignUp(false);

                querySender.sendQuery(authQuery,socketChannel);
                socketChannel.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                Response response = responseReader.getResponse(socketChannel);
                UserOutput.println(response.getContent());
                if(response.getInstruction().equals(Instruction.ASK_COMMAND)) {
                    socketChannel.register(selector,SelectionKey.OP_WRITE);
                    return true;
                }
                socketChannel.register(selector,SelectionKey.OP_WRITE);
            }
        }
        return false;
    }
}

