package generator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import tagger.TextAnnotater;
import utility.exceptions.TextNotProcessedException;

import java.io.*;
import java.util.*;

public abstract class Madlibifier {

    /** Identifies where in the text file the words should be replaced with the user's new words */
    private static final Set<String> posBlocks = new HashSet<>();
    private static final Set<String> wordsToSkip = new HashSet<>();


    static {
        // The parts os speech to blank out and their associated text blocks; when commented out, madlibifier doesn't consider it a madlibifiable word
        posBlocks.add("[noun]");
        posBlocks.add("[pluralNoun]");
        posBlocks.add("[verb]");
        posBlocks.add("[verbPast]");
        // posBlocks.add("[gerund]");
        posBlocks.add("[verbEndingInS]");
        posBlocks.add("[adjective]");
        //posBlocks.add("[adjective ending in \"er\"]");
        posBlocks.add("[adverb]");
        //posBlocks.add("[adverb ending in \"est\"]");
        posBlocks.add("[interjection]");

        wordsToSkip.add("be");
        wordsToSkip.add("not");
        wordsToSkip.add("using");
        wordsToSkip.add("uses");
        wordsToSkip.add("use");
        wordsToSkip.add("used");
        wordsToSkip.add("have");
        wordsToSkip.add("has");
        wordsToSkip.add("had");
        wordsToSkip.add("shall");
    }

    // Removes the skipper-th word with a part of speech in the posBlocks hashset
    // int skipper determines the frequency of madlibification
    // Example: if skipper == 3, madlibify will clear every third madlibifiable word
    // returns ArrayList of parts of speech removed so user can replace the removed words when prompted by CLI
    public static ArrayList<String> removeMadlibifiables(TextAnnotater text, String filepath, int skipper) throws IOException, TextNotProcessedException {

        if (text == null) throw new TextNotProcessedException("Madlib machine encountered an error. Unable to process text to detect parts of speech.");

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

                // disregard any words in wordsToSkip by resetting the block to null
                if (wordsToSkip.contains(token.word())) replacementBlock = null;

                if (i < skipper) {
                    justWriteWord(token, writer);
                    // i only increments when the current word is madlibifiable
                    if (replacementBlock != null) i++;
                }
                // the skipper count resets after a word is madlibified
                else {
                    if (replacementBlock != null) {
                        writer.write(" [" + replacementBlock + "]");
                        posList.add(replacementBlock);
                        i = 1;
                    }
                    else {
                        justWriteWord(token, writer);
                    }
                }
            }
            System.out.println("Madlib skeleton successfully generated in src folder");
            return posList;
        }
        catch (Exception e) {
            throw new IOException("Madlibification failed. Please try again");
        }
    }

    // Adds space before each word for simple avoidance of spaces before punctuation
    // Nothing is added to the punctuation character

    /** Helper method for removeMadlibifiable() that writes each word to a file with a preceding space*/
    private static void justWriteWord(CoreLabel token, BufferedWriter writer) throws IOException {
        if (token.word().matches("\\p{Punct}")) {
            writer.write(token.get(CoreAnnotations.TextAnnotation.class));
        }
        else writer.write(" " + token.get(CoreAnnotations.TextAnnotation.class));
    }

    /** Takes madlibified madlib (txt file with words replaced by [part of speech] blocks) and fills in with replacement word Queue*/
    public static void fillInMadlib(String inFilepath, String outFilepath, Queue<String> replacementWords) throws Exception {

        try (BufferedReader reader = new BufferedReader(new FileReader(inFilepath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFilepath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");

                for (String word : words) {
                    if (word == null) continue;
                    // If the replacement word queue is empty, continue simply copying words into the file
                    String strippedWord = word.replaceAll("[^a-zA-Z\\[\\]]", "");
                    if (posBlocks.contains(strippedWord) && replacementWords.peek() != null) {
                        writer.write(replacementWords.poll() + " ");
                    }
                    else writer.write(word + " ");
                }
                writer.newLine();
            }
            System.out.println("Madlib successfully populated and saved to the src folder!");
        }

        catch(Exception e) {
            throw new Exception("nope.");
        }
    }


}
