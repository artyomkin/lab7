package server.utility;

import java.sql.Connection;
import java.util.concurrent.Callable;

public class ValidateUserTask implements Callable<Boolean> {
    private String login;
    private String password;
    private UserValidator userValidator;

    public ValidateUserTask(String login, String password, UserValidator userValidator){
        this.login = login;
        this.password = password;
        this.userValidator = userValidator;
    }

    public Boolean call(){
        return userValidator.validate(login,password);
    }
}
