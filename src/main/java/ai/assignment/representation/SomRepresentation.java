package ai.assignment.representation;

import ai.assignment.common.Directories;
import ai.assignment.common.IndividualTextFilesObtainer;
import ai.assignment.kmeans.data.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SomRepresentation {

    public static void main(String[] args) throws Exception {
        File somRepresentation = new File(Directories.TOKENS_DIRECTORY, "som.csv");

        FileOutputStream stream = new FileOutputStream(somRepresentation);
        String header = String.join(",", TokenObtainer.getTokensInAlphabeticalOrder()) + "\n";
        stream.write(header.getBytes());
        stream.flush();

        Arrays.stream(loadAllPoints()).forEach(point -> {
            BigDecimal[] coordinates = point.getCoordinates();
            List<String> coordinatesAsString = Arrays.stream(coordinates)
                    .map(BigDecimal::toPlainString)
                    .collect(Collectors.toList());

            String points = String.join(",", coordinatesAsString) + "\n";
            try {
                stream.write(points.getBytes());
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        stream.flush();
        stream.close();

    }


    private static Point[] loadAllPoints() throws Exception {
        List<Point> points = new ArrayList<>();

        IndividualTextFilesObtainer.getAllTextsFromTfIdfDirectory().forEach(textFile -> {
            try {
                String text = new String(Files.readAllBytes(textFile.toPath()));
                text = text.replace("[", "");
                text = text.replace("]", "");
                text = text.replace(" ", "");
                String[] numbers = text.split(",");

                List<BigDecimal> coordinates = Arrays.stream(numbers)
                        .map(SomRepresentation::bigDecimalFor)
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
