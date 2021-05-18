package clientPackage.utility;

import clientPackage.console.UserOutput;
import clientPackage.exceptions.ConnectionException;
import common.Pair;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ConnectionReciever {

    private String host;
    private int port;

    public ConnectionReciever(String host, int port){
        this.host = host;
        this.port = port;
    }

    public Pair<Selector,SocketChannel> connectToServer() throws ConnectionException {
        int maxConnectionAttempts = 10;
        int connectionAttempts = 0;
        Pair<Selector,SocketChannel> connection;
        while(connectionAttempts<=maxConnectionAttempts){
            try{
                connectionAttempts++;
                connection = tryToConnect();
                return connection;
            } catch (IOException e){
                UserOutput.println("Trying to reconnect to server...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                continue;
            }
        }
        throw new ConnectionException("Failed to connect to server");
    }
    private Pair<Selector,SocketChannel> tryToConnect() throws IOException{
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(host,port));
            socketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            socketChannel.register(selector,SelectionKey.OP_WRITE);
            return new Pair(selector,socketChannel);

    }

}
