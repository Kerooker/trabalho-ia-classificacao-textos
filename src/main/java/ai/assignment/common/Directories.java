package ai.assignment.common;

import java.io.File;

public class Directories {

    public static final File CORPUS_DIRECTORY = new File("corpus");
    public static final File PRE_PROCESSED_DIRECTORY = new File("pre_processed_texts");
    public static final File TOKENS_DIRECTORY = new File("tokens");
    public static final File BINARY_DIRECTORY = new File("binary_representation");
    public static final File TF_IDF_DIRECTORY = new File("tf_idf_representation");
    public static final File TERM_FREQUENCY_DIRECTORY = new File("term_frequency_representation");

    static {
        PRE_PROCESSED_DIRECTORY.mkdir();
        TF_IDF_DIRECTORY.mkdir();
        TOKENS_DIRECTORY.mkdir();
        BINARY_DIRECTORY.mkdir();
    }
}
