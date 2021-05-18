package server.commandHandler.utility;

import server.commandHandler.taskClasses.Flat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Manages the main collection
 * **/
public class CollectionManager {

    private Map<Integer, Flat> collection;
    private Date date;
    private int length;
    private DataBaseManager dataBaseManager;
    public CollectionManager(DataBaseManager dataBaseManager) throws SQLException {
        this.dataBaseManager = dataBaseManager;
        collection = Collections.synchronizedMap(dataBaseManager.loadCollection());
        date = new Date();
        length = collection.size();
    }
    /**
     * Inserts specified element with specified key into collection
     * @return exit status of insertion
     * @param flat
     * @param key
     * **/
    public boolean insert(Integer key,Flat flat) {
        if(!dataBaseManager.insert(key,flat)) return false;

        collection.put(key,flat);
        flat.setId(key);
        updateAfterInsert();
        return true;
    }
    public boolean contains(Integer key){
        return collection.keySet().contains(key);
    }
    /**
     * Removes the element by its key
     * @return exit status of removal
     * @param key
     * **/
    public boolean remove(Integer key){
        if(!dataBaseManager.remove(key)) return false;

        synchronized (collection){
            if (collection.isEmpty() || !collection.containsKey(key)){
                return false;
            }
            collection.remove(key);
        }
        updateAfterRemove();
        return true;

    }
    /**
     * Replaces replaceable flat with substitutable flat in collection
     * @return exit status of replacing
     * @param ID
     * @param substitutable
     * **/
    public boolean replace(Integer ID, Flat substitutable){
        return dataBaseManager.replace(ID,substitutable) && (collection.replace(ID,substitutable)==null);
    }
    /**
     * Returns flat by its key from collection
     * @return flat
     * @param key
     * **/
    public Flat getElementByKey(Integer key){
        return collection.get(key);
    }

    public Class getCollectionClass(){
        return collection.getClass();
    }
    /**
     * Returns the date when collection was created
     * @return creation date
     * **/
    public Date getInitializationDate(){ return date; };
    /**
     * Returns the lenght of collection
     * @return length
     * **/
    public int getLength(){ return length; }
    public void updateAfterRemove(){
        this.length--;
    }
    public void updateAfterInsert(){this.length++;}
    public Stream<Flat> getStream(){
        List<Flat> flats = this.collection.values().stream()
                .collect(Collectors.toList());
        flats.sort(Flat::compareTo);
        return flats.stream();
    };
}
