package tagger;

import java.util.Properties;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;

public enum TextParser {
    INSTANCE;

    private final static Properties props = new Properties();
    private final static StanfordCoreNLP pipeline;

    static {
        props.setProperty("annotators", "tokenize,ssplit,pos");
        pipeline = new StanfordCoreNLP(props);
    }

    public static StanfordCoreNLP getPipeline() {
        return pipeline;
    }



}
