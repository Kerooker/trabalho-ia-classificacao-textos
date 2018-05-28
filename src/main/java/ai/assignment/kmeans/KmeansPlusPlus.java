package ai.assignment.kmeans;

import ai.assignment.kmeans.calculator.distance.DistanceCalculator;
import ai.assignment.kmeans.data.Point;
import ai.assignment.kmeans.data.Prototype;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javafx.util.Pair;

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
            System.out.println("Initializing centroid " + centroid);
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
        ConcurrentHashMap<Point, BigDecimal> distancesToNearestPrototype = new ConcurrentHashMap<>();
        dataSet.points().parallelStream().forEach(point -> {
            Prototype nearestPrototype = point.findNearestPrototype(centroids, distanceCalculator);
            distancesToNearestPrototype.put(point, point.distanceTo(nearestPrototype, distanceCalculator));
        });

       ProbabilityAccumulator accumulator = new ProbabilityAccumulator();
       distancesToNearestPrototype.forEach(accumulator::add);

       Point weightedPoint = accumulator.drawWeightedRandomPoint();
       centroids[centroid] = new Prototype(weightedPoint, centroid);
    }

    private class ProbabilityAccumulator {

        private HashMap<Point, BigDecimal> pointsAndWeights = new HashMap<>();
        private BigDecimal lastAddedDistance = BigDecimal.ZERO;

        public void add(Point point, BigDecimal distance) {
            BigDecimal weight = distance.add(lastAddedDistance);
            pointsAndWeights.put(point, weight);
            lastAddedDistance = weight;
        }

        public Point drawWeightedRandomPoint() {
            List<Pair<Point, BigDecimal>> crescentWeightList =
                    pointsAndWeights.entrySet().stream()
                    .map(it -> new Pair<>(it.getKey(), it.getValue()))
                    .sorted(Comparator.comparing(Pair::getValue))
                    .collect(Collectors.toList());

            BigDecimal maxPoint = crescentWeightList.get(crescentWeightList.size() - 1).getValue();
            BigDecimal randomWeight = randomWeight(maxPoint);

            crescentWeightList.sort(Comparator.comparing(Pair::getValue, Comparator.reverseOrder()));
            for (Pair<Point, BigDecimal> item : crescentWeightList) {
                //If weight is bigger than item's distance
                if (randomWeight.compareTo(item.getValue()) > 0) {
                    return item.getKey();
                }
            }

            throw new RuntimeException("The probability wasn't found!");
        }

        private BigDecimal randomWeight(BigDecimal maxPoint) {
            BigInteger roundedValue = maxPoint.toBigInteger();
            int asIntValue = roundedValue.intValueExact();

            //Plus 2 is needed to include all the possible bounds.
            //This increases the probability for the last point a tiny amount, but it's not relevant
            //given the data.
            return BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(asIntValue + 2));
        }
    }

}
