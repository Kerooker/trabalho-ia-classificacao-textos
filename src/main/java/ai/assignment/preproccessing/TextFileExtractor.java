package ai.assignment.preproccessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextFileExtractor {

    private File file;
    private String allTexts;

    public TextFileExtractor(File file) {
        this.file = file;
    }

    public void extractTexts() throws IOException {
        allTexts = new String(Files.readAllBytes(file.toPath()), StandardCharsets.ISO_8859_1);
        allTexts = allTexts
                .replaceAll(Pattern.compile("Newsgroup: .*\\ndocument_id:.*\\n",
                        Pattern.CASE_INSENSITIVE).pattern(), "");
        String newsGroup = file.getName().replace(".txt", "");

        File newsGroupDirectory = new File(file.getParent(), newsGroup);
        if (!newsGroupDirectory.mkdir()) {
            throw new RuntimeException("Group already extracted");
        }

        Stream<String> splitTexts = splitTextsByHeader();

        List<String> filteredTexts = splitTexts.collect(Collectors.toList());

        System.out.println("Extracting texts for category " + newsGroup);
        for (int i = 0; i < filteredTexts.size(); i++) {
            String text = filteredTexts.get(i);
            File file = new File(newsGroupDirectory, String.valueOf(i));
            FileWriter writer = new FileWriter(file);
            writer.write(text);
            writer.close();
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
