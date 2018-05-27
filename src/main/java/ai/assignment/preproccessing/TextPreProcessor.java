package ai.assignment.preproccessing;

import static ai.assignment.common.Directories.PRE_PROCESSED_DIRECTORY;

import ai.assignment.common.IndividualTextFilesObtainer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class TextPreProcessor {


    public static void processAll() {
        Stream<File> individualTexts = IndividualTextFilesObtainer.getAllTextsFromCorpusDirectory();

        individualTexts.parallel().forEach(textFile -> {
            String fileContent = getTextFromFile(textFile);

            System.out.println("Pipelining text " + textFile.getName());
            String pipelinedText = executePipelineOnText(fileContent);
            saveProcessedTextToFile(pipelinedText, textFile.getName());
        });
    }

    private static String getTextFromFile(File textFile) {
        String fileContent = null;
        try {
            fileContent = String.join("", Files.readAllLines(textFile.toPath()));
        } catch (IOException e) {
            //Shouldn't happen
            e.printStackTrace();
        }
        return fileContent;
    }

    private static String executePipelineOnText(String text) {
        TextPipeliner pipeliner = new TextPipeliner(text);
        pipeliner.executePipeline();
        return pipeliner.getText();
    }

    private static void saveProcessedTextToFile(String pipelinedText, String textFile) {
        try {
            File outputFile = new File(PRE_PROCESSED_DIRECTORY, textFile);
            if (pipelinedText.isEmpty()) {
                return;
            }
            outputFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(outputFile, false);
            outputStream.write(pipelinedText.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            //Shouldn't happen
            e.printStackTrace();
        }

    }
}
