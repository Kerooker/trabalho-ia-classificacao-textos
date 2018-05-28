package ai.assignment.kmeans.data;

import ai.assignment.kmeans.calculator.distance.DistanceCalculator;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Dataset {

    private final ConcurrentHashMap<Point, Prototype> data = new ConcurrentHashMap<>();
    private final DistanceCalculator distanceCalculator;
    private final Random random = new Random();

    public Dataset(Point[] data, DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
        for (Point p : data) {
            this.data.put(p, Prototype.NONE);
        }
    }

    public void updatePoints(Prototype[] centroids) {
        data.forEachKey(1, point -> {
            Prototype nearestPrototype = point.findNearestPrototype(centroids, distanceCalculator);
            data.put(point, nearestPrototype);
        });
    }

    public List<Point> pointsForPrototype(Prototype searchPrototype) {
        return data.entrySet().parallelStream()
                .filter(it -> it.getValue() == searchPrototype)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Point randomPoint() {
        int randomIndex = random.nextInt(data.size());
        return new ArrayList<>(data.keySet()).get(randomIndex);
    }

    public String mapResults() {
        List<String> strings = data.entrySet().parallelStream().map(it ->
            it.getKey().getOwnerText() + "," + it.getValue().prototypeIndex)
                .sorted()
                .collect(Collectors.toList());

        return String.join("\n", strings);
    }

    public BigDecimal averageSilhouette() {
        Map<Point, BigDecimal> silhouettes = calculateSilhouette();

        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal individual : silhouettes.values()) {
            total = total.add(individual);
        }

        return total.divide(new BigDecimal(silhouettes.size()), MathContext.DECIMAL32);
    }

    public List<Point> points() {
        return new ArrayList<>(data.keySet());
    }

    private Map<Point, BigDecimal> calculateSilhouette() {
        ConcurrentHashMap<Point, BigDecimal> silhouettes = new ConcurrentHashMap<>();
        data.keySet().parallelStream().forEach(point -> {
            System.out.println(System.currentTimeMillis() + "Start of point " + point.getOwnerText());
            BigDecimal averageDistanceToNonGroupPoints = calculateAverageDistanceToNonGroupPoints(point);
            System.out.println(System.currentTimeMillis() + "End of average distance to NonGroup" + point.getOwnerText());
            BigDecimal averageDistanceToGroupPoints = calculateAverageDistanceToGroupPoints(point);
            System.out.println(System.currentTimeMillis() + " End of average distance to grupo " + point.getOwnerText());

            silhouettes.put(point, silhouetteFormula(averageDistanceToNonGroupPoints, averageDistanceToGroupPoints));
            System.out.println("Silhouettes size " + silhouettes.size());
        });

        return silhouettes;
    }

    private BigDecimal calculateAverageDistanceToGroupPoints(Point point) {
        Prototype myPrototype = data.get(point);

        List<Point> myGroup = pointsForPrototype(myPrototype);
        myGroup.remove(point);  //Removing own point

        return point.averageDistanceTo(myGroup, distanceCalculator);

    }

    private BigDecimal calculateAverageDistanceToNonGroupPoints(Point point) {
        Prototype myPrototype = data.get(point);

        Set<Prototype> allPrototypes = new HashSet<>(data.values());
        allPrototypes.remove(myPrototype);

        Prototype nearestGroup = point.findNearestPrototype(allPrototypes.toArray(new Prototype[0]), distanceCalculator);

        List<Point> nearestGroupPoints = pointsForPrototype(nearestGroup);
        return point.averageDistanceTo(nearestGroupPoints, distanceCalculator);
    }

    private BigDecimal silhouetteFormula(BigDecimal averageDistanceToNonGroupPoints,
                                         BigDecimal averageDistanceToGroupPoints) {

        BigDecimal maxOfBoth = averageDistanceToGroupPoints.max(averageDistanceToNonGroupPoints);

        BigDecimal bMinusA = averageDistanceToNonGroupPoints.subtract(averageDistanceToGroupPoints);
        BigDecimal division = bMinusA.divide(maxOfBoth, MathContext.DECIMAL32);

        return division;
    }

    public int size() {
        return data.size();
    }
}
