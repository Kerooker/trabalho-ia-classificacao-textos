package ai.assignment.representation;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class TokenObtainer {

    private static final FileFilter directoriesFilter = File::isDirectory;
    private static final FileFilter filesFilter = file -> !file.isDirectory();

    public static LinkedHashSet<String> obtainTokensInOrder() {
        File corpusDirectory = new File("corpus");

        List<File> textDirectories = Arrays.asList(corpusDirectory.listFiles(directoriesFilter));

        textDirectories.forEach(directory -> {
            List<File> individualTexts = Arrays.asList(directory.listFiles(filesFilter));

            individualTexts.parallelStream().forEach(textFile -> {
            });
        });
    }

}
