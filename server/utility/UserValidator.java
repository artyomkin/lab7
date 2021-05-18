package server.utility;

import server.Server;
import server.commandHandler.utility.DataBaseManager;
import server.commandHandler.utility.ServerOutput;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserValidator {
    private Connection connection;
    private DataBaseManager dataBaseManager;
    private Encryptor encryptor;

    public UserValidator(Connection connection, DataBaseManager dataBaseManager) throws NoSuchAlgorithmException {
        this.connection = connection;
        this.dataBaseManager = dataBaseManager;
        this.encryptor = Encryptor.getINSTANCE();
    }

    public boolean validate(String login, String password){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE(login = ?)");
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) return false;
            String passwordHash = resultSet.getString("password");
            String salt = resultSet.getString("salt");
            return passwordHash.equals(encryptor.encrypt(password+salt));
        } catch (SQLException e){
            ServerOutput.warning("Failed to validate user");
            return false;
        }

    }


}
