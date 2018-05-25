package ai.assignment.kmeans;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class Point {

    protected BigDecimal[] coordinates;
    protected int ownerText = -1;

    public Point(BigDecimal[] coordinates, int ownerText) {
        this.coordinates = coordinates;
        this.ownerText = ownerText;
    }

    public Point(BigDecimal[] coordinates) {
        this.coordinates = coordinates;
    }

    public abstract BigDecimal distanceTo(Point otherPoint);

    protected int amountOfDimensions() {
        return coordinates.length;
    }

    public Prototype findNearestPrototype(Prototype[] centroids) {
        Prototype currentMin = null;

        for (Prototype p : centroids) {
            if(currentMin == null){
                currentMin = p;
                continue;
            }
            BigDecimal currentMinDistance = distanceTo(currentMin);
            BigDecimal possibleMinDistance = distanceTo(p);

            if (min(currentMinDistance, possibleMinDistance).equals(possibleMinDistance)) {
                currentMin = p;
            }

        }
        return currentMin;
    }

    private BigDecimal min(BigDecimal one, BigDecimal two) {
        return one.min(two);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;

        Point point = (Point) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(coordinates, point.coordinates);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coordinates);
    }

    public static Point averagePoint(List<Point> points) {
        int amountOfPoints = points.size();
        if (amountOfPoints == 0)throw new RuntimeException("At least one point should be here");
        Point referencePoint = points.get(0);

        BigDecimal[] averageCoordinates = new BigDecimal[referencePoint.amountOfDimensions()];

        points.forEach(point -> {
            for (int coordinate = 0; coordinate < point.amountOfDimensions(); coordinate++) {
                if (averageCoordinates[coordinate] == null) {
                    averageCoordinates[coordinate] = point.coordinates[coordinate];
                }else {
                    averageCoordinates[coordinate] = averageCoordinates[coordinate].add(point.coordinates[coordinate]);
                }
            }
        });

        for (int coordinate = 0; coordinate < averageCoordinates.length; coordinate++) {
            averageCoordinates[coordinate] = averageCoordinates[coordinate]
                    .divide(new BigDecimal(amountOfPoints), MathContext.DECIMAL32);
        }

        if (referencePoint instanceof EuclidianPoint) {
            return new EuclidianPoint(averageCoordinates, -1);
        }else {
            //TODO
            return null;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("coordinates", coordinates)
                .append("ownerText", ownerText)
                .toString();
    }
}
