package server.utility;

import java.util.concurrent.Callable;

public class ValidateUserTask implements Callable<Boolean> {
    private String login;
    private String password;
    private UserAuthorizer userAuthorizer;

    public ValidateUserTask(String login, String password, UserAuthorizer userAuthorizer){
        this.login = login;
        this.password = password;
        this.userAuthorizer = userAuthorizer;
    }

    public Boolean call(){
        return userAuthorizer.validate(login,password).equals(UserCount.ONE);
    }
}
