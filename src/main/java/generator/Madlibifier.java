package generator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import tagger.TextAnnotater;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Madlibifier {

    // int skipper determines the frequency of madlibification
    // Example: if skipper == 3, madlibify will clear every third madlibifiable word
    public static ArrayList<String> removeMadlibifiables(TextAnnotater text, String filepath, int skipper) throws IOException {
        if (skipper < 1) {
            skipper = 1;
            System.out.println("Invalid skip increment. Skip increment auto set to 1.");
        }
        int i = 1;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            String replacementBlock;
            // posList stores parts of speech for each removed word; list is passed to method that prompts user to input replacement words based on the POS
            ArrayList<String> posList = new ArrayList<>();
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
                        writer.write("[" + replacementBlock + "] ");
                        posList.add(replacementBlock);
                        i = 1;
                    }
                    else {
                        writer.write(token.get(CoreAnnotations.TextAnnotation.class) + " ");
                    }
                }
            }
            return posList;
        }
        catch (Exception e) {
            throw new IOException("Madlibification failed. Please try again");
        }
    }


    public static void main(String[] args) throws IOException {
        TextAnnotater newText = new TextAnnotater("On a crisp autumn morning, Emily wandered through Central Park, admiring the golden leaves that danced gently in the breeze. She paused beside a quiet pond, where ducks glided lazily across the water. Suddenly, her phone buzzed — a reminder that she was already late for her meeting downtown. With a sigh, she tightened her scarf, picked up her pace, and disappeared into the bustling crowd.");
        removeMadlibifiables(newText, "/Users/adambarnett/Coding/MadlibMachine/madlib-machine/src/moose.txt", 5);
    }


}
