package ai.assignment.representation;

import ai.assignment.common.IndividualTextFilesObtainer;
import ch.obermuhlner.math.big.BigDecimalMath;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InverseDocumentFrequencyRepresentation {


    private static final File file = new File("tokens", "inverse_document_frequencies");

    public static void representIDF() {

        if (file.exists())return;

        HashMap<String, BigDecimal> inverseDocumentFrequencies = getTokenInverseDocumentFrequencies();

        List<String> invertedDocumentFrequencies = inverseDocumentFrequencies.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .map(BigDecimal::toPlainString)
                .collect(Collectors.toList());

        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);

        file.createNewFile();
        output.write(String.join("\n", invertedDocumentFrequencies).getBytes());
        output.flush();
        output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static HashMap<String, BigDecimal> getTokenInverseDocumentFrequencies() {
        List<String> tokens = new ArrayList<>(TokenObtainer.getTokensInAlphabeticalOrder());

        List<File> documents = IndividualTextFilesObtainer
                .getAllTextsFromPreProcessedDirectory().filter(it -> it.getName().matches("\\d+"))
                .collect(Collectors.toList());

        int totalAmountOfDocuments = documents.size();


        ConcurrentHashMap<String, Integer> documentFrequency = countDocumentFrequency(tokens, documents);

        HashMap<String, BigDecimal> inverseDocumentFrequencies = new HashMap<>();
        tokens.forEach(token -> {
            System.out.println("Calculating Inverse Document Frequency for token " + token);
            BigDecimal division = new BigDecimal(totalAmountOfDocuments)
                    .divide(new BigDecimal(1 + documentFrequency.get(token)), 3, RoundingMode.UP);

            //Using https://github.com/eobermuhlner/big-math to calculate logarithm
            BigDecimal tokenIdf = BigDecimalMath.log(division, MathContext.DECIMAL32);

            inverseDocumentFrequencies.put(token, tokenIdf);

        });
        return inverseDocumentFrequencies;
    }

    private static ConcurrentHashMap<String, Integer> countDocumentFrequency(List<String> tokens, List<File> documents) {
        ConcurrentHashMap<String, Integer> amountOfDocumentsTermAppears = new ConcurrentHashMap<>();

        documents.parallelStream().forEach(document -> {
            System.out.println("Counting document frequency for " + document.getName());
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
