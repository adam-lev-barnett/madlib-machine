package userinterface;

import madlibgeneration.Madlib;
import utility.exceptions.NullPOSListException;
import utility.exceptions.TextNotProcessedException;
import utility.filehandling.TextFileLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CLI {
    INSTANCE;

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Pattern DIGITS = Pattern.compile("[0-9]+");

    public static CLI getInstance() {
        return INSTANCE;
    }

    public static void initiateMadlibCreation() throws Exception {
        System.out.println("Welcome to the Madlib Machine!");
        System.out.println();

        System.out.println("Please enter filepath of .txt file or type \"quit\" to quit: ");
        String filename = SCANNER.nextLine();

        String originalText = TextFileLoader.loadTextFile((getSourceTxtFile(filename)));

        System.out.println("What would you like to save your blanked madlib as?");
        String blankMadlibFilename = SCANNER.nextLine() + ".txt";

        // prompts user for how many madlibifiable words will be skipped before a madlibifiable word is blanked
        int skipper = Integer.parseInt(getMadlibifiableSkipper());

        Madlib madlib = new Madlib(originalText, blankMadlibFilename, skipper);

        // identifies parts of speech for each word of the given file, replaces the nth madlibifiable word with associated part of speech block, returns list of removed parts of speech
        // to prompt user to fill out replacement words in order of removal
        System.out.println();

        //~ Queries whether or not the user wants to fill in the madlib; exits if no
        if (!queryFillInMadlib()) return;

        System.out.println("What would you like to save your completed madlib as?");
        String completedMadlibFilename = CLI.getScanner().nextLine() + ".txt";

        System.out.println("You will now be prompted to fill in a word for each provided part of speech.");
        System.out.println();
        // userWords are passed to the MadlibFiller to replace pos blocks in order of entry
        Queue<String> userWords = getReplacementWords(madlib.getPosList());

        try {
            madlib.fillInMadlib(userWords, completedMadlibFilename);
            System.out.println("Congratulations! You did it! Whether you created a new spin on a short story or perverted your favorite bible chapter, thank you for having fun.");
        } catch (IOException e) {
            System.err.println("Madlib word replacement failed. You're stuck with the unfilled madlib until you try again");
        }

        System.out.println("Goodbye.");
    }

    private static boolean queryFillInMadlib() {
        System.out.println("Would you like to fill in your new madlib? (yes/no) ");
        String response = SCANNER.nextLine();

        while (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
            System.out.println("Please answer yes or no");
            response = SCANNER.nextLine();
        }

        if (response.equalsIgnoreCase("no")) {
            System.out.println("Thank you for madlibbing! Enjoy, I guess!");
            return false;
        }
        return true;
    }

    private static String getMadlibifiableSkipper() {
        // if skipMadlibifiables is higher than the total madlibifiable word count, the text will be unchanged; 0 results in original text
        System.out.println("How many madlibifiable words would you like to ignore? The lower the number, the more words you'll need to replace");
        String skipMadlibifiables = SCANNER.nextLine();
        Matcher matcher = DIGITS.matcher(skipMadlibifiables);

        while (!matcher.matches()) {
            System.out.println("Please enter a number.");
            skipMadlibifiables = SCANNER.nextLine();
            matcher = DIGITS.matcher(skipMadlibifiables);
        }
        return skipMadlibifiables;
    }

    public static Scanner getScanner() {
        return SCANNER;
    }

    // Prompts user for words for each needed part of speech; returns list used to repopulate madlib with new words
    private static Queue<String> getReplacementWords(List<String> posList) throws NullPOSListException {

        // Convert null list to empty ArrayList
        if (posList == null) {
            throw new NullPOSListException("No POS list found. Blanked words will not be replaced, and program will exit");
        }
        Queue<String> wordList = new ArrayDeque<>();
        System.out.println("Please enter a word for each of the following parts of speech:");
        for (String pos : posList) {
            System.out.println(pos + ": ");
            if (pos != null) wordList.add(SCANNER.nextLine());
        }
        return wordList;
    }

    private static Path getSourceTxtFile(String filename) {
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
        return originalText.toPath();
    }
}
