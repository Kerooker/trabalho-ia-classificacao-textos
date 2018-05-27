package ai.assignment.kmeans.calculator.distance;

import ai.assignment.kmeans.calculator.data.Point;
import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;

public enum DistanceCalculator {

    EUCLIDIAN {

        @Override
        public BigDecimal calculateDistance(Point p1, Point p2) {
            if (p1.amountOfDimensions() != p2.amountOfDimensions()) {
                throw new IllegalArgumentException("Both points should have the same dimensions");
            }

            BigDecimal squaredDistance = BigDecimal.ZERO;

            for (int i = 0; i < p1.amountOfDimensions(); i++) {
                BigDecimal myCoordinate = p1.getCoordinates()[i];
                BigDecimal otherCoordinate = p2.getCoordinates()[i];

                BigDecimal squared = calculateSquaredDistance(myCoordinate, otherCoordinate);
                squaredDistance = squaredDistance.add(squared);
            }

            return BigDecimalMath.sqrt(squaredDistance, MathContext.DECIMAL32);
        }

        private BigDecimal calculateSquaredDistance(BigDecimal point1, BigDecimal point2) {
            BigDecimal subtraction = point1.subtract(point2);
            return subtraction.pow(2, MathContext.DECIMAL32);
        }
    },
    COSINE_SIMILARITY {

        @Override
        public BigDecimal calculateDistance(Point p1, Point p2) {
            if (p1.amountOfDimensions() != p2.amountOfDimensions()) {
                throw new IllegalArgumentException("Both points should have the same dimensions");
            }

            BigDecimal dotProduct = calculateDotProduct(p1, p2);
            BigDecimal p1Magnitude = calculateMagnitude(p1);
            BigDecimal p2Magnitude = calculateMagnitude(p2);

//            System.out.println("Applying formula " + dotProduct + " / " + p1Magnitude + " * " + p1Magnitude );

            return dotProduct.divide(p1Magnitude.multiply(p2Magnitude), MathContext.DECIMAL32);
        }

        private BigDecimal calculateDotProduct(Point p1, Point p2) {
            BigDecimal dotProduct = BigDecimal.ZERO;
            for (int i = 0; i < p1.amountOfDimensions(); i++) {
                BigDecimal myCoordinate = p1.getCoordinates()[i];
                BigDecimal otherCoordinate = p2.getCoordinates()[i];

                dotProduct = dotProduct.add(myCoordinate.multiply(otherCoordinate));
            }
            return dotProduct.round(MathContext.DECIMAL32);
        }

        private BigDecimal calculateMagnitude(Point p1) {
            //Magnitude == sqrt of dot product of the vector with itself.
            return BigDecimalMath.sqrt(calculateDotProduct(p1, p1), MathContext.DECIMAL32);
        }
    };

    public abstract BigDecimal calculateDistance(Point p1, Point p2);
}
