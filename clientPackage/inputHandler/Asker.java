package clientPackage.inputHandler;

import clientPackage.console.UserInput;
import clientPackage.console.UserOutput;
import clientPackage.exceptions.EndOfFileException;
import common.dataTransferObjects.*;

public class Asker {
    private UserInput userInput;
    private Validator validator;
    public Asker(UserInput userInput, Validator validator){
        this.userInput = userInput;
        this.validator = validator;
    }
    private CommandTransferObject askCommand() throws EndOfFileException {
        CommandTransferObject result = new CommandTransferObject();
        String[] command = userInput.askCommand();
        result.setCommand(command[0]);
        result.setArgument(command[1]);
        return result;
    }
    private String askPropertyString(String request) throws EndOfFileException{
        return userInput.askPropertyString(request);
    }
    private Integer askPropertyInt(String request) throws EndOfFileException{
        return userInput.askPropertyInt(request);
    }
    private CoordinatesTransferObject askCoordinates() throws EndOfFileException{
        CoordinatesTransferObject coordinates = new CoordinatesTransferObject();
        coordinates.setX(Double.valueOf(askPropertyInt("Enter x")));
        coordinates.setY(Long.valueOf(askPropertyInt("Enter y")));
        return coordinates;
    }
    private HouseTransferObject askHouse() throws EndOfFileException{
        HouseTransferObject house = new HouseTransferObject();
        house.setName(askPropertyString("Enter name of house"));
        house.setYear(askPropertyInt("Enter year"));
        house.setNumberOfFloors(askPropertyInt("Enter number of floors"));
        house.setNumberOfFlatsOnFloor(Long.valueOf(askPropertyInt("Enter number of flats on floor")));
        house.setNumberOfLifts(askPropertyInt("Enter number of lifts"));
        return house;
    }
    private FlatTransferObject askFlat() throws EndOfFileException{
        FlatTransferObject flat = new FlatTransferObject();
        flat.setName(askPropertyString("Enter name of flat"));
        flat.setCoordinates(askCoordinates());
        flat.setArea(Long.valueOf(askPropertyInt("Enter area")));
        flat.setNumberOfRooms(askPropertyInt("Enter number of rooms"));
        flat.setPrice(Double.valueOf(askPropertyInt("Enter price")));
        flat.setLivingSpace(askPropertyInt("Enter living space"));
        boolean valid = false;
        while(!valid){
            try{
                flat.setTransport(TransportTransferObject.valueOf(askPropertyString("Enter transport")));
                valid = true;
            }catch (IllegalArgumentException e){
                continue;
            }
        }
        flat.setHouse(askHouse());
        return flat;
    }
    public CommandTransferObject askValidatedCommand() throws EndOfFileException{
        CommandTransferObject command;
        do{
            command = askCommand();
            if(!validator.validate(command)){
                UserOutput.println("Incorrect command");
                if(!userInput.isInteractive()){
                    return null;
                }
            }
        } while(!validator.validate(command));
        return command;
    }

    public FlatTransferObject askValidatedFlat() throws EndOfFileException{
        FlatTransferObject flat;
        do{
            flat = askFlat();
            if(!validator.validate(flat)){
                UserOutput.println("Incorrect flat data");
                if(!userInput.isInteractive()){
                    return null;
                }
            }
        } while(!validator.validate(flat));
        return flat;
    }

    public HouseTransferObject askValidatedHouse() throws EndOfFileException{
        HouseTransferObject house;
        do{
            house = askHouse();
            if(!validator.validate(house)){
                UserOutput.println("Incorrect house data");
                if(!userInput.isInteractive()){
                    return null;
                }
            }
        } while(!validator.validate(house));
        return house;
    }

    public CoordinatesTransferObject askValidatedCoordinates() throws EndOfFileException{
        CoordinatesTransferObject coordinates;
        do{
            coordinates = askCoordinates();
            if(!validator.validate(coordinates)){
                UserOutput.println("Incorrect coordinates");
                if(!userInput.isInteractive()){
                    return null;
                }
            }
        } while(!validator.validate(coordinates));
        return coordinates;
    }

    public String[] signIn() throws EndOfFileException {
        UserOutput.println("Enter your login");
        String login;
        String password;
        login = userInput.getLine();
        UserOutput.println("Enter your password");
        password = userInput.getLine();
        String[] result = {login,password};
        return result;
    }

    public String[] signUp() throws EndOfFileException {
        UserOutput.println("Enter your login");
        String login;
        String password1; String password2;
        login = userInput.getLine();
        do{
            UserOutput.println("Enter your password");
            password1 = userInput.getLine();
            UserOutput.println("Repeat your password");
            password2 = userInput.getLine();
            if(!password1.equals(password2)) UserOutput.println("Passwords don't match");
        } while (!password1.equals(password2));
        String[] result = {login,password1};
        return result;

    }
    public boolean isSignUp() throws EndOfFileException {
        UserOutput.println("Do you want to sign up or sign in?(u/i)");
        return userInput.getLine().equals("u");
    }
    public boolean isInteractive(){ return this.userInput.isInteractive(); }
}
