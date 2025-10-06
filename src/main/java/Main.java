import generator.TextParser;
import utility.exceptions.NullEntryException;
import words.DictionaryFetcher;
import words.DictionaryEntries;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, NullEntryException {
        TextParser.parseTextForEntries("/Users/adambarnett/Coding/MadlibMachine/madlib-machine/test1.txt");

        System.out.println(DictionaryEntries.getInstance());

    }
}
