package ai.assignment.preproccessing;

import static ai.assignment.common.Directories.CORPUS_DIRECTORY;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TextExtractor {

    private static final FileFilter textFileFilter = pathname -> !pathname.isDirectory();

    private static AtomicLong counter = new AtomicLong(0);

    public static void extract() {
        List<File> unprocessedTextFiles = Arrays.asList(CORPUS_DIRECTORY.listFiles(textFileFilter));

        unprocessedTextFiles.parallelStream().forEach(unprocessedTextFile -> {
            new TextFileExtractor(unprocessedTextFile, counter).extractTexts();

            System.out.println("Deleting text file " + unprocessedTextFile.getName());
            unprocessedTextFile.delete();
        });

    }


}
