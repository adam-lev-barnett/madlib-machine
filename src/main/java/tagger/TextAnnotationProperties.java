package tagger;

import java.util.Properties;

import edu.stanford.nlp.pipeline.*;

/** Singleton that determines which properties words should be tagged with. The properties are then passed through a pipeline through which TextAnnotater parses source text to madlibify.
 * The only properties requested are the words themselves (tokens) and their parts of speech.
 * @see TextAnnotater
 * @see madlibgeneration.Madlib*/
public enum TextAnnotationProperties {
    INSTANCE;

    /** Properties for CoreNLP are set to pair each word parsed (separated by white space) only with its part of speech*/
    private final static Properties props = new Properties();

    /** Pipeline object is responsible for feeding the text file through the annotator*/
    private final static StanfordCoreNLP pipeline;

    static {
        props.setProperty("annotators", "tokenize,ssplit,pos");
        pipeline = new StanfordCoreNLP(props);
    }

    public StanfordCoreNLP getPipeline() {
        return pipeline;
    }

    public static TextAnnotationProperties getInstance() {
        return INSTANCE;
    }



}
