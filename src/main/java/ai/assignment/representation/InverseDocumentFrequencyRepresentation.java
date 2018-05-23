package ai.assignment.representation;

import ai.assignment.common.IndividualTextFilesObtainer;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InverseDocumentFrequencyRepresentation {


    public static void main(String[] args) {
        List<String> tokens = new ArrayList<>(TokenObtainer.obtainTokensInOrder());

        Stream<File> documents = IndividualTextFilesObtainer.getAllIndividualTextFiles().filter(it -> it.getName().matches("\\d"));

        int totalAmountOfDocuments = documents.collect(Collectors.toList()).size();


        ConcurrentHashMap<String, Integer> documentFrequency = countDocumentFrequency(tokens, documents);

        HashMap<String, BigDecimal> inverseDocumentFrequencies = new HashMap<>();
        tokens.forEach(token -> {
            BigDecimal division = new BigDecimal(totalAmountOfDocuments)
                    .divide(new BigDecimal(1 + documentFrequency.get(token)), 3, RoundingMode.UP);

            double doubleValue = division.doubleValue();
            double log = Math.log(doubleValue);
        });


    }

    private static ConcurrentHashMap<String, Integer> countDocumentFrequency(List<String> tokens, Stream<File> documents) {
        ConcurrentHashMap<String, Integer> amountOfDocumentsTermAppears = new ConcurrentHashMap<>();

        documents.parallel().forEach(document -> {
            try {
                String text = String.join("", Files.readAllLines(document.toPath()));
                List<String> words = Arrays.stream(text.split("\\|")).filter(tokens::contains).collect(Collectors.toList());

                words.forEach(word -> {
                    if (amountOfDocumentsTermAppears.containsKey(word)) {
                        int current = amountOfDocumentsTermAppears.get(word);
                        amountOfDocumentsTermAppears.put(word, current + 1);
                    }else {
                        amountOfDocumentsTermAppears.put(word, 1);
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        return amountOfDocumentsTermAppears;
    }
}
