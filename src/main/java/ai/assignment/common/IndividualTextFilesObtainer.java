package ai.assignment.common;

import static ai.assignment.common.Directories.TF_IDF_DIRECTORY;
import static ai.assignment.common.Directories.TOKENS_DIRECTORY;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class IndividualTextFilesObtainer {

    public static Stream<File> getAllTextsFromCorpusDirectory() {
        File corpusDirectory = Directories.CORPUS_DIRECTORY;

        List<File> individualTexts = Arrays.asList(corpusDirectory.listFiles());

        return individualTexts.stream();
    }

    public static Stream<File> getAllTextsFromPreProcessedDirectory() {
        File corpusDirectory = Directories.PRE_PROCESSED_DIRECTORY;

        List<File> individualTexts = Arrays.asList(corpusDirectory.listFiles());

        return individualTexts.stream();
    }

    public static Stream<File> getAllTextsFromTfIdfDirectory() {
        List<File> individualTexts = Arrays.asList(TF_IDF_DIRECTORY.listFiles());
        return individualTexts.stream();
    }

    public static File getTokensFile() {
        return new File(TOKENS_DIRECTORY, "ordered_tokens");
    }
}
