package userinterface;

import madlibgeneration.MadlibCreator;
import madlibgeneration.MadlibFiller;
import tagger.TextAnnotater;
import utility.exceptions.TextNotProcessedException;
import utility.filehandling.FileHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CLI {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Pattern DIGITS = Pattern.compile("[0-9]+");


    public static void initiateMadlibCreation() throws Exception {
        System.out.println("Welcome to the Madlib Machine!");
        System.out.println();

        System.out.println("Please enter filepath of .txt file or type \"quit\" to quit: ");
        String filename = SCANNER.nextLine();

        File originalText = FileHandler.getSourceTxtFile(filename);

        if (originalText == null || !originalText.exists()) {
            throw new TextNotProcessedException("txt file could not be parsed. You blew it. Exiting program.");
        }

        System.out.println("What would you like to save your blanked madlib as?");
        String blankMadlibFilename = SCANNER.nextLine() + ".txt";

        // prompts user for how many madlibifiable words will be skipped before a madlibifiable word is blanked
        String skipMadlibifiables = getMadlibifiableSkipper();

        // identifies parts of speech for each word of the given file, replaces the nth madlibifiable word with associated part of speech block, returns list of removed parts of speech
        // to prompt user to fill out replacement words in order of removal
        ArrayList<String> posList = MadlibCreator.removeMadlibifiables(new TextAnnotater(originalText), blankMadlibFilename, Integer.parseInt(skipMadlibifiables));
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
        Queue<String> userWords = MadlibFiller.getReplacementWords(posList);

        System.out.println("What would you like to save your completed madlib as?");
        String completedMadlibFilename = CLI.getScanner().nextLine() + ".txt";

        // Replaces pos text blocks with user's replacement words
        try {
            MadlibFiller.fillInMadlib(blankMadlibFilename, completedMadlibFilename, userWords);
            System.out.println("Congratulations! You did it! Whether you created a new spin on a short story or perverted your favorite bible chapter, thank you for having fun.");
        } catch (Exception e) {
            System.err.println("Madlib word replacement failed. You're stuck with the unfilled madlib until you try again");
        }

        System.out.println("Goodbye.");
    }

    private static String getMadlibifiableSkipper() {
        // skipMadlibifiables is limited to 9; 0 results in original text
        System.out.println("How many madlibifiable words would you like to ignore? The lower the number, the more words you'll need to replace");
        String skipMadlibifiables = SCANNER.nextLine();
        Matcher matcher = DIGITS.matcher(skipMadlibifiables);

        while (!matcher.matches()) {
            System.out.println("Please enter a number 0 through 9");
            skipMadlibifiables = SCANNER.nextLine();
            matcher = DIGITS.matcher(skipMadlibifiables);
        }
        return skipMadlibifiables;
    }

    public static Scanner getScanner() {
        return SCANNER;
    }

}
