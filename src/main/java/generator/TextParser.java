package generator;

import utility.exceptions.NullEntryException;
import words.DictionaryEntry;
import words.DictionaryFetcher;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

public enum TextParser {
    INSTANCE;

    FileReader reader;

    public static TextParser getInstance() {
        return INSTANCE;
    }

    //! Need to keep API calls to < 1000 per day
    public static void parseTextForEntries(String filePath) throws NullEntryException, IOException, InterruptedException {
        File inFile = new File(filePath);
        try (Scanner scanner = new Scanner(inFile)) {
            scanner.useDelimiter("[^A-Za-z-]+");
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();
                if (!word.isEmpty()) {
                    DictionaryFetcher.fetchEntry(word);
                }
            }
        }
    }


}
