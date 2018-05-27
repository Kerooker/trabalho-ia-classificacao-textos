package ai.assignment.kmeans;

import ai.assignment.common.IndividualTextFilesObtainer;
import ai.assignment.kmeans.calculator.data.Point;
import ai.assignment.kmeans.calculator.distance.DistanceCalculator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
public class ExecuteSimpleKmeans {

    public static void main(String[] args) throws IOException {
        Point[] points = loadAllPoints();
        int k = 3;
        SimpleKmeans simpleKmeans = new SimpleKmeans(points, k, 1000,
                new BigDecimal("0.03"), DistanceCalculator.COSINE_SIMILARITY);
        simpleKmeans.executeSimpleKmeans();

        String result = simpleKmeans.result();

        File directory = new File("answer");
        directory.mkdir();
        File kmeansFile = new File(directory, "simple_kmeans_cosine");

        FileOutputStream stream = new FileOutputStream(kmeansFile);
        stream.write(result.getBytes());
        stream.flush();
        stream.close();

//        File silhouetteFile = new File(directory, "simple_kmeans_silhouette_" + k + "_clusters");
//        FileOutputStream silhouetteStream = new FileOutputStream(silhouetteFile);
//        silhouetteStream.write(simpleKmeans.silhouette().toString().getBytes());
//        silhouetteStream.flush();
//        silhouetteStream.close();



    }

    private static Point[] loadAllPoints() {
        List<Point> points = new ArrayList<>();

        IndividualTextFilesObtainer.getAllTextsFromTfIdfDirectory().forEach(textFile -> {
            try {
                String text = new String(Files.readAllBytes(textFile.toPath()));
                text = text.replace("[", "");
                text = text.replace("]", "");
                text = text.replace(" ", "");
                String[] numbers = text.split(",");

                List<BigDecimal> coordinates = Arrays.stream(numbers)
                        .map(ExecuteSimpleKmeans::bigDecimalFor)
                        .collect(Collectors.toList());

                System.out.println("Mapping coordinates for " + textFile.getName());
                BigDecimal[] coordinateArray = coordinates.toArray(new BigDecimal[0]);

                points.add(new Point(coordinateArray, Integer.parseInt(textFile.getName())));

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
