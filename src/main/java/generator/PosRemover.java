package generator;

import java.util.HashMap;

public abstract class PosRemover {

    // Takes annotated part of speech and replaces the parts of speech included in the map with blocks formatted like "[noun]"

    private static final HashMap<String, String> posReplacements = new HashMap<>();

    // Does not include proper nouns yet
    // Idea: prompt user to review proper nouns and enter the part of speech themselves
    // Idea: enum with parts of speech instead of strings?

    static {
        posReplacements.put("NN", "noun");
        posReplacements.put("NNS", "pluralNoun");
        posReplacements.put("VB", "verb");
        posReplacements.put("VBD", "verbPast");
        // posReplacements.put("VBG", "gerund");
        posReplacements.put("VBZ", "verbEndingInS");
        posReplacements.put("JJ", "adjective");
        // posReplacements.put("JJR", "adjective ending in \"er\"");
        posReplacements.put("RB", "adverb");
        // posReplacements.put("RBS", "adverb ending in \"est\"");
        posReplacements.put("UH", "interjection");
    }

    public static String getPosReplacementBlock(String posTag) {
        return posReplacements.getOrDefault(posTag, null);
    }

}
