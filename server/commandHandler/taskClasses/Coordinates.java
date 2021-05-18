package server.commandHandler.taskClasses;

import common.dataTransferObjects.CoordinatesTransferObject;

import java.util.Objects;

/**
 * Coordinates of flat
 * **/
public class Coordinates {
    private Double x;
    private Long y;
    public Coordinates() {
        x = 0d; y = 0l;
    }
    public Coordinates(Double x, Long y){
        this.x = x; this.y = y;
    }
    public Coordinates(CoordinatesTransferObject coordinates){
        this.x = coordinates.getX();
        this.y = coordinates.getY();
    }
    /**
     * @return x coordinate
     * **/
    public Double getX() {
        return x;
    }
    /**
     * @return y coordinate
     * **/
    public Long getY() {
        return y;
    }

    @Override
    public String toString(){
        return "X: " + x.toString() + " Y: " + y.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x.equals(that.x) &&
                y.equals(that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
