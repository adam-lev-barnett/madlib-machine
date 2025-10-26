package utility.filehandling;

import java.io.File;
import java.util.Scanner;


public abstract class FileHandler {

    private static final Scanner scanner = new Scanner(System.in);


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
                filename = scanner.nextLine();
            }
        }
        return originalText;
    }

}
