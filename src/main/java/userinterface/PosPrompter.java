package userinterface;

import utility.exceptions.NullPOSListException;

import java.util.*;

public abstract class PosPrompter {

    private static final Scanner scanner = CLI.getScanner();

    // Prompts user for words for each needed part of speech; returns list used to repopulate madlib with new words
    public static Queue<String> fillInMadlib(List<String> posList) throws NullPOSListException {
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

}
