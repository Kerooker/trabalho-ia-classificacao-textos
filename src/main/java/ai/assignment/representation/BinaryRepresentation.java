package ai.assignment.representation;

import ai.assignment.common.IndividualTextFilesObtainer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinaryRepresentation {

    public static void main(String[] args) {
        List<String> tokens = new ArrayList<>(TokenObtainer.obtainTokensInOrder());

        IndividualTextFilesObtainer.getAllIndividualTextFiles().parallel().forEach(it -> {
            String index = it.getName();
            if (!index.matches("\\d*"))return;
            File directory = new File("binary_representation");
            directory.mkdir();
            File processedFile = new File(directory, index);

            int[] textVector = new int[tokens.size()];

            try {
                String text = String.join("", Files.readAllLines(it.toPath()));
                List<String> words = Arrays.asList(text.split("\\|"));
                words.forEach(word -> {
                    if (tokens.contains(word)) {
                        textVector[tokens.indexOf(word)] = 1;
                    }
                });

                System.out.println("Representing as binary: " + it.getName());
                String allPositions = Arrays.toString(textVector);
                FileOutputStream stream = new FileOutputStream(processedFile);
                stream.write(allPositions.getBytes());
                stream.flush();
                stream.close();

            } catch (IOException e) {
                //Shouldn't happen
            }
        });

    }
}
