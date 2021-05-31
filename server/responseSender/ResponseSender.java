package server.responseSender;

import server.commandHandler.utility.ServerOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import common.*;
public class ResponseSender{
    private static ResponseSender INSTANCE;

    public static ResponseSender getInstance(){
        if (INSTANCE==null) {
            INSTANCE = new ResponseSender();
        }
        return INSTANCE;
    }

    public synchronized void sendResponse(Response response, SocketChannel socketChannel){
        try{
            byte[] bytes = Serializer.serialize(response);
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            socketChannel.write(byteBuffer);
            ServerOutput.info("Sent new response");
        }catch(IOException e){
            ServerOutput.warning("Failed to send response");
        }
    }
}
