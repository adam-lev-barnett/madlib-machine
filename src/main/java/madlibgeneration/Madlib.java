package madlibgeneration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
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

/** The core object that contains the source text file from which its MadlibBlanker and MadlibFiller can blank words in the document and fill in the blanks with user-determined words
 * @see MadlibBlanker
 * @see MadlibFiller
 * @author Adam Barnett */

public class Madlib {

    /** Used to pair the annotation identifiers from CoreNLP to clearer parts of speech. Example: CoreNLP tags nouns as "NN," so the program relays "NN" as "noun" to the rest of the program. */
    private static final HashMap<String, String> posMap = new HashMap<>();

    /** Utility class used to remove words from the source file and produce a new file with the blanked madlib */
    private final MadlibBlanker blanker = new MadlibBlanker();

    /** Utility class to fill in the blanked madlib with user-chosen words and write it to a new file. This is referred to as a filled madlib. */
    private final MadlibFiller filler = new MadlibFiller();

    /** Converts CoreNLP tags with clearer part of speech identifiers. CoreNLP is a library that, in this program,
     * is used to annotate each word with a "token" that contains the word and its associated part of speech */
    TextAnnotater annotatedText;

    /** Source text for madlib creation */
    String originalText;

    /** The original text with certain words deleted and replaced with text blocks containing their associated parts of speech */
    String blankedText;

    /** Produced from the blankedText using user-prompted replacement words to replace the deleted words / part of speech blocks */
    String filledText;

    /** The parts of speech associated with the removed words during the blankingn process. The CLI and MadlibFiller use these to prompt user replacement words and fill in the madlib */
    List<String> posList;

    // Initialize collections for part of speech identification, removal, and replacement

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

    /** Removes the skipper-th madlibifiable word with a part of speech in the posBlocks hashset. Madlibifiable word are tagged with parts of speech included in the posMap.
       Assigns the instance's posList returned by the helper method.
       @param skipper determines the frequency of madlib blanking (madlibification). Example: if skipper == 3, the method will clear every third madlibifiable word. */
    private void removeMadlibifiables(String filepath, int skipper) throws IOException, TextNotProcessedException {
        if (filepath == null || annotatedText == null) throw new TextNotProcessedException("Text could not be processed due to null filepath or text");
        this.posList = blanker.removeMadlibifiables(filepath, annotatedText, skipper);
    }

    /** Primarily called by CLI to create a filled-in Madlib. Calls the instance's MadlibFiller to actually create and write to the file. Public method callable by CLI. */
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

    private void setAnnotatedText(String text) {
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

    public static @NotNull @UnmodifiableView Map<String, String> getPosMap() {
        return Collections.unmodifiableMap(posMap);
    }

}
