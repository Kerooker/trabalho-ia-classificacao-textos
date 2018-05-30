package ai.assignment.kmeans;

import ai.assignment.kmeans.calculator.AveragePointCalculator;
import ai.assignment.kmeans.calculator.distance.DistanceCalculator;
import ai.assignment.kmeans.data.Dataset;
import ai.assignment.kmeans.data.Point;
import ai.assignment.kmeans.data.Prototype;
import java.math.BigDecimal;
import java.util.List;

public abstract class KMeans {

    protected final Dataset dataSet;

    protected int k; //Number of clusters
    private int maxIterations;
    private BigDecimal tolerance; //Amount to stop algorithm if difference from one interaction to another is lesser than
    protected DistanceCalculator distanceCalculator;  //What kind of measurement should be used

    protected Prototype[] centroids;


    public KMeans(Point[] dataSet,
                        int k,
                        int maxIterations,
                        BigDecimal tolerance,
                        DistanceCalculator distanceCalculator) {
        this.dataSet = new Dataset(dataSet, distanceCalculator);
        this.k = k;
        this.maxIterations = maxIterations;
        this.tolerance = tolerance;
        this.distanceCalculator = distanceCalculator;
    }

    public String result() {
        return dataSet.mapResults();
    }

    public BigDecimal silhouette() {
        System.out.println("Calculating silhouette");
        BigDecimal result = dataSet.averageSilhouette();
        System.out.println("Finished.");
        return result;
    }

    public void executeKmeans() {
        initializeCentroids();
        executeIterations();
    }


    protected abstract void initializeCentroids();

    private void executeIterations() {
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            System.out.println("Starting iteration " + iteration);

            adjustPointPrototypesToNearestCentroid();
            BigDecimal distanceMoved = adjustCentroidsToAverageOfThePoints();

            System.out.println("Distance moved for all centroids: " + distanceMoved);
            if (distanceMoved.compareTo(tolerance) < 0) {
                //Stopping after tolerance is reached
                break;
            }

        }
    }

    private void adjustPointPrototypesToNearestCentroid() {
        System.out.println("Adjusting all point to the nearest centroid");
        dataSet.updatePoints(centroids);
        System.out.println("Points adjusted.");
    }

    private BigDecimal adjustCentroidsToAverageOfThePoints() {

        System.out.println("Adjusting centroids to average of the points");
        BigDecimal totalDistanceMoved = BigDecimal.ZERO;
        for (Prototype p : centroids) {
            List<Point> prototypePoints = findAllPointsForPrototype(p);

            Point averagePoint = AveragePointCalculator.averagePoint(prototypePoints);

            totalDistanceMoved = totalDistanceMoved.add(averagePoint.distanceTo(p, distanceCalculator));

            p.setCoordinates(averagePoint.cloneCoordinates());

        }
        System.out.println("Centroids adjusted");
        return totalDistanceMoved;
    }

    private List<Point> findAllPointsForPrototype(Prototype p) {
        return dataSet.pointsForPrototype(p);
    }

}
