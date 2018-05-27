import ai.assignment.kmeans.SimpleKmeans;
import ai.assignment.kmeans.calculator.data.Point;
import ai.assignment.kmeans.calculator.distance.DistanceCalculator;
import java.io.IOException;
import java.math.BigDecimal;

public class Playground {

    public static void main(String[] args) throws IOException {

        Point p = new Point(new BigDecimal[]{new BigDecimal("100"), new BigDecimal("100")}, 1);
        Point p1 = new Point(new BigDecimal[]{new BigDecimal("105"), new BigDecimal("105")}, 1);
        Point p2 = new Point(new BigDecimal[]{new BigDecimal("5"), new BigDecimal("5")}, 2);
        Point p3 = new Point(new BigDecimal[]{new BigDecimal("6"), new BigDecimal("6")}, 3);
        Point p4 = new Point(new BigDecimal[]{new BigDecimal("7"), new BigDecimal("7")},4 );

        SimpleKmeans kmeans = new SimpleKmeans(new Point[]{p, p1, p2, p3, p4},
                2,
                100,
                new BigDecimal("0.1"),
                DistanceCalculator.EUCLIDIAN);
        kmeans.executeSimpleKmeans();

        System.out.println(kmeans.result());
        System.out.println(kmeans.silhouette());

    }
}
