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
}
