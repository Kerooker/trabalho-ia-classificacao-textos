package ai.assignment.kmeans;

import ai.assignment.kmeans.calculator.data.Point;
import ai.assignment.kmeans.calculator.data.Prototype;
import ai.assignment.kmeans.calculator.distance.DistanceCalculator;
import java.math.BigDecimal;

public class SimpleKmeans extends KMeans {

    public SimpleKmeans(Point[] dataSet, int k,
                        int maxIterations, BigDecimal tolerance,
                        DistanceCalculator distanceCalculator) {
        super(dataSet, k, maxIterations, tolerance, distanceCalculator);
    }

    @Override
    protected void initializeCentroids() {
        centroids = new Prototype[k];

        for (int i = 0; i < k; i++) {
            System.out.println("Initializing prototype " + i);
            initializePrototypeRandomly(i);
        }
    }

    private void initializePrototypeRandomly(int prototypeToInitialize) {
        Point randomPoint = dataSet.randomPoint();
        centroids[prototypeToInitialize] = new Prototype(randomPoint, prototypeToInitialize);
    }
}
