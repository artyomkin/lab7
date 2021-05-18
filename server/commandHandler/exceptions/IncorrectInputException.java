package server.commandHandler.exceptions;

/**
 * Is thrown when user gives inappropriate input, (for example gives a string instead of number)
 * **/
public class IncorrectInputException extends Exception{
    public IncorrectInputException(String message){
        super(message);
    }
}
