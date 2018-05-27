package ai.assignment.kmeans.calculator;

import ai.assignment.kmeans.calculator.data.Point;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class AveragePointCalculator {

    public static Point averagePoint(List<Point> points) {
        int amountOfPoints = points.size();
        if (amountOfPoints == 0)throw new RuntimeException("Prototype didn't get any point");
        Point referencePoint = points.get(0);

        BigDecimal[] averageCoordinates = new BigDecimal[referencePoint.amountOfDimensions()];

        points.forEach(point -> {
            for (int coordinate = 0; coordinate < point.amountOfDimensions(); coordinate++) {
                if (averageCoordinates[coordinate] == null) {
                    averageCoordinates[coordinate] = point.getCoordinates()[coordinate];
                }else {
                    averageCoordinates[coordinate] = averageCoordinates[coordinate].add(point.getCoordinates()[coordinate]);
                }
            }
        });

        for (int coordinate = 0; coordinate < averageCoordinates.length; coordinate++) {
            averageCoordinates[coordinate] = averageCoordinates[coordinate]
                    .divide(new BigDecimal(amountOfPoints), MathContext.DECIMAL32);
        }

        return new Point(averageCoordinates);
    }
}
