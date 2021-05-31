package clientPackage;

import clientPackage.console.UserInput;
import clientPackage.console.UserOutput;
import clientPackage.exceptions.ConnectionException;
import clientPackage.exceptions.EndOfFileException;
import clientPackage.inputHandler.Asker;
import clientPackage.inputHandler.Validator;
import clientPackage.utility.Authorizer;
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
    private Authorizer authorizer;
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
        SocketChannel socketChannel;
        Selector selector;
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

        Pair<Selector, SocketChannel> connectionPair = null;
        try {
            connectionPair = connectionReciever.connectToServer();
        } catch (ConnectionException e) {
            UserOutput.println("Connecting with server failed");
        }
        authorizer = new Authorizer(connectionReciever,asker,querySender,responseReader);
        if(filepath.isEmpty()){
            authorizer.logIn(connectionPair);
            this.login = authorizer.getLogin();
            this.password = authorizer.getPassword();
        }

        selector = connectionPair.first;
        socketChannel = connectionPair.second;
        try {
            Query query = null;
            //int cnt = 0;
            while (isRunning) {

                    selector.select();
                    Set keySet = selector.selectedKeys();
                    Iterator it = keySet.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = (SelectionKey) it.next();
                        it.remove();
                        if (key.isWritable()) {
                            if (query == null){

                               /* int argInt = 100 + new Random().nextInt()%100;
                                String arg = ""+argInt;
                                String command = "insert";
                                if (argInt<50){
                                    command = "clear";
                                }*/
                                query = new Query()
                                        .setStage(Stage.BEGINNING)
                                        .setDTOCommand(asker.askValidatedCommand())
                                        //.setDTOCommand(new CommandTransferObject().setCommand("execute_script").setArgument("C:\\Users\\User\\IdeaProjects\\Client\\src\\clientPackage\\inputScript2.txt"))
                                        .setLogin(login)
                                        .setPassword(password);

                            }
                            querySender.sendQuery(query, socketChannel);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        }
                        if (key.isReadable()) {
                            Response response = responseReader.getResponse(socketChannel);
                            if (response.getInstruction() != Instruction.SCRIPT && !response.getContent().isEmpty()) {
                                UserOutput.println(response.getContent());
                            }
                            query = handleResponse(response, socketChannel, selector);
                            if(query!=null){
                                query.setLogin(login).setPassword(password);
                            } else {
                                socketChannel.close();
                                return false;
                            }

                            socketChannel.register(selector, SelectionKey.OP_WRITE);

                        }
                    }
                    //cnt++;
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
               /* UserOutput.println("ASKING FLAT");
                FlatTransferObject flat = new FlatTransferObject()
                        .setArea(12)
                        .setCoordinates(
                                new CoordinatesTransferObject()
                                .setX(1D)
                                .setY(2L)
                        )
                        .setHouse(
                                new HouseTransferObject()
                                .setName("123")
                                .setNumberOfFlatsOnFloor(12)
                                .setYear(12)
                                .setNumberOfFloors(12)
                                .setNumberOfLifts(1)
                        )
                        .setName("123")
                        .setLivingSpace(12)
                        .setCreator("123")
                        .setNumberOfRooms(1)
                        .setPrice(12)
                        .setTransport(TransportTransferObject.NONE);*/
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
                     while(!authorizer.signIn(selector,socketChannel));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ConnectionException e) {
                    e.printStackTrace();
                }
            }
            case SIGN_UP:{
                try{
                   while(!authorizer.signUp(selector,socketChannel));
                } catch (IOException e){
                    e.printStackTrace();
                } catch (ConnectionException e){
                    e.printStackTrace();
                }
            }
            default: return null;
        }

    }

}

