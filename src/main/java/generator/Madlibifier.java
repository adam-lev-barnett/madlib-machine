package generator;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;
import tagger.AnnotatedText;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Madlibifier {

    public static void madlibify(AnnotatedText text, String filepath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (CoreLabel token : text.getDocument().tokens()) {
                if (token.get(CoreAnnotations.PartOfSpeechAnnotation.class).equalsIgnoreCase("NN")) {
                    writer.write("[noun] ");
                } else {
                    writer.write(token.get(CoreAnnotations.TextAnnotation.class) + " ");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        AnnotatedText newText = new AnnotatedText("The quick brown fox jumped over the lazy dog.");
        madlibify(newText, "/Users/adambarnett/Coding/MadlibMachine/madlib-machine/src/moose.txt");
    }
}
