package ai.assignment;

import ai.assignment.preproccessing.TextExtractorBBCSports;
import ai.assignment.preproccessing.TextPreProcessor;
import java.io.IOException;

public class PreProcessorBBCSports {

    public static void main(String[] args) throws IOException {
        TextExtractorBBCSports.extractTexts();
        TextPreProcessor.processAll();

    }
}
