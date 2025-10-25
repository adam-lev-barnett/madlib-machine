package generator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import tagger.TextAnnotater;
import utility.exceptions.InvalidPartOfSpeechException;
import utility.exceptions.TextNotProcessedException;

import java.io.*;
import java.util.*;

/* Converts text into Madlibs by removing words and replacing them with text blocks containing the associated part of speech**/
public abstract class Madlibifier {

    // Converts CoreNLP tags with clearer part of speech identifiers
    private static final HashMap<String, String> posMap = new HashMap<>();

    /** Identifies where in the text file the words should be replaced with the user's new words */
    private static final Set<String> posBlocks = new HashSet<>();
    private static final Set<String> wordsToSkip = new HashSet<>();

    // Initialize collections for part of speech identification, removal, and replacement
    // Filters for parts of speech and words that should never be removed
    static {
        // Comment out parts of speech you don't want to skip
        //! When commenting out pos, be sure to comment out the same part of speech in posBlocks below
        posMap.put("NN", "noun");
        posMap.put("NNS", "pluralNoun");
        posMap.put("VB", "verb");
        posMap.put("VBD", "verbPast");
        // posReplacements.put("VBG", "gerund");
        posMap.put("VBZ", "verbEndingInS");
        posMap.put("JJ", "adjective");
        // posReplacements.put("JJR", "adjective ending in \"er\"");
        posMap.put("RB", "adverb");
        // posReplacements.put("RBS", "adverb ending in \"est\"");
        posMap.put("UH", "interjection");

        // List of words to avoid that have the accepted parts of speech
        wordsToSkip.add("be");
        wordsToSkip.add("being");
        wordsToSkip.add("am");
        wordsToSkip.add("not");
        wordsToSkip.add("using");
        wordsToSkip.add("uses");
        wordsToSkip.add("use");
        wordsToSkip.add("used");
        wordsToSkip.add("have");
        wordsToSkip.add("has");
        wordsToSkip.add("had");
        wordsToSkip.add("shall");
        wordsToSkip.add("is");
        wordsToSkip.add("was");
        wordsToSkip.add("were");
        wordsToSkip.add("isn't");
        wordsToSkip.add("behalf");
        wordsToSkip.add("can");
        wordsToSkip.add("cannot");
        wordsToSkip.add("can't");
        wordsToSkip.add("will");
        wordsToSkip.add("won't");
        wordsToSkip.add("would");
        wordsToSkip.add("must");
        wordsToSkip.add("might");
        wordsToSkip.add("may");
        wordsToSkip.add("should");
        wordsToSkip.add("could");
        wordsToSkip.add("does");
        wordsToSkip.add("did");
        wordsToSkip.add("do");
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

                // First word won't have a space added before it
                boolean isFirstWord = text.getDocument().tokens().indexOf(token) == 0;

                // Retrieve the [part of speech block] to replace the word in the new madlib
                // Map above returns null if part of speech can't be madlibified
                replacementBlock = posMap.get((token.get(CoreAnnotations.PartOfSpeechAnnotation.class)));

                // disregard any words in wordsToSkip by resetting the block to null
                if (wordsToSkip.contains(token.word())) replacementBlock = null;

                if (i < skipper) {
                    justWriteWord(token, writer, isFirstWord);
                    // i only increments when the current word is madlibifiable
                    if (replacementBlock != null) i++;
                }
                // the skipper count resets after a word is madlibified
                else {
                    if (replacementBlock != null) {
                        replaceWordWithBlock(isFirstWord, writer, replacementBlock);
                        posList.add(replacementBlock);
                        i = 1;
                    }
                    else {
                        justWriteWord(token, writer, isFirstWord);
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

    // justWriteWord but handles Strings instead of tokens to print the part of speech returned by the part of speech map inside square brackets
    static void replaceWordWithBlock(boolean isFirstWord, BufferedWriter writer, String replacementBlock) throws IOException, InvalidPartOfSpeechException {
        if (!posMap.containsValue(replacementBlock)) {
            writer.write("[YouMessedUp]");
            throw new InvalidPartOfSpeechException("Passed invalid part of speech. Replacing word with [YouMessedUp]");
        }
        if (isFirstWord) {
            writer.write("[" + replacementBlock + "]");
        }
        else writer.write(" [" + replacementBlock + "]");
    }

    // Adds space before each word for simple avoidance of spaces before punctuation
    // Nothing is added to the punctuation character

    /** Helper method for removeMadlibifiable() that writes each word to a file with a preceding space*/
    static void justWriteWord(CoreLabel token, BufferedWriter writer, boolean isFirstWord) throws IOException {

        if (token.word().matches("\\p{Punct}") || isFirstWord) {
            writer.write(token.get(CoreAnnotations.TextAnnotation.class));
        }
        else writer.write(" " + token.get(CoreAnnotations.TextAnnotation.class));
    }

    public static Map<String, String> getPosMap() {
        return Collections.unmodifiableMap(posMap);
    }


}
