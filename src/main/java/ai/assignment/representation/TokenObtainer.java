package ai.assignment.representation;

import ai.assignment.common.IndividualTextFilesObtainer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TokenObtainer {

    private static File tokens = IndividualTextFilesObtainer.getTokensFile();


    public static List<String> getTokensInAlphabeticalOrder() {
        if (tokens.exists()) {
            //Token file already exists, read it and return
            return readTokenFile();
        }else {
            buildTokenList();
            return readTokenFile();
        }
    }

    private static List<String> readTokenFile() {
        try {
            return new ArrayList<>(Files.readAllLines(tokens.toPath()));
        } catch (IOException e) {
            //Shouldn't happen
            throw new RuntimeException();
        }
    }

    private static void buildTokenList() {
        ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
        try {
            IndividualTextFilesObtainer.getAllTextsFromPreProcessedDirectory().parallel().forEach(it -> {
            String[] words = readTextFromFile(it);

            System.out.println("Processing tokens of text " + it.getName());
                addWordsToMap(map, words);
            });

            FileOutputStream output = new FileOutputStream(tokens);

            List<String> uniqueTokensThatAppearOftenEnough = filterTokensThatAppearOftenEnough(map);

            saveOutputToFile(output, uniqueTokensThatAppearOftenEnough);

        } catch (IOException e) {
            //Shouldn't happen
        }


    }

    private static void saveOutputToFile(FileOutputStream output,
                                         List<String> uniqueTokensThatAppearOftenEnough) {

        try {
            //TreeSet guarantees the alphabetical ordering
            output.write(String.join("\n", new TreeSet<>(uniqueTokensThatAppearOftenEnough)).getBytes());
            output.flush();
            output.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Filtering all tokens that appeared at least 50 times in the corpus. Anything less than that is likely
     * to be too specific for a certain text and isn't considered a relevant token.
     */
    private static List<String> filterTokensThatAppearOftenEnough(ConcurrentHashMap<String, Long> map) {
        return map.entrySet()
                        .parallelStream()
                        .filter(it -> it.getValue() > 50)
                        .filter(it -> it.getKey() != null && !it.getKey().equals(""))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
    }

    private static void addWordsToMap(ConcurrentHashMap<String, Long> map, String[] words) {
        for (String w : words) {
            if (map.containsKey(w)) {
                Long current = map.get(w);
                map.put(w, current+1);
            }else {
                map.put(w, 1L);
            }
        }
    }

    private static String[] readTextFromFile(File it) {
        String text = null;
        try {
            text = String.join("", Files.readAllLines(it.toPath()));
        } catch (IOException e) {
            //Shouldn't happen
            e.printStackTrace();
        }
        return text.split("\\|");
    }

}
