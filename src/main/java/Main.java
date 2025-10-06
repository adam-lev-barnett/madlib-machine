import utility.exceptions.NullEntryException;
import words.DictionaryFetcher;
import words.DictionaryEntries;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, NullEntryException {
        DictionaryFetcher fetcher = new DictionaryFetcher();
        DictionaryEntries entries = DictionaryEntries.getInstance();

        fetcher.fetchEntry("love");
        fetcher.fetchEntry("poop");
        fetcher.fetchEntry("read");

        System.out.println(DictionaryEntries.getInstance().getEntryMap());

    }
}
