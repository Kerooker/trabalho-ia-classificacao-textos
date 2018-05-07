package ai.assignment.preproccessing;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class TextExtractor {

    private static final FileFilter textFileFilter = pathname -> !pathname.isDirectory();

    public static void main(String[] args) throws IOException {
        File mainCorpusDirectory = new File("corpus");

        File[] unprocessedTextFiles = mainCorpusDirectory.listFiles(textFileFilter);

        for (File unprocessedTextFile : unprocessedTextFiles) {
            new TextFileExtractor(unprocessedTextFile).extractTexts();

            System.out.println("Deleting text file " + unprocessedTextFile.getName());
            unprocessedTextFile.delete();
        }
    }


}
