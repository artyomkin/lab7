package server.responseSender;

import common.Response;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SocketChannel;

public class SendingResponseTask implements Runnable{

    private Response response;
    private SocketChannel socketChannel;

    public SendingResponseTask(Response response, SocketChannel socketChannel){
        this.response = response;
        this.socketChannel = socketChannel;
    }

    public void run(){
        ResponseSender.getInstance().sendResponse(response,socketChannel);
    }

}
