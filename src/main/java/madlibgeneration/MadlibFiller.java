package madlibgeneration;

import java.io.*;
import java.util.Queue;

/**
 * Utility class that takes madlibified madlib (txt file with some words replaced by [part of speech] blocks) and fills in with user's replacement words collected from the CLI
 * @see userinterface.CLI*/

class MadlibFiller {

    /** Responsible for replacing the part of speech blocks in the blanked madlib with the list of words prompted by the CLI
     * @param blankedMadlib Text resulting from the MadlibBlanker that has a number of words replaced by text blocks displaying their associated parts of speech
     * @param replacementWords The list of words the user chose to replace each removed word (prompted by the CLI class). */

    void fillInMadlib(BufferedWriter writer, String blankedMadlib, Queue<String> replacementWords) throws IOException {

        // Blanked madlib is split into words for individual token analysis. This determines if and how a word is replaced
        String[] words = blankedMadlib.split("\\s+");

        for (String word : words) {

            // lastWord determines if there should be a space after the word. Doesn't necessarily matter except for testing.
            boolean lastWord = false;
            if (word == null) continue;

            // If the replacement word queue is empty, continue simply copying words into the file
            if (word.equals(words[words.length - 1])) lastWord = true;

            // Clear any punctuation in the word to check if it's a legitimate part of speech block in MadlibCreator posMap
            String strippedWord = word.replaceAll("[^a-zA-Z]", "");

            // Check word syntax and replace based on conditionals, or else write the word as written (it's not madlibifiable)
            if (Madlib.getPosMap().containsValue(strippedWord) && replacementWords.peek() != null) {
                replaceWord(replacementWords, Character.toString(word.charAt(word.length() - 1)), lastWord, writer);
            } else writer.write(word + " ");
        }
        System.out.println("Madlib successfully populated and saved to the src folder!");
    }

    /** Helper method for parsing and formatting words in the blanked madlib based on whether a word should be replaced or not
     * @param lastChar Determines if the last character of the word is punctuation. If it is, we need to maintain that punctuation after removing the text block and replacing the word.
     * @param lastWord If a word is the last word on a line, do not add a space at the end.*/

    private static void replaceWord(Queue<String> replacementWords, String lastChar, boolean lastWord, BufferedWriter writer) throws IOException {
        // Check last character to check against regex
        if (lastChar.matches("[.,\"!?;“”]")) {
            // Keep punctuation to append to replacement word if the word ends in punctuation
            // Don't add a space to the end if it's the last word on a line - mostly matters for testing
            if (lastWord) writer.write(replacementWords.poll() + lastChar);
            else writer.write(replacementWords.poll() + lastChar + " ");
        } else {
            writer.write(replacementWords.poll() + " ");
        }
    }
}
