package tagger;

import edu.stanford.nlp.pipeline.CoreDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Provided text is converted to CoreDocument object, allowing the CoreNLP library to annotate parts of speech for each word
 * @see TextAnnotationProperties
 * @see madlibgeneration.Madlib*/
public class TextAnnotater {

    private final CoreDocument document;

    /** Constructor creates document object upon instantiation with each word and punctuation identified with a part of speech */
    public TextAnnotater(String text) {
        this.document = new CoreDocument(text);
        TextAnnotationProperties.getInstance().getPipeline().annotate(document);
    }

    /** If a Path has already been instantiated, constructor reads file and calls resulting String constructor */
    public TextAnnotater(Path file) throws IOException {
        this(Files.readString(file));
    }

    public CoreDocument getDocument() {
        return document;
    }

}
