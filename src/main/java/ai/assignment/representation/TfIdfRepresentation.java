package ai.assignment.representation;

import ai.assignment.common.IndividualTextFilesObtainer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TfIdfRepresentation {

    public static void representTfIdf() {
        List<String> tokens = new ArrayList<>(TokenObtainer.getTokensInAlphabeticalOrder());

        HashMap<String, BigDecimal> tokenIDF = InverseDocumentFrequencyRepresentation.getTokenInverseDocumentFrequencies();

        IndividualTextFilesObtainer.getAllTextsFromPreProcessedDirectory().parallel().forEach(it -> {
                String index = it.getName();
                if (!index.matches("\\d*"))return;
                File directory = new File("tf_idf_representation");
                if (!directory.exists()) {
                    directory.mkdir();
                }
                File processedFile = new File(directory, index);

                BigDecimal[] textVector = new BigDecimal[tokens.size()];

                try {
                    String text = String.join("", Files.readAllLines(it.toPath()));
                    List<String> words = Arrays.asList(text.split("\\|"));
                    for (int i = 0; i < tokens.size(); i++) {
                        String token = tokens.get(i);
                        int amount = Collections.frequency(words, token);
                        textVector[i] = tokenIDF.get(token).multiply(new BigDecimal(amount));
                    }

                    String allPositions = Arrays.stream(textVector)
                            .map(BigDecimal::toPlainString)
                            .collect(Collectors.toList()).toString();

                    boolean containsNonZero = Arrays.stream(textVector).anyMatch(itt ->
                            itt.compareTo(BigDecimal.ZERO) > 0
                    );
                    if (!containsNonZero) {
                        //Text is useless to the corpus
                        return;
                    }

                    System.out.println("Representing TF-IDF: " + it.getName());
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
