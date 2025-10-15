package userinterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class PosPrompter {

    private static final Scanner scanner = userController.getScanner();

    // Prompts user for words for each needed part of speech; returns list used to repopulate madlib with new words
    public static List<String> fillInMadlib(List<String> posList) {
        System.out.println("Please enter a word for each of the following parts of speech:");
        List<String> wordList = new ArrayList<>();
        for (String pos : posList) {
            System.out.println(pos + ": ");
            wordList.add(scanner.nextLine());
        }
        return wordList;
    }

}
