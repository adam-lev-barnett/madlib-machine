package userinterface;

import generator.Madlibifier;
import org.jetbrains.annotations.Nullable;
import tagger.TextAnnotater;
import utility.exceptions.NullPOSListException;
import utility.exceptions.TextNotProcessedException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
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

    public static void initiateMadlibCreation() throws IOException, TextNotProcessedException, NullPOSListException {
        System.out.println("Welcome to the Madlib Machine!");
        System.out.println();

        System.out.println("Please enter filepath of .txt file or type \"quit\" to quit: ");
        String filename = SCANNER.nextLine();

        File originalText = getFile(filename);

        if (originalText == null) {
            throw new TextNotProcessedException("txt file could not be parsed. You blew it. Exiting program.");
        }

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

        // identifies parts of speech for each word of the given file, replaces the nth madlibifiable word with associated part of speech block, returns list of removed parts of speech
        // to prompt user to fill out replacement words in order of removal
        ArrayList<String> posList = Madlibifier.removeMadlibifiables(new TextAnnotater(originalText), newFilename, Integer.parseInt(skipMadlibifiables));
        System.out.println();
        System.out.println("Would you like to fill in your new madlib? (yes/no) ");
        String response = SCANNER.nextLine();

        while (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
            System.out.println("Please answer yes or no");
            response = SCANNER.nextLine();
        }

        if (response.equalsIgnoreCase("no")) {
            System.out.println("Thank you for madlibbing! Enjoy, I guess!");
            return;
        }

        System.out.println("You will now be prompted to fill in a word for each provided part of speech.");
        System.out.println();

        // If the part of speech list collected from removeMadlibifiables returns null, fillInMadlib fails and closes the program as if the user responded "no" to filling in the madlib
        if (!replaceWords(posList, newFilename)) {
            System.err.println("Madlib word replacement failed. You're stuck with the unfilled madlib until you try again");
            System.out.println("Goodbye.");
            return;
        }

        System.out.println("Congratulations! You did it! Whether you created a new spin on a short story or perverted your favorite bible chapter, thank you for having fun.");
        System.out.println("Goodbye.");
    }

    private static File getFile(String filename) {
        File originalText;
        while (true) {
            if (filename.equalsIgnoreCase("quit")) return null;
            try {
                originalText = new File(filename);
                break;
            } catch (Exception e) {
                System.err.println("Invalid filepath.");
                System.out.println("Please enter filepath of .txt file or type \"quit\" to quit: ");
                filename = SCANNER.nextLine();
            }
        }
        return originalText;
    }

    // Attempts and confirms word replacement. Calls method to save file if successful
    private static boolean replaceWords(ArrayList<String> posList, String newFilename) throws NullPOSListException {
        if (posList == null) throw new NullPOSListException("Could not locate any removed parts of speech; list was null.");
        Queue<String> userWords;
        try {
            userWords = PosPrompter.fillInMadlib(posList);
            saveFilledInMadlib(newFilename, userWords);
            return true;
        } catch (Exception e) {
            System.out.println("Blanked madlib still saved. Thank you for madlibbing! Goodbye.");
            return false;
        }
    }

    // initiateMadlibCreation passes the blank madlib filename and the replacement words
    private static void saveFilledInMadlib(String newFilename, Queue<String> userWords) throws Exception {
        System.out.println("What would you like to save your completed madlib as?");
        String completeMadlibFilename = SCANNER.nextLine() + ".txt";
        try {
            Madlibifier.fillInMadlib(newFilename, completeMadlibFilename, userWords);
        } catch (Exception e) {
            throw new Exception("Couldn't populate madlib");
        }
    }

    public static Scanner getScanner() {
        return SCANNER;
    }
}
