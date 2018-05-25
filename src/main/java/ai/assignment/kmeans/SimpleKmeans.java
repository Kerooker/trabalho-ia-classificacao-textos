package ai.assignment.kmeans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SimpleKmeans {

    private ConcurrentHashMap<Point, Prototype> dataSet = new ConcurrentHashMap<>();

    private int k; //Number of clusters
    private int maxIterations;
    private BigDecimal tolerance; //Amount to stop algorithm if difference from one interaction to another is lesser than

    private Prototype[] centroids;


    public SimpleKmeans(Point[] dataSet, int k, int maxIterations, BigDecimal tolerance) {
        for (Point p : dataSet){
            this.dataSet.put(p, Prototype.NONE);
        }

        this.k = k;
        this.maxIterations = maxIterations;
        this.tolerance = tolerance;
    }

    public void executeSimpleKmeans() {
        initializeCentroids();
        executeIterations();
        //todo tratar resultados
    }

    private void initializeCentroids() {
        centroids = new Prototype[k];

        for (int i = 0; i < k; i++) {
            initializePrototypeRandomly(i);
        }
    }

    private void executeIterations() {
        for (int iteration = 0; iteration < maxIterations; iteration++) {

            adjustPointPrototypesToNearestCentroid();
            BigDecimal distanceMoved = adjustCentroidsToAverageOfThePoints();

            if (distanceMoved.compareTo(tolerance) < 0) {
                //Stopping after tolerance is reached
                break;
            }

        }
    }

    private void adjustPointPrototypesToNearestCentroid() {
        dataSet.keySet().parallelStream().forEach(point -> {
            Prototype nearestPrototype = point.findNearestPrototype(centroids);
            dataSet.put(point, nearestPrototype);
        });
    }

    private BigDecimal adjustCentroidsToAverageOfThePoints() {

        BigDecimal totalDistanceMoved = BigDecimal.ZERO;
        for (Prototype p : centroids) {
            List<Point> prototypePoints = findAllPointsForPrototype(p);

            Point averagePoint = Point.averagePoint(prototypePoints);

            totalDistanceMoved = totalDistanceMoved.add(averagePoint.distanceTo(p));

            p.setCoordinates(averagePoint.coordinates);

        }
        return totalDistanceMoved;
    }

    private List<Point> findAllPointsForPrototype(Prototype p) {
        return dataSet.keySet().stream()
                .filter(it -> dataSet.get(it) == p)
                .collect(Collectors.toList());
    }

    private void initializePrototypeRandomly(int prototypeToInitialize) {
        Point randomPoint = randomPointFromData();
        centroids[prototypeToInitialize] = new Prototype(randomPoint);
    }

    private Point randomPointFromData() {
        int amountOfPoints = dataSet.size();
        int randomPointIndex = ThreadLocalRandom.current().nextInt(amountOfPoints);

        return new ArrayList<>(dataSet.keySet()).get(randomPointIndex);
    }
}
