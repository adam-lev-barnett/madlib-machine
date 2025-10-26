package madlibgeneration;

import java.io.*;
import java.util.Queue;

public abstract class MadlibFiller {

    /**
     * Takes madlibified madlib (txt file with words replaced by [part of speech] blocks) and fills in with replacement word Queue
     */
    public static void fillInMadlib(BufferedWriter writer, String blankedMadlib, Queue<String> replacementWords) throws IOException {

        String[] words = blankedMadlib.split("\\s+");

        for (String word : words) {

            // lastWord determines if there should be a space after the word. Doesn't necessarily matter except for testing
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

    // Helper method for parsing and formatting words in the blanked madlib based on whether a word should be replaced or not
    private static void replaceWord(Queue<String> replacementWords, String lastChar, boolean lastWord, BufferedWriter writer) throws IOException {
        // Check last character to check against regex
        if (lastChar.matches("[.,\"!?]")) {
            // Keep punctuation to append to replacement word if the word ends in punctuation
            // Don't add a space to the end if it's the last word on a line - mostly matters for testing
            if (lastWord) writer.write(replacementWords.poll() + lastChar);
            else writer.write(replacementWords.poll() + lastChar + " ");
        } else {
            writer.write(replacementWords.poll() + " ");
        }
    }
}
