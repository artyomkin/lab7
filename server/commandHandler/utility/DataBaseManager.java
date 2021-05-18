package server.commandHandler.utility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.commandHandler.taskClasses.Coordinates;
import server.commandHandler.taskClasses.Flat;
import server.commandHandler.taskClasses.House;
import server.commandHandler.taskClasses.Transport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads the JSON file and returns ready-made collection
 * **/
public class DataBaseManager {
    private Connection connection;
    private final String DB_COLLECTION_NAME = "collection";
    public DataBaseManager(Connection connection){
        this.connection = connection;
    }

    /**
     * Reads JSON and returns the collection
     * @return HashMap
     * **/
    public Map<Integer, Flat> loadCollection() throws SQLException{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM collection");
            Map<Integer,Flat> collection = new HashMap();
            while (resultSet.next()){
                Flat flat = new Flat(
                        resultSet.getString("name"),
                        new Coordinates(
                                resultSet.getDouble("x"),
                                resultSet.getLong("y")
                        ),
                        resultSet.getLong("area"),
                        resultSet.getInt("numberOfRooms"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("livingSpace"),
                        Transport.valueOf(resultSet.getString("transport")),
                        new House(
                                resultSet.getString("houseName"),
                                resultSet.getInt("year"),
                                resultSet.getInt("numberOfFloors"),
                                resultSet.getLong("numberOfFlatsOnFloor"),
                                resultSet.getInt("numberOfLifts")
                        ),
                        resultSet.getString("creator")
                );
                Integer id = resultSet.getInt("id");
                flat.setId(id);
                collection.put(id,flat);
            }
            return collection;
    }
    public boolean insert(Integer key, Flat flat){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + DB_COLLECTION_NAME + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            setFlat(preparedStatement,key,flat);
            return preparedStatement.executeUpdate()!=0;
        } catch (SQLException e){
            return false;
        }
    }

    public boolean remove(Integer key){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + DB_COLLECTION_NAME + " WHERE (id = ?)");
            preparedStatement.setInt(1,key);
            return preparedStatement.executeUpdate()!=0;
        } catch (SQLException e){
            return false;
        }
    }

    public boolean replace(Integer key, Flat flat){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + DB_COLLECTION_NAME + " SET (" +
                    "id = ?," +
                    "name = ?," +
                    "x = ?," +
                    "y = ?," +
                    "creationdate = ?," +
                    "area = ?," +
                    "numberofrooms = ?," +
                    "price = ?," +
                    "livingspace = ?," +
                    "transport = ?," +
                    "housename = ?," +
                    "year = ?," +
                    "numberoffloors = ?," +
                    "numberofflatsonfloor = ?," +
                    "numberoflifts = ?" +
                    "creator = ?" +
                    ") WHERE (id = "+key+")");
            setFlat(preparedStatement, key, flat);
            return preparedStatement.executeUpdate()!=0;
        } catch (SQLException e){
            return false;
        }
    }
    public boolean clear(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE * FROM " + DB_COLLECTION_NAME);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e){
            return false;
        }
    }
    private void setFlat(PreparedStatement preparedStatement, Integer key, Flat flat) throws SQLException {
        preparedStatement.setInt(1, key);
        preparedStatement.setString(2, flat.getName());
        preparedStatement.setDouble(3, flat.getCoordinates().getX());
        preparedStatement.setLong(4, flat.getCoordinates().getY());
        preparedStatement.setDate(5, flat.getDate());
        preparedStatement.setDouble(6, flat.getArea());
        preparedStatement.setLong(7, flat.getNumberOfRooms());
        preparedStatement.setDouble(8, flat.getPrice());
        preparedStatement.setInt(9, flat.getLivingSpace());
        preparedStatement.setString(10, flat.getTransport().toString());
        preparedStatement.setString(11, flat.getHouse().getName());
        preparedStatement.setInt(12, flat.getHouse().getYear());
        preparedStatement.setInt(13, flat.getHouse().getNumberOfFloors());
        preparedStatement.setLong(14, flat.getHouse().getNumberOfFlatsOnFloor());
        preparedStatement.setInt(15, flat.getHouse().getNumberOfLifts());
        preparedStatement.setString(16,flat.getCreator());
    }
}
