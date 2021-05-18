package clientPackage.console;

import clientPackage.exceptions.EndOfFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class UserInput {
    private Scanner scanner;
    private BufferedReader bufferedReader;
    private static final String REQUEST_COMMAND = "Enter your command:";
    public UserInput(Scanner scanner){
        this.scanner = scanner;
        this.bufferedReader = null;
    }
    public UserInput(BufferedReader bufferedReader){
        this.bufferedReader = bufferedReader;
        this.scanner = null;
    }
    public String getLine() throws EndOfFileException{
        String input = "";
        if(isInteractive()){
            input = scanner.nextLine();
        } else {
            try{
                input = bufferedReader.readLine();
                if (input==null) throw new EndOfFileException();
            } catch (IOException e){
                UserOutput.println(e.getMessage());
            }
        }
        return input;
    }
    public Integer getInt() throws EndOfFileException{
        Integer res = null;
        try{
            res = Integer.parseInt(getLine().trim());
        }catch (NumberFormatException e){
            UserOutput.println("Incorrect input");
        }
        return res;
    }
    public boolean isInteractive(){
        return scanner!=null && bufferedReader==null;
    }

    public String[] askCommand() throws EndOfFileException{
        if (isInteractive()) UserOutput.println(REQUEST_COMMAND);
        String[] res = (getLine().trim()+" ").split(" ",2);
        if(res[1].isEmpty()) res[1] = " ";
        return res;
    }
    public String askPropertyString(String requestProperty) throws EndOfFileException{
        if (isInteractive()) UserOutput.println(requestProperty+":");
        return getLine();
    }
    public Integer askPropertyInt(String requestProperty) throws EndOfFileException{
        Integer res = null;
        if(isInteractive()){
            do{
                UserOutput.println(requestProperty);
                res = getInt();
            }while(res==null);
        } else {
            try{
                return Integer.parseInt(getLine());
            } catch (NumberFormatException e){
                return null;
            }
        }
        return res;
    }

}
