package ai.assignment.kmeans;

import ai.assignment.common.IndividualTextFilesObtainer;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExecuteKmeans {

    public static void main(String[] args) {
        Point[] points = loadAllPoints();
        SimpleKmeans simpleKmeans = new SimpleKmeans(points, 20, 1000, new BigDecimal("0.01"));
        simpleKmeans.executeSimpleKmeans();

        List<Prototype> prototypes = Arrays.asList(simpleKmeans.getCentroids());

        List<String> relation = simpleKmeans.getDataSet().entrySet().stream().map(it ->
                it.getKey().ownerText + " " + prototypes.indexOf(it.getValue()))
                .collect(Collectors.toList());


    }

    private static Point[] loadAllPoints() {
        List<Point> points = new ArrayList<>();

        IndividualTextFilesObtainer.getIndividualTfIdfFiles().forEach(textFile -> {
            try {
                String text = new String(Files.readAllBytes(textFile.toPath()));
                text = text.replace("[", "");
                text = text.replace("]", "");
                text = text.replace(" ", "");
                String[] numbers = text.split(",");

                List<BigDecimal> coordinates = Arrays.stream(numbers)
                        .map(ExecuteKmeans::bigDecimalFor)
                        .collect(Collectors.toList());

                System.out.println("Mapping coordinates for " + textFile.getName());
                BigDecimal[] coordinateArray = coordinates.toArray(new BigDecimal[0]);

                points.add(new EuclidianPoint(coordinateArray, Integer.parseInt(textFile.getName())));

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        return points.toArray(new Point[0]);
    }

    private static  BigDecimal bigDecimalFor(String s) {
        if (s.equals("0.000000")) {
            return BigDecimal.ZERO;
        }else {
            return new BigDecimal(s);
        }
    }
}
