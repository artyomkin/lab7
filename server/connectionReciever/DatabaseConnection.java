package server.connectionReciever;

import server.commandHandler.utility.ServerOutput;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static String DRIVER = "org.postgresql.Driver";
    private static String URL = "jdbc:postgresql://pg:5432/studs";
    //private static String URL = "jdbc:postgresql://localhost:5432/postgres";

    public static Connection getConnection(){
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("bd.properties"));
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(URL, properties);
            ServerOutput.info("Connection with database set");
            return connection;
        } catch (SQLException throwables) {
            ServerOutput.warning("Connection with data base error");
            return null;
        } catch (ClassNotFoundException e){
            ServerOutput.warning("Connection with data base error");
            return null;
        } catch (FileNotFoundException e){
            ServerOutput.warning("File bd.properties not found");
            return null;
        } catch (IOException e){
            ServerOutput.warning("IOE");
            return null;
        }
    }
}
