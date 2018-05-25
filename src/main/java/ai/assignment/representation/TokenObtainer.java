package ai.assignment.representation;

import ai.assignment.common.IndividualTextFilesObtainer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TokenObtainer {

    public static TreeSet<String> obtainTokensInOrder() {
        File tokens = new File("tokens", "ordered_tokens");
        new File("tokens").mkdir();
        if (tokens.exists()) {
            try {
                return new TreeSet<>(Files.readAllLines(tokens.toPath()));
            } catch (IOException e) {
                //Shouldn't happen
                throw new RuntimeException();
            }
        }else {
            buildTokenList();
            return obtainTokensInOrder();
        }
    }

    private static void buildTokenList() {
        ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
        try {
            File createdFile = new File("tokens", "ordered_tokens");

            IndividualTextFilesObtainer.getIndividualTextProcessedFiles().parallel().forEach(it -> {
                try {
                    String text = String.join("", Files.readAllLines(it.toPath()));
                    String[] words = text.split("\\|");
                    System.out.println("Processing tokens of text " + it.getName());

                    for (String w : words) {
                        if (map.containsKey(w)) {
                            Long current = map.get(w);
                            map.put(w, current+1);
                        }else {
                            map.put(w, 1L);
                        }
                    }
                } catch (IOException e) {
                    //Shouldn't happen
                }
            });

            createdFile.createNewFile();
            FileOutputStream output = new FileOutputStream(createdFile);

            List<String> uniqueStringWithMoreThanOneToken = map.entrySet()
                    .parallelStream()
                    .filter(it -> it.getValue() > 50)
                    .filter(it -> it.getKey() != null && !it.getKey().equals(""))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            output.write(String.join("\n", new TreeSet<>(uniqueStringWithMoreThanOneToken)).getBytes());
            output.flush();
            output.close();

        } catch (IOException e) {
            //Shouldn't happen
        }


    }

}
