package ai.assignment.kmeans.data;

import java.math.BigDecimal;

public class Prototype extends Point {

    public static final Prototype NONE = new Prototype((BigDecimal[]) null);

    public int prototypeIndex;

    private Prototype(BigDecimal[] coordinates) {
        super(coordinates);
        this.prototypeIndex = -1;
    }

    public Prototype(Point point, int prototypeIndex) {
        super(point.cloneCoordinates());
        this.prototypeIndex = prototypeIndex;
    }

}
