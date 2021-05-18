package server.responseSender;

import server.commandHandler.utility.ServerOutput;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import common.*;
public class ResponseSender implements Runnable{

    private SocketChannel socketChannel;
    private Response response;

    public ResponseSender(Response response, SocketChannel socketChannel){
        this.response = response;
        this.socketChannel = socketChannel;
    }

    public void run(){
        try{
            byte[] bytes = Serializer.serialize(response);
            socketChannel.socket().getOutputStream().write(bytes);
            ServerOutput.info("Sent new response");
            if(response.getInstruction() == Instruction.EXIT){
                socketChannel.close();
            }
        }catch(IOException e){
            ServerOutput.warning("Failed to send response");
        }
    }
}
