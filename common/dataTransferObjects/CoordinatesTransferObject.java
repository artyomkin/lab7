package common.dataTransferObjects;

import java.io.Serializable;

public class CoordinatesTransferObject  implements Serializable {
    private Double x;
    private Long y;

    public CoordinatesTransferObject(){
        x = 0d;
        y = 0l;
    }

    public Long getY() {
        return y;
    }

    public Double getX() {
        return x;
    }

    public CoordinatesTransferObject setY(Long y) {
        this.y = y;
        return this;
    }

    public CoordinatesTransferObject setX(Double x) {
        this.x = x;
        return this;
    }
}
