package ai.assignment.kmeans;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;

public class EuclidianPoint extends Point {

    public EuclidianPoint(BigDecimal[] coordinates, int ownerText) {
        super(coordinates, ownerText);
    }

    @Override
    public BigDecimal distanceTo(Point otherPoint) {
        if (this.amountOfDimensions() != otherPoint.amountOfDimensions()) {
            throw new IllegalArgumentException("Both points should have the same dimensions");
        }

        BigDecimal squaredDistance = BigDecimal.ZERO;

        for (int i = 0; i < amountOfDimensions(); i++) {
            BigDecimal myCoordinate = coordinates[i];
            BigDecimal otherCoordinate = otherPoint.coordinates[i];

            BigDecimal squared = calculateSquaredDistance(myCoordinate, otherCoordinate);
            squaredDistance = squaredDistance.add(squared);
        }

        return BigDecimalMath.sqrt(squaredDistance, MathContext.DECIMAL32);
    }

    private BigDecimal calculateSquaredDistance(BigDecimal point1, BigDecimal point2) {
        BigDecimal subtraction = point1.subtract(point2);
        return subtraction.pow(2, MathContext.DECIMAL32);
    }

}
