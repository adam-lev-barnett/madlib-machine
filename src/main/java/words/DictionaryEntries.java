package words;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import utility.exceptions.NullEntryException;

import java.io.File;
import java.io.IOException;
import java.util.*;

// Singleton list of all processed words

public final class DictionaryEntries {

    // Map of words and potential parts of speech for all uses of that word
    // Example: Love - [noun, verb]
    private static final Map<String, HashSet<String>> entryMap = new HashMap<>();

    private static final ObjectMapper mapper = new ObjectMapper();

    private DictionaryEntries() {}

    public Map<String, HashSet<String>> getEntryMap() {
        return Collections.unmodifiableMap(entryMap);
    }

    void addEntries(String word, Set<String> partsOfSpeech) throws NullEntryException, IOException {
        if (word == null || partsOfSpeech == null) {
            System.err.println("Entry list cannot be null");
            return;
        }
        if (!entryMap.containsKey(word)) {
            entryMap.put(word, new HashSet<>());
        }
        for (String speechPart : partsOfSpeech) {
            if (speechPart == null) continue;
            getEntryPartsOfSpeech(word).add(speechPart);
        }
        System.out.println(word + ": " + entryMap.get(word));
        this.saveEntries();
    }

    // Cleans up message chain to access partsOfSpeech ArrayList in entries
    public static HashSet<String> getEntryPartsOfSpeech(String word) throws NullEntryException {
        if (word == null) throw new NullEntryException("Word cannot be null");
        return entryMap.get(word.toLowerCase());
    }

    public static DictionaryEntries getInstance() {
        return Holder.INSTANCE;
    }

    public void saveEntries() throws IOException {
        mapper.writeValue(new File("entries.json"), entryMap);
    }

    public void loadEntries() throws IOException {
        Map<String, HashSet<String>> mapFromFile = mapper.readValue(new File("entries.json"), new TypeReference<Map<String, HashSet<String>>>() {});
        entryMap.putAll(mapFromFile);
    }

    // Prevent Singleton instantiation until called
    private static class Holder {
        private static final DictionaryEntries INSTANCE = new DictionaryEntries();
    }




}
