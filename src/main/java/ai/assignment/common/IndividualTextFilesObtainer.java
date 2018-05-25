package ai.assignment.common;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class IndividualTextFilesObtainer {

    public static Stream<File> getAllIndividualTextFiles() {
        File corpusDirectory = new File("corpus");

        List<File> individualTexts = Arrays.asList(corpusDirectory.listFiles());

        return individualTexts.stream();
    }

    public static Stream<File> getIndividualTfIdfFiles() {
        File tfIdfDirectory = new File("tf_idf_representation");

        List<File> individualTexts = Arrays.asList(tfIdfDirectory.listFiles());
        return individualTexts.stream();
    }

    public static Stream<File> getIndividualTextProcessedFiles() {
        File corpusDirectory = new File("pre_processed_texts");

        List<File> individualTexts = Arrays.asList(corpusDirectory.listFiles());

        return individualTexts.stream();
    }
}
