package common.dataTransferObjects;

import java.io.Serializable;

public class HouseTransferObject implements Serializable {
    private String name;
    private Integer year;
    private Integer numberOfFloors;
    private long numberOfFlatsOnFloor;
    private Integer numberOfLifts;
    public HouseTransferObject(){
        name = "";
        year = 0;
        numberOfFloors = 0;
        numberOfFlatsOnFloor = 0;
        numberOfLifts = 0;
    }

    public long getNumberOfFlatsOnFloor() {
        return numberOfFlatsOnFloor;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getNumberOfLifts() {
        return numberOfLifts;
    }

    public Integer getNumberOfFloors() {
        return numberOfFloors;
    }

    public String getName() {
        return name;
    }

    public HouseTransferObject setYear(Integer year) {
        this.year = year;
        return this;
    }

    public HouseTransferObject setNumberOfLifts(Integer numberOfLifts) {
        this.numberOfLifts = numberOfLifts;
        return this;
    }

    public HouseTransferObject setNumberOfFloors(Integer numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
        return this;
    }

    public HouseTransferObject setNumberOfFlatsOnFloor(long numberOfFlatsOnFloor) {
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
        return this;
    }

    public HouseTransferObject setName(String name) {
        this.name = name;
        return this;
    }
}
