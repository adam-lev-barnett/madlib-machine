package tagger;

import edu.stanford.nlp.pipeline.CoreDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextAnnotater {

    /**
     * Provided text is converted to CoreDocument object, allowing the CoreNLP library to annotate parts of speech for each word
     */
    private final CoreDocument document;

    // Constructor creates document object upon instantiation
    public TextAnnotater(String text) {
        this.document = new CoreDocument(text);
        TextParser.getPipeline().annotate(document);
    }

    // If a file has already been instantiated, constructor reads file and calls resulting String constructor
    public TextAnnotater(File file) throws IOException {
        this(Files.readString(file.toPath()));
    }

    public TextAnnotater(Path file) throws IOException {
        this(Files.readString(file));
    }

    public CoreDocument getDocument() {
        return document;
    }

}
