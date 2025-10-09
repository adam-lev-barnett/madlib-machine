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

    // int skipper determines the frequency of madlibification
    // Example: if skipper == 3, madlibify will clear every third madlibifiable word
    public static void madlibify(AnnotatedText text, String filepath, int skipper) throws IOException {
        if (skipper < 1) {
            skipper = 1;
            System.out.println("Invalid skip increment. Skip increment auto set to 1.");
        }
        int i = 1;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            String replacementBlock;
            for (CoreLabel token : text.getDocument().tokens()) {

                // Retrieve the [part of speech block] to replace the word in the new madlib
                // pos Map in PosRemover returns null if part of speech can't be madlibified
                replacementBlock = PosRemover.getPosReplacementBlock(token.get(CoreAnnotations.PartOfSpeechAnnotation.class));

                if (i < skipper) {
                    writer.write(token.get(CoreAnnotations.TextAnnotation.class) + " ");

                    // i only increments when the current word is madlibifiable
                    if (replacementBlock != null) i++;
                }

                // the skipper count resets after a word is madlibified
                else {
                    if (replacementBlock != null) {
                        writer.write(replacementBlock + " ");
                        i = 1;
                    }
                    else {
                        writer.write(token.get(CoreAnnotations.TextAnnotation.class) + " ");
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {
        AnnotatedText newText = new AnnotatedText("On a crisp autumn morning, Emily wandered through Central Park, admiring the golden leaves that danced gently in the breeze. She paused beside a quiet pond, where ducks glided lazily across the water. Suddenly, her phone buzzed — a reminder that she was already late for her meeting downtown. With a sigh, she tightened her scarf, picked up her pace, and disappeared into the bustling crowd.");
        madlibify(newText, "/Users/adambarnett/Coding/MadlibMachine/madlib-machine/src/moose.txt", 3);
    }


}
