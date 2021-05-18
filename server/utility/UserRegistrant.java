package server.utility;

import server.commandHandler.utility.DataBaseManager;
import server.commandHandler.utility.RandomStr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRegistrant {
    private Connection connection;
    private DataBaseManager dataBaseManager;
    private Encryptor encryptor;

    public UserRegistrant (Connection connection, DataBaseManager dataBaseManager, Encryptor encryptor){
        this.connection = connection;
        this.dataBaseManager = dataBaseManager;
        this.encryptor = encryptor;
    }

    public boolean register(String login, String password){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users VALUES(?,?,?)");
            preparedStatement.setString(1,login);
            String salt = RandomStr.randomStr(10);
            preparedStatement.setString(2,encryptor.encrypt(password+salt));
            preparedStatement.setString(3,salt);
            return preparedStatement.executeUpdate()!=0;
        } catch (SQLException e){
            return false;
        }
    }
}
