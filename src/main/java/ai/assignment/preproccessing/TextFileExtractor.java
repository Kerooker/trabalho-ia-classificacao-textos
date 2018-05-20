package ai.assignment.preproccessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextFileExtractor {

    private File file;
    private String allTexts;
    private AtomicLong counter;

    public TextFileExtractor(File file, AtomicLong counter) {
        this.file = file;
        this.counter = counter;
    }

    public void extractTexts()  {
        try {
            allTexts = new String(Files.readAllBytes(file.toPath()), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            //Shouldn't happen
        }
        allTexts = allTexts
                .replaceAll(Pattern.compile("Newsgroup: .*\\ndocument_id:.*\\n",
                        Pattern.CASE_INSENSITIVE).pattern(), "");
        String newsGroup = file.getName().replace(".txt", "");


        Stream<String> splitTexts = splitTextsByHeader();

        List<String> filteredTexts = splitTexts.collect(Collectors.toList());

        System.out.println("Extracting texts for category " + newsGroup);
        for (String text : filteredTexts) {
            File file = new File(String.valueOf(counter.incrementAndGet()));
            FileWriter writer;
            try {
                writer = new FileWriter(file);

                writer.write(text);
                writer.close();
            } catch (IOException e) {
                //Shouldn't happen
            }
        }
    }


    private Stream<String> splitTextsByHeader() {
        String[] splitTexts = allTexts.split("From:.*\\n");

        return Arrays.stream(splitTexts)
                .filter(it -> !it.isEmpty())
                .map(it -> it.replaceAll("(?i)NewsGroup: .*\\n", ""))
                .map(it -> it.replaceAll("(?i)Document_id:.*\\n", ""))
                .map(it -> it.replaceAll("Subject:.*\\n", ""))
                .map(String::trim)
                .distinct();
    }
}
