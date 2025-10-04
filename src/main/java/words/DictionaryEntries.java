package words;

import java.util.*;

// Singleton list of all processed words

public final class DictionaryEntries {

    private static final Map<String, List<DictionaryEntry>> entryMap = new HashMap<>();

    private DictionaryEntries() {}

    public Map<String, List<DictionaryEntry>> getEntryMap() {
        return Collections.unmodifiableMap(entryMap);
    }

    void addEntries(List<DictionaryEntry> entries) {
        if (entries == null) {
            System.err.println("Entry list cannot be null");
            return;
        }
        for (DictionaryEntry entry : entries) {
            if (entry == null) continue;
            if (entryMap.containsKey(entry.getWord())) {
                entryMap.get(entry.getWord()).add(entry);
            }
            else {
                entryMap.put(entry.getWord(), new ArrayList<>());
            }

        }
        System.out.println(entryMap);
    }

//    void addEntry(DictionaryEntry entry) {
//        if (entry == null) return;
//        entryMap.put(entry.getWord(), entry);
//    }

    public static DictionaryEntries getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DictionaryEntries INSTANCE = new DictionaryEntries();
    }




}
