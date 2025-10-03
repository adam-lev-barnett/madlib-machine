package words;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DictionaryEntries {

    private static final Map<String, DictionaryEntry> entryMap = new HashMap<>();

    public Map<String, DictionaryEntry> getEntryMap() {
        return Collections.unmodifiableMap(entryMap);
    }

    void addEntries(List<DictionaryEntry> entries) {
        if (entries == null) {
            System.err.println("Entry list cannot be null");
            return;
        }
        for (DictionaryEntry entry : entries) {
            if (entry == null) continue;
            entryMap.put(entry.getWord(), entry);
        }
    }

    void addEntry(DictionaryEntry entry) {
        if (entry == null) return;
        entryMap.put(entry.getWord(), entry);
    }


}
