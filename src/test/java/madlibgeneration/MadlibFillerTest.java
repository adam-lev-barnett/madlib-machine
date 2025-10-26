package madlibgeneration;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import tagger.TextAnnotater;
import utility.exceptions.TextNotProcessedException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Queue;

public class MadlibFillerTest {

    @TempDir
    Path tempDir;
    Path testTextFile;
    Path outputFile1, outputFile2;
    TextAnnotater annotatedText;

    @BeforeEach
    void initializeTempFiles() throws IOException {
        // Create temp directory and files
        testTextFile = tempDir.resolve("outText.txt");

        outputFile1 = tempDir.resolve("test1.txt");
        outputFile2 = tempDir.resolve("test2.txt");

        // Write sample text to compare different executions of the same file
        String textForFile = "Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?";
        Files.writeString(testTextFile, textForFile);

        // Parse sample text to tag with parts of speech
        annotatedText = new TextAnnotater(testTextFile);
    }

    @Test
    void testFillInMadlib() throws TextNotProcessedException, IOException {

        // Reference madlib
        // "[pluralNoun], [noun]. I [verbPast] to the [noun] [noun] and [verbPast] some [noun]. Do you [verb] a [noun] [noun]?";
        String test1 = "Potatoes, cowboy. I tested to the space moon and folded some napkins. Do you carry a banana cabbage?";

        // Remove all madlibifiable words from txt file
        MadlibCreator.removeMadlibifiables(annotatedText, outputFile1.toString(), 1);

        Queue<String> replacementWords = new ArrayDeque<>();
        replacementWords.add("Potatoes");
        replacementWords.add("cowboy");
        replacementWords.add("tested");
        replacementWords.add("space");
        replacementWords.add("moon");
        replacementWords.add("folded");
        replacementWords.add("napkins");
        replacementWords.add("carry");
        replacementWords.add("banana");
        replacementWords.add("cabbage");

        MadlibFiller.fillInMadlib(outputFile1.toString(), outputFile2.toString(), replacementWords);
        assertEquals(test1, Files.readString(outputFile2));

    }

}
