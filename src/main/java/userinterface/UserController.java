package userinterface;

import generator.Madlibifier;
import tagger.TextAnnotater;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum UserController {
    INSTANCE;

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Pattern DIGITS = Pattern.compile("[0-9]+");

    // Prompts user to enter missing parts of speech to populate madlib
    public void promptPOS() {

    }

    public static void initiateMadlibCreation() throws IOException {
        System.out.println("Welcome to the Madlib Machine!");
        System.out.println();

        System.out.println("Please enter filepath of .txt file: ");
        String filename = SCANNER.nextLine();
        File originalText = new File(filename);
        // identifies parts of speech for each word of the given file
        TextAnnotater annotater = new TextAnnotater(originalText);

        System.out.println("What would you like to save your new madlib as?");
        String newFilename = SCANNER.nextLine() + ".txt";

        System.out.println("How many madlibifiable words would you like to ignore? The lower the number, the more words you'll need to replace");

        // while parsing the desired text, skipMadlibifiables represents how many madlibifiable words will be skipped before a madlibifiable word is blanked
        // skipMadlibifiables is limited to 9; 0 results in original text
        String skipMadlibifiables = SCANNER.nextLine();
        Matcher matcher = DIGITS.matcher(skipMadlibifiables);

        while (!matcher.matches()) {
            System.out.println("Please enter a number 0 through 9");
            skipMadlibifiables = SCANNER.nextLine();
            matcher = DIGITS.matcher(skipMadlibifiables);
        }

        ArrayList<String> posList = Madlibifier.removeMadlibifiables(new TextAnnotater(originalText), newFilename, Integer.parseInt(skipMadlibifiables));
        System.out.println("Madlib skeleton successfully generated in src folder");
        System.out.println();
        System.out.println("Would you like to fill in your new madlib? (yes/no) ");
        String response = SCANNER.nextLine();

        while (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
            System.out.println("Please answer yes or no");
            response = SCANNER.nextLine();
        }

        if (response.equalsIgnoreCase("no")) {
            System.out.println("Thank you for madlibbing!");
            return;
        }

        System.out.println("You will now be prompted to fill in a word for each provided part of speech.");
        System.out.println();

        Queue<String> userWords = PosPrompter.fillInMadlib(posList);

        while (true) {
            System.out.println("What would you like to save your completed madlib as?");
            String completeMadlibFilename = SCANNER.nextLine() + ".txt";
            try {
                Madlibifier.fillInMadlib(newFilename, completeMadlibFilename, userWords);
                break;
            } catch (Exception e) {
                System.out.println("Please try a new filename");
            }
        }
        System.out.println("Congratulations! You did it! Whether you created a new spin on a short story or perverted your favorite bible chapter, thank you for having fun.");
        System.out.println("Goodbye.");
    }

    public static Scanner getScanner() {
        return SCANNER;
    }
}
