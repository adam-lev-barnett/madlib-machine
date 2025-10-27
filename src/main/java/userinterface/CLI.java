package userinterface;

import madlibgeneration.Madlib;
import utility.exceptions.NullPOSListException;
import utility.exceptions.TextNotProcessedException;
import utility.filehandling.TextFileLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Singleton command-line interface controller responsible for parsing user input to create blanked and filled-in madlibs.
 * Enum singleton to maintain thread safety, readability, and serialization.
 * @see Madlib
 * @author Adam Barnett */
public enum CLI {
    INSTANCE;

    private static final Scanner SCANNER = new Scanner(System.in);

    /** Parsing logic to ensure user is inputting valid numbers when prompted */
    private static final Pattern DIGITS = Pattern.compile("[0-9]+");

    /** Used by Main to begin program logic*/
    public static CLI getInstance() {
        return INSTANCE;
    }

    /** Main controller logic that facilitates the madlib creation process in steps by requesting filenames, calling helper methods, and confirming successful text processing*/

    public void initiateMadlibCreation() throws NullPointerException, TextNotProcessedException, IOException, NullPOSListException {
        System.out.println("Welcome to the Madlib Machine!");
        System.out.println();

        System.out.println("Please enter filepath of .txt file or type \"quit\" to quit: ");
        String filename = SCANNER.nextLine();

        String originalText = TextFileLoader.loadTextFile((getSourceTxtFile(filename)));

        System.out.println("What would you like to save your blanked madlib as?");
        String blankMadlibFilename = SCANNER.nextLine() + ".txt";

        /*  skipper variable: prompts user for how many madlibifiable words will be skipped before a madlibifiable word is blanked
         *  Madlibifiable words are words with parts of speech accepted by the Madlib Machine (nouns, adjectives, etc.).
         *  See Madlib class for list of accepted parts of speech as well as madlibifiable words to be excluded altogether
         */

        int skipper = Integer.parseInt(getMadlibifiableSkipper());

        /* Instantiation automatically blanks the madlib (annotates the source file, replaces the madlibifiable words not skipped with appropriate [part of speech blocks])
           Madlib instance is saved to call its methods if the user intends to fill the Madlib in
         */

        Madlib madlib = new Madlib(originalText, blankMadlibFilename, skipper);

        System.out.println();

        //~ Queries if the user wants to fill in the madlib; exits if no
        if (!queryFillInMadlib()) return;

        System.out.println("What would you like to save your completed madlib as?");
        String completedMadlibFilename = SCANNER.nextLine() + ".txt";

        System.out.println("You will now be prompted to fill in a word for each provided part of speech.");
        System.out.println();

        // Prompts the user with parts of speech for the purpose of obtaining a queue of words used to replace the speech blocks in the blanked Madlib
        Queue<String> userWords = getReplacementWords(madlib.getPosList());

        try {
            madlib.fillInMadlib(userWords, completedMadlibFilename);
            System.out.println("Congratulations! You did it! Whether you created a new spin on a short story or perverted your favorite bible chapter, thank you for having fun.");
        } catch (IOException e) {
            System.err.println("Madlib word replacement failed. You're stuck with the unfilled madlib until you try again");
        }

        System.out.println("Goodbye.");
    }

    /** Parsing logic to obtain a valid filepath for the text to be madlibified*/
    private Path getSourceTxtFile(String filepath) {
        File originalText;
        while (true) {
            if (filepath.equalsIgnoreCase("quit")) return null;
            try {
                originalText = new File(filepath);
                break;
            } catch (NullPointerException e) {
                System.err.println("Invalid filepath.");
                System.out.println("Please enter filepath of .txt file or type \"quit\" to quit: ");
                filepath = SCANNER.nextLine();
            }
        }
        return originalText.toPath();
    }

    /** Helper method to ensure user is entering a proper numerical value for the madlibifiable skipper variable*/
    private String getMadlibifiableSkipper() {
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

    /** Helper method that determines if CLI should prompt the user for parts of speech or end the program.
     *  If user decides to enter parts of speech, the CLI will call getReplacementWords. Otherwise, it ends the program.
     * */
    private boolean queryFillInMadlib() {
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
    /** Prompt user to enter words one at a time based on the parts of speech of the words removed during madlib blanking
     * @param posList CLI iterates over the parts of speech in order so that the user can enter replacement words in order of when they appear in the text
     * @return Returns the list of words in a queue for cleaner replacement when filling in the madlib; no indexing required,
     * and constant time removal better handles errors involved in case the list empties before madlib filling is complete.
     * */
    private Queue<String> getReplacementWords(List<String> posList) throws NullPOSListException {

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

}
