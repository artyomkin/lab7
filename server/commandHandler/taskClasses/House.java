package server.commandHandler.taskClasses;

import common.dataTransferObjects.HouseTransferObject;

import java.util.Objects;
/**
 * Class of house of flat
 * **/
public class House {
    private String name;
    private Integer year;
    private Integer numberOfFloors;
    private long numberOfFlatsOnFloor;
    private Integer numberOfLifts;

    public House() {
        this.name = "unknown";
        this.year = 1;
        this.numberOfFloors = 1;
        this.numberOfFlatsOnFloor = 1;
        this.numberOfLifts = 1;
    }
    public House(String name, Integer year, Integer numberOfFloors, long numberOfFlatsOnFloor, Integer numberOfLifts){
        this.name = name;
        this.year = year;
        this.numberOfFloors = numberOfFloors;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
        this.numberOfLifts = numberOfLifts;
    }
    public House(HouseTransferObject house){
        this.name = house.getName();
        this.numberOfFlatsOnFloor = house.getNumberOfFlatsOnFloor();
        this.numberOfFloors = house.getNumberOfFloors();
        this.numberOfLifts = house.getNumberOfLifts();
        this.year = house.getYear();
    }
    /**
     * @return name
     * **/
    public String getName() {
        return name;
    }
    /**
     * @return number of floors
     * **/
    public Integer getNumberOfFloors() {
        return numberOfFloors;
    }
    /**
     * @return number of lifts
     * **/
    public Integer getNumberOfLifts() {
        return numberOfLifts;
    }
    /**
     * @return years of house
     * **/
    public Integer getYear() {
        return year;
    }
    /**
     * @return number of flats on floor
     * **/
    public long getNumberOfFlatsOnFloor() {
        return numberOfFlatsOnFloor;
    }

    @Override
    public String toString(){
        return  "Name of house: " + this.name + '\n' +
                "Year of house: " + this.year + '\n' +
                "Number of floors: " + this.numberOfFloors + '\n' +
                "Number of floors: " + this.numberOfFlatsOnFloor + '\n' +
                "Number of lifts: " + this.numberOfLifts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        House house = (House) o;
        return numberOfFlatsOnFloor == house.numberOfFlatsOnFloor &&
                Objects.equals(name, house.name) &&
                Objects.equals(year, house.year) &&
                numberOfFloors.equals(house.numberOfFloors) &&
                numberOfLifts.equals(house.numberOfLifts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year, numberOfFloors, numberOfFlatsOnFloor, numberOfLifts);
    }
}

