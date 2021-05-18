package server.queryReader;

import server.commandHandler.utility.ServerOutput;

import java.io.IOException;
import java.io.InputStream;
import common.*;
public class QueryReader {
    private static QueryReader INSTANCE;
    public static QueryReader getInstance(){
        if (INSTANCE != null){
            return INSTANCE;
        } else {
            INSTANCE = new QueryReader();
            return INSTANCE;
        }
    }
    public Query getQuery(InputStream in){

        byte[] bytes = new byte[1024];
        byte[] resultBytes = new byte[0];
        int numberOfBytesRead;
        Query query;
        while(true){
            try{
                numberOfBytesRead = in.read(bytes);
                if (numberOfBytesRead<0) continue;
                byte[] tempBytes = new byte[resultBytes.length + numberOfBytesRead];
                System.arraycopy(resultBytes, 0, tempBytes, 0, resultBytes.length);
                System.arraycopy(bytes, 0, tempBytes, resultBytes.length, numberOfBytesRead);
                resultBytes = tempBytes;
                try{
                    query = (Query) Serializer.deserialize(resultBytes);
                    ServerOutput.info("Received new query");
                    return query;
                } catch (IOException | ClassNotFoundException | ClassCastException e){
                    continue;
                }
            } catch (IOException e){
                ServerOutput.warning("Reading query error");
                return null;
            }
        }
    }



}
