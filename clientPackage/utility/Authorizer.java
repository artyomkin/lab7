package clientPackage.utility;

import clientPackage.console.UserOutput;
import clientPackage.exceptions.ConnectionException;
import clientPackage.exceptions.EndOfFileException;
import clientPackage.inputHandler.Asker;
import common.Instruction;
import common.Pair;
import common.Query;
import common.Response;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Authorizer {
    private ConnectionReciever connectionReciever;
    private Asker asker;
    private QuerySender querySender;
    private ResponseReader responseReader;
    private String login;
    private String password;

    public Authorizer(ConnectionReciever connectionReciever, Asker asker, QuerySender querySender, ResponseReader responseReader){
        this.asker = asker;
        this.querySender = querySender;
        this.connectionReciever = connectionReciever;
        this.responseReader = responseReader;
    }

    public void logIn(Pair<Selector,SocketChannel> connection){
        try{
            UserOutput.println("Connecting to server...");
            Selector selector = connection.first;
            SocketChannel socketChannel = connection.second;
            boolean isSignUp = asker.isSignUp();//=false;
            if(isSignUp){
                while(!signUp(selector,socketChannel));
            }
            while(!signIn(selector,socketChannel));
        } catch (IOException | EndOfFileException | ConnectionException e){
            UserOutput.println("Connection error");
        }
    }
    public boolean signUp(Selector selector,SocketChannel socketChannel) throws IOException, EndOfFileException, ConnectionException {
        selector.select();
        Set keySet = selector.selectedKeys();
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            SelectionKey key = (SelectionKey) it.next();
            it.remove();
            if (key.isWritable()) {
                String authPair[];
                UserOutput.println("Signing up...");
                authPair = asker.signUp();
                login = authPair[0];
                password = authPair[1];
                Query authQuery = new Query()
                        .setLogin(login)
                        .setPassword(password)
                        .setSignIn(false)
                        .setSignUp(true);
                querySender.sendQuery(authQuery, socketChannel);

                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                Response response = responseReader.getResponse(socketChannel);
                UserOutput.println(response.getContent());
                if (response.getInstruction().equals(Instruction.SIGN_IN)) {
                    UserOutput.println("You are signed up!");
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                    return true;
                }
                socketChannel.register(selector, SelectionKey.OP_WRITE);
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
                login = /*"123"*/authPair[0];
                password = /*"123"*/authPair[1];
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
    public String getLogin(){
        return this.login;
    }
    public String getPassword(){
        return this.password;
    }
}
