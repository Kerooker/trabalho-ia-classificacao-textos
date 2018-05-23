package ai.assignment.representation;

import ai.assignment.common.IndividualTextFilesObtainer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TermFrequencyRepresentation {

    public static void main(String[] args) {
        List<String> tokens = new ArrayList<>(TokenObtainer.obtainTokensInOrder());

        IndividualTextFilesObtainer.getAllIndividualTextFiles().parallel().forEach(it -> {
            String index = it.getName();
            if (!index.matches("\\d"))return;
            File processedFile = new File("term_frequency_representation", index);

            int[] textVector = new int[tokens.size()];

            try {
                String text = String.join("", Files.readAllLines(it.toPath()));
                List<String> words = Arrays.asList(text.split("\\|"));
                for (int i = 0; i < tokens.size(); i++) {
                    String token = tokens.get(i);
                    int amount = Collections.frequency(words, token);
                    textVector[i] = amount;
                }

                System.out.println("Representing token frequency: " + it.getName());
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
