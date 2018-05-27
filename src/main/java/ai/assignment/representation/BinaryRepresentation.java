package ai.assignment.representation;

import static ai.assignment.common.Directories.BINARY_DIRECTORY;

import ai.assignment.common.IndividualTextFilesObtainer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinaryRepresentation {

    public static void representAsBinary() {

        List<String> tokens = TokenObtainer.getTokensInAlphabeticalOrder();

        IndividualTextFilesObtainer.getAllTextsFromPreProcessedDirectory().parallel().forEach(it -> {
            String index = it.getName();
            File processedFile = new File(BINARY_DIRECTORY, index);

            int[] textVector = new int[tokens.size()];

            try {
                String text = String.join("", Files.readAllLines(it.toPath()));
                List<String> words = Arrays.asList(text.split("\\|"));
                words.forEach(word -> {
                    if (tokens.contains(word)) {
                        textVector[tokens.indexOf(word)] = 1;
                    }
                });

                List<Integer> list = new ArrayList<>();
                for (int i : textVector)list.add(i);
                if (!list.contains(1)) {
                    //This text is useless to the corpus, it contains no useful token.
                    return;
                }

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
