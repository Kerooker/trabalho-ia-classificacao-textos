package ai.assignment.kmeans;

import ai.assignment.common.IndividualTextFilesObtainer;
import ai.assignment.kmeans.calculator.distance.DistanceCalculator;
import ai.assignment.kmeans.data.Point;
import ai.assignment.kmeans.data.Prototype;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExecuteKmeansPlusPlus {

    private static final int k = 2;
    private static final int maxIterations = 100;
    private static final DistanceCalculator distance = DistanceCalculator.EUCLIDIAN;
    private static final BigDecimal tolerance = new BigDecimal("0.0005");

    public static void main(String[] args) throws IOException {
        Point[] points = loadAllPoints();
        List<String> results = new ArrayList<>();
        for (int k = 2; k < 20; k++) {
            Map<Integer, Integer> values = new HashMap<>();
            KmeansPlusPlus kmeansPlusPlus = new KmeansPlusPlus(points, k, 100,
                    new BigDecimal("1.0"), DistanceCalculator.EUCLIDIAN);
            kmeansPlusPlus.executeKmeans();

            String result = kmeansPlusPlus.result();

            File directory = new File("answer");
            directory.mkdir();
            File kmeansFile = new File(directory, "kmeans_plus_plus_euc_" + k + "_clusters");


            Collection<Prototype> protos = kmeansPlusPlus.dataSet.data.values();
            protos.parallelStream().distinct().forEach(it -> {
                values.put(it.prototypeIndex, Collections.frequency(protos, it));
            });

            FileOutputStream stream = new FileOutputStream(kmeansFile);
            stream.write(result.getBytes());
            stream.flush();
            stream.close();

//        File silhouetteFile = new File(directory, "simple_kmeans_silhouette_cos_" + k + "_clusters");
//        FileOutputStream silhouetteStream = new FileOutputStream(silhouetteFile);
//        silhouetteStream.write(kmeansPlusPlus.silhouette().toString().getBytes());
//        silhouetteStream.flush();
//        silhouetteStream.close();
            results.add(values.toString());
            System.out.println(values.toString());
        }

        System.out.println(results);


    }

    private static Point[] loadAllPoints() {
        List<Point> points = new ArrayList<>();

        IndividualTextFilesObtainer.getAllTextsFromBinaryDirectory().forEach(textFile -> {
            try {
                String text = new String(Files.readAllBytes(textFile.toPath()));
                text = text.replace("[", "");
                text = text.replace("]", "");
                text = text.replace(" ", "");
                String[] numbers = text.split(",");

                List<BigDecimal> coordinates = Arrays.stream(numbers)
                        .map(ExecuteKmeansPlusPlus::bigDecimalFor)
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
        BigDecimal b = new BigDecimal(s);
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }else {
            return b;
        }
    }
}

