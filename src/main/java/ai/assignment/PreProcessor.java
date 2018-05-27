package ai.assignment;

import ai.assignment.preproccessing.TextExtractor;
import ai.assignment.preproccessing.TextPreProcessor;

public class PreProcessor {

    public static void main(String[] args) {
        TextExtractor.extract();
        TextPreProcessor.processAll();
    }
}
