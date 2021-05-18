package common.dataTransferObjects;


import java.io.Serializable;

public class FlatTransferObject implements Serializable {
    private String name;
    private CoordinatesTransferObject coordinates;
    private long area;
    private Integer numberOfRooms;
    private double price;
    private Integer livingSpace;
    private TransportTransferObject transport;
    private HouseTransferObject house;
    private String creator;

    public FlatTransferObject(){
        name = "";
        coordinates = new CoordinatesTransferObject();
        area = 0;
        numberOfRooms = 0;
        price = 0;
        livingSpace = 0;
        transport = null;
        house = new HouseTransferObject();
    }

    public CoordinatesTransferObject getCoordinates() {
        return coordinates;
    }

    public long getArea() {
        return area;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public double getPrice() {
        return price;
    }

    public Integer getLivingSpace() {
        return livingSpace;
    }

    public TransportTransferObject getTransport() {
        return transport;
    }

    public HouseTransferObject getHouse() {
        return house;
    }

    public String getCreator() {return creator;}

    public FlatTransferObject setCreator(String creator){
        this.creator = creator;
        return this;
    }

    public FlatTransferObject setName(String name) {
        this.name = name;
        return this;
    }

    public FlatTransferObject setCoordinates(CoordinatesTransferObject coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public FlatTransferObject setArea(long area) {
        this.area = area;
        return this;
    }

    public FlatTransferObject setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
        return this;
    }

    public FlatTransferObject setPrice(double price) {
        this.price = price;
        return this;

    }

    public FlatTransferObject setLivingSpace(Integer livingSpace) {
        this.livingSpace = livingSpace;
        return this;
    }

    public FlatTransferObject setTransport(TransportTransferObject transport) {
        this.transport = transport;
        return this;
    }

    public FlatTransferObject setHouse(HouseTransferObject house) {
        this.house = house;
        return this;
    }

    public String getName() {
        return name;
    }
}
