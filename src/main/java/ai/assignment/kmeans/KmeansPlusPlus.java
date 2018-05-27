package ai.assignment.kmeans;

import ai.assignment.kmeans.calculator.data.Point;
import ai.assignment.kmeans.calculator.data.Prototype;
import ai.assignment.kmeans.calculator.distance.DistanceCalculator;
import java.math.BigDecimal;

public class KmeansPlusPlus extends KMeans {


    public KmeansPlusPlus(Point[] dataSet,
                          int k, int maxIterations,
                          BigDecimal tolerance, DistanceCalculator distanceCalculator) {
        super(dataSet, k, maxIterations, tolerance, distanceCalculator);
    }

    @Override
    protected void initializeCentroids() {
        centroids = new Prototype[k];

        for(int centroid = 0; centroid < k; centroid++) {
            if (centroid == 0) {
                initializePrototypeRandomly(centroid);
                continue;
            }

            initializeWeightenedPrototype(centroid);

        }
    }

    private void initializePrototypeRandomly(int prototypeToInitialize) {
        Point randomPoint = dataSet.randomPoint();
        centroids[prototypeToInitialize] = new Prototype(randomPoint, prototypeToInitialize);
    }

    private void initializeWeightenedPrototype(int centroid) {
        int dataSetSize = dataSet.size();

        // check if the most recently added centroid is closer to any of the points than previously added ones
        for (int p = 0; p < m; p++) {
            // gives chosen points 0 probability of being chosen again -> sampling without replacement
            double tempDistance = Distance.L2(points[p], centroids[c - 1]); // need L2 norm here, not L1

            // base case: if we have only chosen one centroid so far, nothing to compare to
            if (c == 1)
                distToClosestCentroid[p] = tempDistance;

            else { // c != 1
                if (tempDistance < distToClosestCentroid[p])
                    distToClosestCentroid[p] = tempDistance;
            }

            // no need to square because the distance is the square of the euclidean dist
            if (p == 0)
                weightedDistribution[0] = distToClosestCentroid[0];
            else weightedDistribution[p] = weightedDistribution[p - 1] + distToClosestCentroid[p];

        }

        // choose the next centroid
        double rand = gen.nextDouble();
        for (int j = m - 1; j > 0; j--) {
            // TODO: review and try to optimize
            // starts at the largest bin. EDIT: not actually the largest
            if (rand > weightedDistribution[j - 1] / weightedDistribution[m - 1]) {
                choose = j; // one bigger than the one above
                break;
            } else // Because of invalid dimension errors, we can't make the forloop go to j2 > -1 when we have (j2-1) in the loop.
                choose = 0;
        }
    }
}
