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
    Path outputFile1, outputFile2, outputFile3;
    String textForFile;
    TextAnnotater annotatedText;

    @BeforeEach
    void initializeTempFiles() throws IOException {
        // Create temp directory and files
        testTextFile = tempDir.resolve("outText.txt");

        outputFile1 = tempDir.resolve("test1.txt");
        outputFile2 = tempDir.resolve("test2.txt");
        outputFile3 = tempDir.resolve("test3.txt");

        // Write sample text to compare different executions of the same file
        this.textForFile = "Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?";
        Files.writeString(testTextFile, textForFile);

        // Parse sample text to tag with parts of speech
        annotatedText = new TextAnnotater(testTextFile);

    }

    @Test
    void testFillInMadlib() throws TextNotProcessedException, IOException {

        // Reference madlib
        // "[pluralNoun], [noun]. I [verbPast] to the [noun] [noun] and [verbPast] some [noun]. Do you [verb] a [noun] [noun]?";
        String test1 = "Potatoes, cowboy. I tested to the space moon and folded some napkins. Do you carry a banana cabbage?";
        String test2 = "Potatoes, cowboy. I tested to the space moon and folded some napkins. Do you carry a [noun] [noun]?";


        // Remove all madlibifiable words from txt file for comparison
        Madlib testMadlib = new Madlib(textForFile, outputFile1.toString(), 1);

        // Normal test
        Queue<String> replacementWords1 = new ArrayDeque<>();
        replacementWords1.add("Potatoes");
        replacementWords1.add("cowboy");
        replacementWords1.add("tested");
        replacementWords1.add("space");
        replacementWords1.add("moon");
        replacementWords1.add("folded");
        replacementWords1.add("napkins");
        replacementWords1.add("carry");
        replacementWords1.add("banana");
        replacementWords1.add("cabbage");

        // Queue is two words short
        Queue<String> replacementWords2 = new ArrayDeque<>();
        replacementWords2.add("Potatoes");
        replacementWords2.add("cowboy");
        replacementWords2.add("tested");
        replacementWords2.add("space");
        replacementWords2.add("moon");
        replacementWords2.add("folded");
        replacementWords2.add("napkins");
        replacementWords2.add("carry");

        testMadlib.fillInMadlib(replacementWords1, outputFile2.toString());
        assertEquals(test1, Files.readString(outputFile2));

        testMadlib.fillInMadlib(replacementWords2, outputFile3.toString());
        assertEquals(test2, Files.readString(outputFile3));

    }

}
