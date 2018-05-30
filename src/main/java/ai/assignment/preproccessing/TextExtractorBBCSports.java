package ai.assignment.preproccessing;

import ai.assignment.common.Directories;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TextExtractorBBCSports {


    public static void extractTexts() throws IOException {
        File[] directories = Directories.CORPUS_DIRECTORY.listFiles();

        List<String> texts = new ArrayList<>();
        for(File directory : directories) {
            File[] allFiles = directory.listFiles();
            for (File individualTextFile : allFiles) {
                String text = String.join("\n", Files.readAllLines(individualTextFile.toPath(), StandardCharsets.ISO_8859_1));
                texts.add(text);
                individualTextFile.delete();
            }
            directory.delete();
        }

        for (int i = 0; i < texts.size(); i++) {
            File file = new File(Directories.CORPUS_DIRECTORY, i+"");
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(texts.get(i).getBytes());
            stream.flush();
            stream.close();
        }


    }
}
