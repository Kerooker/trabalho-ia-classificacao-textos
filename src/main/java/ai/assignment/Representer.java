package ai.assignment;

import ai.assignment.representation.BinaryRepresentation;
import ai.assignment.representation.InverseDocumentFrequencyRepresentation;
import ai.assignment.representation.TermFrequencyRepresentation;
import ai.assignment.representation.TfIdfRepresentation;

public class Representer {

    public static void main(String[] args) {
        BinaryRepresentation.representAsBinary();
        InverseDocumentFrequencyRepresentation.representIDF();
        TermFrequencyRepresentation.representTermFrequency();
        TfIdfRepresentation.representTfIdf();

    }
}
