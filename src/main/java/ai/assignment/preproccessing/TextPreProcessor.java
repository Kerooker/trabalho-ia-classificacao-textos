package ai.assignment.preproccessing;

import ai.assignment.common.IndividualTextFilesObtainer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class TextPreProcessor {


    public static void main(String[] args) {
        Stream<File> individualTexts = IndividualTextFilesObtainer.getAllIndividualTextFiles();

        individualTexts.parallel().forEach(textFile -> {
            String fileContent = null;
            try {
                fileContent = String.join("", Files.readAllLines(textFile.toPath()));
            } catch (IOException e) {
                //Shouldn't happen
            }

            System.out.println("Pipelining text " + textFile.getName());
            String pipelinedText = executePipelineOnText(fileContent);
            saveProcessedTextToFile(pipelinedText, textFile);
        });
    }

    private static String executePipelineOnText(String text) {
        TextPipeline pipeliner = new TextPipeline(text);
        pipeliner.executePipeline();
        return pipeliner.getText();
    }

    private static void saveProcessedTextToFile(String pipelinedText, File textFile) {
        try {
            FileOutputStream outputStream = new FileOutputStream(textFile, false);
            outputStream.write(pipelinedText.getBytes());
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Impossible to happen");
        }

    }
}
