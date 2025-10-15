package generator;

import java.util.HashMap;

public abstract class PosRemover {

    private static final HashMap<String, String> posReplacements = new HashMap<>();

    // Does not include proper nouns yet
    // Idea: prompt user to review proper nouns and enter the part of speech themselves
    // Idea: enum with parts of speech instead of strings?

    static {
        posReplacements.put("NN", "noun");
        posReplacements.put("NNS", "plural noun");
        posReplacements.put("VB", "verb");
        posReplacements.put("VBD", "verb, past-tense");
        posReplacements.put("VBG", "gerund");
        posReplacements.put("VBZ", "verb ending in \"s\"");
        posReplacements.put("JJ", "adjective");
        posReplacements.put("JJR", "adjective ending in \"er\"");
        posReplacements.put("RB", "adverb");
        posReplacements.put("RBS", "adverb ending in \"est\"");
        posReplacements.put("UH", "interjection");
    }

    public static String getPosReplacementBlock(String posTag) {
        return posReplacements.getOrDefault(posTag, null);
    }

}
