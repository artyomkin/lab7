package server.commandHandler.taskClasses;
import common.dataTransferObjects.FlatTransferObject;
import server.commandHandler.utility.RandomStr;

import java.sql.Date;
import java.time.LocalDate;
import java.lang.Comparable;
import java.util.Objects;
import java.util.Random;

/**
 * Objects of this class are stored in collection
 * **/
public class Flat implements Comparable<Flat> {

    private static int idCount;
    static {
        idCount = 0;
    }
    private int id;
    private String name;
    private Coordinates coordinates;
    private Date creationDate;
    private long area;
    private Integer numberOfRooms;
    private double price;
    private Integer livingSpace;
    private Transport transport;
    private House house;
    private String creator;

    private static final long MAX_AREA = 897L;
    private static final Integer MAX_NUMBER_OF_ROOMS = 18;
    public Flat() {
        this.id = idCount;
        updateIdCount();

        this.name = RandomStr.randomStr(10);
        this.coordinates = new Coordinates();
        this.creationDate = new Date(System.currentTimeMillis());

        Random rand = new Random();
        this.area = (long)1 + rand.nextInt((int)MAX_AREA);
        this.numberOfRooms = 1 + rand.nextInt(MAX_NUMBER_OF_ROOMS);
        this.price = 1 + rand.nextInt(1000);
        this.livingSpace = 1 + rand.nextInt(1000);
        this.transport = null;
        this.house = null;
    }

    public Flat(String name,
                Coordinates coordinates,
                long area,
                Integer numberOfRooms,
                double price,
                Integer livingSpace,
                Transport transport,
                House house,
                String creator){
        this.id = idCount;
        updateIdCount();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date(System.currentTimeMillis());
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.price = price;
        this.livingSpace = livingSpace;
        this.transport = transport;
        this.house = house;
        this.creator = creator;
    }

    public Flat(FlatTransferObject flat){
        this.area = flat.getArea();
        this.coordinates = new Coordinates(flat.getCoordinates());
        this.creationDate = new Date(System.currentTimeMillis());
        this.house = new House(flat.getHouse());
        this.livingSpace = flat.getLivingSpace();
        this.numberOfRooms = flat.getNumberOfRooms();
        this.name = flat.getName();
        this.price = flat.getPrice();
        this.transport = Transport.valueOf(flat.getTransport().toString());
        this.creator = flat.getCreator();
    }

    @Override
    public String toString(){
        return  "ID: " + this.id + '\n' +
                "Name : " + this.name + '\n' +
                "Coordinates: \n" + this.coordinates.toString() + '\n' +
                "Created: " + this.creationDate.toString() + '\n' +
                "Area: " + this.area + '\n' +
                "Number of rooms: " + this.numberOfRooms + '\n' +
                "Price: " + this.price + '\n' +
                "Living space: " + this.livingSpace + '\n' +
                "Transport: " + this.transport.toString() + '\n' +
                "House: \n" + this.house.toString()+'\n'+
                "Creator: \n" + this.creator + '\n';
    }
    public boolean setId(int id){
        this.id = id;
        if (this.id>idCount){
                idCount = id;
        }return true;
    }

    public Flat setCreator(String creator){
        this.creator = creator;
        return this;
    }

    /**
     * @return coordinates
     * **/
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getDate(){return this.creationDate;}
    /**
     * @return house
     * **/
    public House getHouse() {
        return house;
    }
    /**
     * @return name
     * **/
    public String getName() {
        return this.name;
    }
    /**
     * @return id
     * **/
    public int getID() {
        return this.id;
    }
    /**
     * @return area
     * **/
    public long getArea() {
        return area;
    }
    /**
     * @return number of rooms
     * **/
    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }
    /**
     * @return price
     * **/
    public double getPrice() {
        return price;
    }
    /**
     * @return living space
     * **/
    public Integer getLivingSpace() {
        return livingSpace;
    }

    public String getCreator(){ return creator;}
    /**
     * @return transport
     * **/
    public Transport getTransport() {
        return transport;
    }

    /**
     * Compares this flat to specified one
     * @return negative value if this flat is less than specified one, zero if they are equal and positive value in other cases
     * **/
    public int compareTo(Flat flat) {
        return this.getName().length()-flat.getName().length();
    }
    /**
     * Increments ID count when another instance of flat was created
     * **/
    private static void updateIdCount() {
        idCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flat flat = (Flat) o;
        return id == flat.id &&
                area == flat.area &&
                Double.compare(flat.price, price) == 0 &&
                name.equals(flat.name) &&
                coordinates.equals(flat.coordinates) &&
                creationDate.equals(flat.creationDate) &&
                numberOfRooms.equals(flat.numberOfRooms) &&
                livingSpace.equals(flat.livingSpace) &&
                transport == flat.transport &&
                Objects.equals(house, flat.house);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, area, numberOfRooms, price, livingSpace, transport, house);
    }
}
