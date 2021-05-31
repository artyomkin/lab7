package server.connectionReciever;

import server.commandHandler.utility.ServerOutput;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.NotYetBoundException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ClientConnection {

    private int port;
    private ServerSocketChannel serverSocketChannel;

    public ClientConnection(int port) {
        this.port = port;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(port);
            serverSocketChannel.bind(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SocketChannel getConnection(){
        try {

            SocketChannel socketChannel = serverSocketChannel.accept();
            ServerOutput.info("Socket channel opened");
            return socketChannel;
        }catch (NotYetBoundException | AlreadyBoundException e){
            ServerOutput.warning("Already bound");
            return null;
        }catch(IOException e){
            ServerOutput.warning("Getting socket channel failed");
            return null;
        }

    }

}
