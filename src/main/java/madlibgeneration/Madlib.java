package madlibgeneration;

import tagger.TextAnnotater;
import utility.exceptions.TextNotProcessedException;
import utility.filehandling.TextFileLoader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Madlib {

    // Converts CoreNLP tags with clearer part of speech identifiers
    private static final HashMap<String, String> posMap = new HashMap<>();

    private final MadlibBlanker blanker = new MadlibBlanker();
    private final MadlibFiller filler = new MadlibFiller();

    //CoreNLP annotates each word with a "token" that contains the word and its associated part of speech
    TextAnnotater annotatedText;

    String originalText;
    String blankedText;
    String filledText;
    ArrayList<String> posList;

    // Initialize collections for part of speech identification, removal, and replacement
    // Filters for parts of speech and words that should never be removed

    static {
        // Comment out parts of speech you don't want to blank
        posMap.put("NN", "noun");
        posMap.put("NNS", "pluralNoun");
        posMap.put("VB", "verb");
        posMap.put("VBD", "verbPast");
        // posReplacements.put("VBG", "gerund");
        posMap.put("VBZ", "verbEndingInS");
        posMap.put("JJ", "adjective");
        // posReplacements.put("JJR", "adjective ending in \"er\"");
        posMap.put("RB", "adverb");
        // posReplacements.put("RBS", "adverb ending in \"est\"");
        posMap.put("UH", "interjection");
    }

    public Madlib(String originalText, String blankedTextPath, int skipper) throws IOException, TextNotProcessedException {
        setOriginalText(originalText);
        setAnnotatedText(originalText);
        removeMadlibifiables(blankedTextPath, skipper);
        setBlankedText(blankedTextPath);
    }

    // ~~~~~ Methods for removing words from the source txt file ~~~~~

    // Removes the skipper-th word with a part of speech in the posBlocks hashset
    // int skipper determines the frequency of madlibification
    // Example: if skipper == 3, madlibify will clear every third madlibifiable word
    // returns ArrayList of parts of speech removed so user can replace the removed words when prompted by CLI
    private void removeMadlibifiables(String filepath, int skipper) throws IOException, TextNotProcessedException {
        if (filepath == null || annotatedText == null) throw new TextNotProcessedException("Text could not be processed due to null filepath or text");
        this.posList = blanker.removeMadlibifiables(filepath, annotatedText, skipper);
    }

    // ~~~~~ Filling in a blanked madlib ~~~~~

    // Public method callable by CLI
    public void fillInMadlib(Queue<String> replacementWords, String outFilename) throws IOException, TextNotProcessedException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFilename))) {
            filler.fillInMadlib(writer, blankedText, replacementWords);
            this.filledText = Files.readString(Paths.get(outFilename));
        } catch(IOException e) {
            throw new TextNotProcessedException("Could not write to file! Madlibification failed.");
        }
    }

    // ~~~~~ Getters/setters ~~~~~

    private void setOriginalText(String originalText) {
        // create empty string if original text is null
        if (originalText == null) originalText = "";
        this.originalText = originalText;
    }

    private String getOriginalText() {
        return originalText;
    }

    private void setAnnotatedText(String text) throws TextNotProcessedException {
        if (text == null) text = "";
        this.annotatedText = new TextAnnotater(text);
    }

    private void setBlankedText(String filepath) throws IOException, TextNotProcessedException {
        Path blankTextPath = Paths.get(filepath);
        this.blankedText = TextFileLoader.loadTextFile(blankTextPath);
    }

    public List<String> getPosList() {
        return Collections.unmodifiableList(this.posList);
    }

    public static Map<String, String> getPosMap() {
        return Collections.unmodifiableMap(posMap);
    }

}
