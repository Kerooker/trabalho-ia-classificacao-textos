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

    public static void representTermFrequency() {
        List<String> tokens = new ArrayList<>(TokenObtainer.getTokensInAlphabeticalOrder());

        IndividualTextFilesObtainer.getAllTextsFromPreProcessedDirectory().parallel().forEach(it -> {
            String index = it.getName();
            if (!index.matches("\\d*"))return;
            File directory = new File("term_frequency_representation");
            if (!directory.exists()) {
                directory.mkdir();
            }
            File processedFile = new File(directory, index);

            int[] textVector = new int[tokens.size()];

            try {
                String text = String.join("", Files.readAllLines(it.toPath()));
                List<String> words = Arrays.asList(text.split("\\|"));
                for (int i = 0; i < tokens.size(); i++) {
                    String token = tokens.get(i);
                    int amount = Collections.frequency(words, token);
                    textVector[i] = amount;
                }

                List<Integer> list = new ArrayList<>();
                for (int i : textVector)list.add(i);
                if (!list.contains(1)) {
                    //This text is useless to the corpus.
                    return;
                }

                String allPositions = Arrays.toString(textVector);

                System.out.println("Representing token frequency: " + it.getName());
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
