package tagger;

import edu.stanford.nlp.pipeline.CoreDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AnnotatedText {
    private final CoreDocument document;

    // Constructor creates document object upon instantiation
    public AnnotatedText(String text) {
        this.document = new CoreDocument(text);
        TextParser.getPipeline().annotate(document);
    }

    // If a file has already been instantiated, constructor reads file and calls resulting String constructor
    public AnnotatedText(File file) throws IOException {
        this(Files.readString(file.toPath()));
    }

    public CoreDocument getDocument() {
        return document;
    }

}
