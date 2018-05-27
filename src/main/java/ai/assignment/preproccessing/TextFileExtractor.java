package ai.assignment.preproccessing;

import static ai.assignment.common.Directories.CORPUS_DIRECTORY;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TextFileExtractor {

    private File file;
    private String allTexts;
    private AtomicLong counter;

    public TextFileExtractor(File file, AtomicLong counter) {
        this.file = file;
        this.counter = counter;
    }

    public void extractTexts()  {
        readAllTextsFromFile();
        String newsGroupName = getNewsgroupName();

        List<String> splitTexts = splitTextsByHeader();

        System.out.println("Extracting texts for category " + newsGroupName);
        for (String text : splitTexts) {
            writeTextToFile(text);
        }
    }

    private String getNewsgroupName() {
        return file.getName().replace(".txt", "");
    }

    private void readAllTextsFromFile() {
        try {
            allTexts = new String(Files.readAllBytes(file.toPath()), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTextToFile(String text) {
        File file = new File(CORPUS_DIRECTORY, String.valueOf(counter.incrementAndGet()));
        FileWriter writer;
        try {
            writer = new FileWriter(file);

            writer.write(text);
            writer.close();
        } catch (IOException e) {
            //Shouldn't happen
        }
    }

    private List<String> splitTextsByHeader() {
        String[] splitTexts = allTexts.split("From:.*\\n");

        return Arrays.stream(splitTexts)
                .filter(it -> !it.isEmpty())

                //Removing headers that add nothing to the text subject
                .map(it -> it.replaceAll("(?i)NewsGroup: .*\\n", ""))
                .map(it -> it.replaceAll("(?i)Document_id:.*\\n", ""))
                .map(it -> it.replaceAll("Subject:.*\\n", ""))
                .map(String::trim)
                .distinct()
                .filter(it -> !it.isEmpty())
                .collect(Collectors.toList());
    }
}
