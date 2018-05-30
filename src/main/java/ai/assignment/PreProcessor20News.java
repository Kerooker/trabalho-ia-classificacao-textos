package ai.assignment;

import ai.assignment.preproccessing.TextExtractor20News;
import ai.assignment.preproccessing.TextPreProcessor;

public class PreProcessor20News {

    public static void main(String[] args) {
        TextExtractor20News.extract();
        TextPreProcessor.processAll();
    }
}
