package server.commandHandler.exceptions;

public class QuerySizeLimitException extends Exception{
    public QuerySizeLimitException(String msg){
        super(msg);
    }
}
