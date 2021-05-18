package clientPackage.utility;

import clientPackage.console.UserOutput;
import clientPackage.exceptions.ConnectionException;
import common.Serializer;
import common.Response;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class ResponseReader {

    public Response getResponse(SocketChannel socketChannel) throws ConnectionException {

        byte[] bytes = new byte[1024];
        int numberOfBytesRead;
        byte[] resultBytes = new byte[0];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Response response;
        while(true){
            try {
                numberOfBytesRead = socketChannel.read(byteBuffer);
                if (numberOfBytesRead<0) throw new SocketException();
                byte[] tempBytes = new byte[resultBytes.length + numberOfBytesRead];
                System.arraycopy(resultBytes, 0, tempBytes, 0, resultBytes.length);
                System.arraycopy(bytes, 0, tempBytes, resultBytes.length, numberOfBytesRead);
                resultBytes = tempBytes;
                byteBuffer.clear();
                try {
                    response = (Response) Serializer.deserialize(resultBytes);
                    return response;
                } catch (IOException | ClassNotFoundException | ClassCastException e) {
                    continue;
                }
            } catch (SocketException e) {
                UserOutput.println("Connection with server disrupted");
                break;
            } catch (IOException e){
                UserOutput.println("Reading error");
                break;
            }
        }
        throw new ConnectionException("Connection disrupted");
    }

}
