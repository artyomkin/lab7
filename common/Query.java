package common;

import common.dataTransferObjects.CommandTransferObject;
import common.dataTransferObjects.CoordinatesTransferObject;
import common.dataTransferObjects.FlatTransferObject;
import common.dataTransferObjects.HouseTransferObject;

import java.io.Serializable;

import static common.Stage.BEGINNING;

public class Query implements Serializable {

    private static final long serialVersionUID = 1L;
    private FlatTransferObject DTOFlat;
    private HouseTransferObject DTOHouse;
    private CoordinatesTransferObject DTOCoordinates;
    private CommandTransferObject DTOCommand;
    private Stage stage;
    private String login;
    private String password;
    private boolean signIn;
    private boolean signUp;

    public Query(){
        this.stage = BEGINNING;
    }

    public CommandTransferObject getDTOCommand() {
        return DTOCommand;
    }

    public Stage getStage() { return stage; }

    public String getLogin() {return login;}

    public String getPassword() {return password;}

    public boolean isSignIn() {return signIn;}

    public boolean isSignUp() {return signUp;}

    public CoordinatesTransferObject getDTOCoordinates() {
        return DTOCoordinates;
    }

    public FlatTransferObject getDTOFlat() {
        return DTOFlat;
    }

    public HouseTransferObject getDTOHouse() {
        return DTOHouse;
    }

    public Query setDTOCommand(CommandTransferObject DTOCommand) {
        this.DTOCommand = DTOCommand;
        return this;
    }

    public Query setStage(Stage stage){
        this.stage = stage;
        return this;
    }

    public Query setDTOCoordinates(CoordinatesTransferObject DTOCoordinates) {
        this.DTOCoordinates = DTOCoordinates;
        return this;
    }

    public Query setLogin(String login){
        this.login = login;
        return this;
    }

    public Query setPassword(String password){
        this.password = password;
        return this;
    }

    public Query setDTOFlat(FlatTransferObject DTOFlat) {
        this.DTOFlat = DTOFlat;
        return this;
    }

    public Query setDTOHouse(HouseTransferObject DTOHouse) {
        this.DTOHouse = DTOHouse;
        return this;
    }

    public Query setSignIn(boolean signIn){
        this.signIn = signIn;
        return this;
    }

    public Query setSignUp(boolean signUp){
        this.signUp = signUp;
        return this;
    }

}
