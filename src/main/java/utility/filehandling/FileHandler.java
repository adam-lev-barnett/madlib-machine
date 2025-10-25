package utility.filehandling;

import madlibgeneration.MadlibFiller;
import userinterface.CLI;

import java.io.File;
import java.util.Queue;

public abstract class FileHandler {

    public static File getSourceTxtFile(String filename) {
        File originalText;
        while (true) {
            if (filename.equalsIgnoreCase("quit")) return null;
            try {
                originalText = new File(filename);
                break;
            } catch (Exception e) {
                System.err.println("Invalid filepath.");
                System.out.println("Please enter filepath of .txt file or type \"quit\" to quit: ");
                filename = CLI.getScanner().nextLine();
            }
        }
        return originalText;
    }

}
