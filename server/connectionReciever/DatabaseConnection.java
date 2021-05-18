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
    private static String URL = "jdbc:postgresql://pg/studs";
    private static String USER = "s312394";
    private static String PASSWORD = "izq929";


    public static Connection getConnection(){
        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
            ServerOutput.info("Connection with database set");
            return connection;
        } catch (SQLException throwables) {
            ServerOutput.warning("Connection with data base error");
            return null;
        } catch (ClassNotFoundException e){
            ServerOutput.warning("Connection with data base error");
            return null;
        } /*catch (FileNotFoundException e){
            ServerOutput.warning("File bd.properties not found");
            return null;
        } catch (IOException e){
            ServerOutput.warning("IOE");
            return null;
        }*/
    }
}
