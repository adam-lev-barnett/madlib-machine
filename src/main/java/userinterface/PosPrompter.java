package userinterface;

import java.util.*;

public abstract class PosPrompter {

    private static final Scanner scanner = UserController.getScanner();

    // Prompts user for words for each needed part of speech; returns list used to repopulate madlib with new words
    public static Queue<String> fillInMadlib(List<String> posList) {
        System.out.println("Please enter a word for each of the following parts of speech:");
        Queue<String> wordList = new ArrayDeque<>();
        for (String pos : posList) {
            System.out.println(pos + ": ");
            if (pos != null) wordList.add(scanner.nextLine());
        }
        return wordList;
    }

}
