package madlibgeneration;

import utility.exceptions.NullPOSListException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public abstract class MadlibFiller {

    private static final Scanner scanner = new Scanner(System.in);

    // Prompts user for words for each needed part of speech; returns list used to repopulate madlib with new words
    public static Queue<String> getReplacementWords(List<String> posList) throws NullPOSListException {

        // Convert null list to empty ArrayList
        if (posList == null) {
            throw new NullPOSListException("No POS list found. Blanked words will not be replaced, and program will exit");
        }
        Queue<String> wordList = new ArrayDeque<>();
        System.out.println("Please enter a word for each of the following parts of speech:");
        for (String pos : posList) {
            System.out.println(pos + ": ");
            if (pos != null) wordList.add(scanner.nextLine());
        }
        return wordList;
    }

    /** Takes madlibified madlib (txt file with words replaced by [part of speech] blocks) and fills in with replacement word Queue*/
    public static void fillInMadlib(String inFilepath, String outFilepath, Queue<String> replacementWords) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(inFilepath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFilepath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {

                    // lastWord determines if there should be a space after the word. Doesn't necessarily matter except for testing
                    boolean lastWord = false;
                    if (word == null) continue;

                    // If the replacement word queue is empty, continue simply copying words into the file
                    if (word.equals(words[words.length - 1])) lastWord = true;

                    // Clear any punctuation in the word to check if it's a legitimate part of speech block in MadlibCreator posMap
                    String strippedWord = word.replaceAll("[^a-zA-Z]", "");

                    // Check word syntax and replace based on conditionals, or else write the word as written (it's not madlibifiable)
                    if (MadlibCreator.getPosMap().containsValue(strippedWord) && replacementWords.peek() != null) {
                        replaceWord(replacementWords, Character.toString(word.charAt(word.length() - 1)), lastWord, writer);
                    }

                    else writer.write(word + " ");
                }
            }
            System.out.println("Madlib successfully populated and saved to the src folder!");
        }
        catch(IOException e) {
            throw new IOException("Could not fill in madlib because input or output path is invalid.");
        }
    }

//    public static void fillInWholeMadlib(String inFilepath, String outFilepath, Queue<String> replacementWords) throws IOException {
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFilepath))) {
//
//            Path unfilledMadlib = Paths.get(inFilepath);
//            String unfilledText = Files.readString(unfilledMadlib);
//
//            String[] words = unfilledText.split("\\s+");
//            for (String word : words) {
//
//            }
//
//
//            while ((line = reader.readLine()) != null) {
//                String[] words = line.split("\\s+");
//                for (String word : words) {
//
//                    // lastWord determines if there should be a space after the word. Doesn't necessarily matter except for testing
//                    boolean lastWord = false;
//                    if (word == null) continue;
//
//                    // If the replacement word queue is empty, continue simply copying words into the file
//                    if (word.equals(words[words.length - 1])) lastWord = true;
//
//                    // Clear any punctuation in the word to check if it's a legitimate part of speech block in MadlibCreator posMap
//                    String strippedWord = word.replaceAll("[^a-zA-Z]", "");
//
//                    // Check word syntax and replace based on conditionals, or else write the word as written (it's not madlibifiable)
//                    if (MadlibCreator.getPosMap().containsValue(strippedWord) && replacementWords.peek() != null) {
//                        replaceWord(replacementWords, Character.toString(word.charAt(word.length() - 1)), lastWord, writer);
//                    }
//
//                    else writer.write(word + " ");
//                }
//            }
//            System.out.println("Madlib successfully populated and saved to the src folder!");
//        }
//        catch(IOException e) {
//            throw new IOException("Could not fill in madlib because input or output path is invalid.");
//        }
//    }

    // Helper method for parsing and formatting words in the blanked madlib based on whether a word should be replaced or not
    private static void replaceWord(Queue<String> replacementWords, String lastChar, boolean lastWord, BufferedWriter writer) throws IOException {
        // Check last character to check against regex
        if (lastChar.matches("[.,\"!?]")) {
            // Keep punctuation to append to replacement word if the word ends in punctuation
            // Don't add a space to the end if it's the last word on a line - mostly matters for testing
            if (lastWord) writer.write(replacementWords.poll() + lastChar);
            else writer.write(replacementWords.poll() + lastChar + " ");
        }
        else {
            writer.write(replacementWords.poll() + " ");
        }
    }


}
