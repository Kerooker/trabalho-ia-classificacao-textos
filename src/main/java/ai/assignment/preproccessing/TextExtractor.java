package ai.assignment.preproccessing;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TextExtractor {

    private static final FileFilter textFileFilter = pathname -> !pathname.isDirectory();

    private static AtomicLong counter = new AtomicLong(0);

    public static void main(String[] args) throws IOException {
        File mainCorpusDirectory = new File("corpus");

        List<File> unprocessedTextFiles = Arrays.asList(mainCorpusDirectory.listFiles(textFileFilter));

        unprocessedTextFiles.parallelStream().forEach(unprocessedTextFile -> {
            new TextFileExtractor(unprocessedTextFile, counter).extractTexts();

            System.out.println("Deleting text file " + unprocessedTextFile.getName());
            unprocessedTextFile.delete();
        });

    }


}
