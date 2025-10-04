import words.DictionaryFetcher;
import words.DictionaryEntries;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        DictionaryFetcher fetcher = new DictionaryFetcher();
        DictionaryEntries entries = DictionaryEntries.getInstance();

        fetcher.fetchEntry("love");

    }
}
