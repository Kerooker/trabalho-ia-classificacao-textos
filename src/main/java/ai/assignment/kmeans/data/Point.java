package ai.assignment.kmeans.data;

import ai.assignment.kmeans.calculator.distance.DistanceCalculator;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Point {

    private BigDecimal[] coordinates;
    private int ownerText = -1;

    public Point(BigDecimal[] coordinates, int ownerText) {
        this.coordinates = coordinates;
        this.ownerText = ownerText;
    }

    public Point(BigDecimal[] coordinates) {
        this.coordinates = coordinates;
    }

    public BigDecimal distanceTo(Point otherPoint, DistanceCalculator calculator) {
        return calculator.calculateDistance(this, otherPoint);
    }

    public BigDecimal averageDistanceTo(List<Point> myGroup, DistanceCalculator distanceCalculator) {
        AtomicReference<BigDecimal> currentAverageDistance = new AtomicReference<>(BigDecimal.ZERO);
        myGroup.parallelStream().forEach(point -> {
            currentAverageDistance.set(currentAverageDistance.get().add(distanceTo(point, distanceCalculator)));

        });
        if (myGroup.size() == 0) {
            return new BigDecimal(Double.MAX_VALUE);
        }
        BigDecimal averageDistance = currentAverageDistance.get()
                .divide(new BigDecimal(myGroup.size()), MathContext.DECIMAL32);

        return averageDistance;
    }

    public int amountOfDimensions() {
        return coordinates.length;
    }

    public Prototype findNearestPrototype(Prototype[] centroids, DistanceCalculator distanceCalculator) {
        Prototype currentMin = null;

        for (Prototype p : centroids) {
            if (p == null)continue;
            if(currentMin == null){
                currentMin = p;
                continue;
            }
            BigDecimal currentMinDistance = distanceTo(currentMin, distanceCalculator);
            BigDecimal possibleMinDistance = distanceTo(p, distanceCalculator);

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("coordinates", coordinates)
                .append("ownerText", ownerText)
                .toString();
    }

    public BigDecimal[] getCoordinates() {
        return coordinates;
    }

    public BigDecimal[] cloneCoordinates() {
        List<BigDecimal> coordinates = new ArrayList<>(Arrays.asList(this.coordinates));
        return coordinates.toArray(new BigDecimal[0]);
    }

    public void setCoordinates(BigDecimal[] coordinates) {
        this.coordinates = coordinates;
    }

    public int getOwnerText() {
        return ownerText;
    }

    public void setOwnerText(int ownerText) {
        this.ownerText = ownerText;
    }

}
