package ai.assignment.kmeans;

import java.math.BigDecimal;

public class Prototype extends Point {

    private BigDecimal[] coordinates;

    public Prototype(BigDecimal[] coordinates) {
        super(coordinates);
    }

    public Prototype(Point point) {
        super(point.coordinates);
    }

    public BigDecimal[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(BigDecimal[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public BigDecimal distanceTo(Point otherPoint) {
        throw new UnsupportedOperationException("A prototype doesn't know what kind of distance it should measure.");
    }
}
