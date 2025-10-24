package generator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import tagger.TextAnnotater;
import utility.exceptions.InvalidPartOfSpeechException;
import utility.exceptions.TextNotProcessedException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class MadlibifierTest {

    @TempDir
    Path tempDir;
    Path testTextFile;
    Path outputFile1, outputFile2, outputFile3, outputFile4, outputFile5, outputFile6;
    TextAnnotater annotatedText;

    @BeforeEach
    void initializeTempFiles() throws IOException {
        // Create temp directory and files
        testTextFile = tempDir.resolve("outText.txt");

        outputFile1 = tempDir.resolve("test1.txt");
        outputFile2 = tempDir.resolve("test2.txt");
        outputFile3 = tempDir.resolve("test3.txt");
        outputFile4 = tempDir.resolve("test4.txt");
        outputFile5 = tempDir.resolve("test5.txt");
        outputFile6 = tempDir.resolve("test6.txt");

        // Write sample text to compare different executions of the same file
        String textForFile = "Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?";
        Files.writeString(testTextFile, textForFile);

        // Parse sample text to tag with parts of speech
        annotatedText = new TextAnnotater(testTextFile);
    }

    @Test
    void testRemoveMadlibifiables(@TempDir Path tempDir) throws IOException, TextNotProcessedException {

        String expectedOutput1 = "[pluralNoun], [noun]. I [verbPast] to the [noun] [noun] and [verbPast] some [noun]. Do you [verb] a [noun] [noun]?";
        String expectedOutput2 = "Greetings, [noun]. I ran to the [noun] today and [verbPast] some gum. Do you [verb] a baloney [noun]?";
        String expectedOutput3 = "Greetings, person. I [verbPast] to the gym today and [verbPast] some gum. Do you want a [noun] sandwich?";
        String expectedOutput4 = "Greetings, person. I ran to the [noun] today and chewed some gum. Do you [verb] a baloney sandwich?";
        String expectedOutput5 = "Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?";
        String expectedOutput6 = "[pluralNoun], [noun]. I [verbPast] to the [noun] [noun] and [verbPast] some [noun]. Do you [verb] a [noun] [noun]?";

        // Populate pos lists to test proper return values
        ArrayList<String> allPartsOfSpeech = new ArrayList<>();
            allPartsOfSpeech.add("pluralNoun");
            allPartsOfSpeech.add("noun");
            allPartsOfSpeech.add("verbPast");
            allPartsOfSpeech.add("noun");
            allPartsOfSpeech.add("noun");
            allPartsOfSpeech.add("verbPast");
            allPartsOfSpeech.add("noun");
            allPartsOfSpeech.add("verb");
            allPartsOfSpeech.add("noun");
            allPartsOfSpeech.add("noun");

        ArrayList<String> expectedList2 = new ArrayList<>();
            expectedList2.add("noun");
            expectedList2.add("noun");
            expectedList2.add("verbPast");
            expectedList2.add("verb");
            expectedList2.add("noun");

        ArrayList<String> expectedList3 = new ArrayList<>();
            expectedList3.add("verbPast");
            expectedList3.add("verbPast");
            expectedList3.add("noun");

        ArrayList<String> expectedList4 = new ArrayList<>();
            expectedList4.add("noun");
            expectedList4.add("verb");

        // list should be empty because all madlibifiable words are skipped
        ArrayList<String> expectedList5 = new ArrayList<>();

        // Remove every madlibifiable word from textForFile, and continue skipping 1 more per test
        ArrayList<String> posList1 = Madlibifier.removeMadlibifiables(annotatedText, outputFile1.toString(), 1);
        String madlibifiedText1 = Files.readString(outputFile1);

        ArrayList<String> posList2 = Madlibifier.removeMadlibifiables(annotatedText, outputFile2.toString(), 2);
        String madlibifiedText2 = Files.readString(outputFile2);

        ArrayList<String> posList3 = Madlibifier.removeMadlibifiables(annotatedText, outputFile3.toString(), 3);
        String madlibifiedText3 = Files.readString(outputFile3);

        ArrayList<String> posList4 = Madlibifier.removeMadlibifiables(annotatedText, outputFile4.toString(), 4);
        String madlibifiedText4 = Files.readString(outputFile4);

        // Large input (above file's word count) should preserve original text
        ArrayList<String> posList5 = Madlibifier.removeMadlibifiables(annotatedText, outputFile5.toString(), 99);
        String madlibifiedText5 = Files.readString(outputFile5);

        // Skipper value below 1 should default to 1 (madlibify every word)
        ArrayList<String> posList6 = Madlibifier.removeMadlibifiables(annotatedText, outputFile6.toString(), -10);
        String madlibifiedText6 = Files.readString(outputFile6);

        assertEquals(expectedOutput1, madlibifiedText1);
        assertNotEquals("Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?", madlibifiedText1);
        assertEquals(posList1, allPartsOfSpeech);

        assertEquals(expectedOutput2, madlibifiedText2);
        assertEquals(posList2, expectedList2);

        assertEquals(expectedOutput3, madlibifiedText3);
        assertEquals(posList3, expectedList3);

        assertEquals(expectedOutput4, madlibifiedText4);
        assertEquals(posList4, expectedList4);

        assertEquals(expectedOutput5, madlibifiedText5);
        assertEquals(posList5, expectedList5);
        assertTrue(posList5.isEmpty());

        assertEquals(expectedOutput6, madlibifiedText6);
        assertEquals(posList6, allPartsOfSpeech);

        // Exception testing
        assertThrows(TextNotProcessedException.class,  () -> Madlibifier.removeMadlibifiables(null, outputFile6.toString(), 3));
        assertThrows(IOException.class,  () -> Madlibifier.removeMadlibifiables(annotatedText, null, 3));

    }

    // Helper method for removeMadlibifiables to handle spaces
    // First word and punctuation should not have spaces before or after them
    @Test
    void testJustWriteWord() throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile1.toFile()))) {
            Madlibifier.justWriteWord(annotatedText.getDocument().tokens().get(0), writer, true);
        }
        String firstWordTest1 = Files.readString(outputFile1);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile1.toFile()))) {
            Madlibifier.justWriteWord(annotatedText.getDocument().tokens().get(0), writer, false);
            Madlibifier.justWriteWord(annotatedText.getDocument().tokens().get(1), writer, false);
        }

        String firstWordTest2 = Files.readString(outputFile1);

        // First word in document should not have a leading space
        assertEquals("Greetings", firstWordTest1);

        // Second word should have a leading space
        assertEquals(" Greetings,", firstWordTest2);
    }

    @Test
    void testReplaceWordWithBlock() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile1.toFile()))) {
            Madlibifier.replaceWordWithBlock(true, writer, "noun");
        } catch (InvalidPartOfSpeechException e) {
        }
        String firstWordTest1 = Files.readString(outputFile1);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile1.toFile()))) {
            Madlibifier.replaceWordWithBlock(false, writer, "noun");
        } catch (InvalidPartOfSpeechException e) {
        }
        String firstWordTest2 = Files.readString(outputFile1);

        // Replaces word with "[YouMessedUp]" if the part of speech passed as a string is not a legitimate part of speech
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile1.toFile()))) {
            Madlibifier.replaceWordWithBlock(false, writer, "not a real part of speech");
        } catch (InvalidPartOfSpeechException e) {
        }
        String firstWordTest3 = Files.readString(outputFile1);

        assertEquals("[noun]", firstWordTest1);
        assertEquals(" [noun]", firstWordTest2);
        assertEquals("[YouMessedUp]", firstWordTest3);
    }

    @Test
    void testFillInMadlib() throws TextNotProcessedException, IOException {

        // Reference madlib
        // "[pluralNoun], [noun]. I [verbPast] to the [noun] [noun] and [verbPast] some [noun]. Do you [verb] a [noun] [noun]?";
        String test1 = "Potatoes, cowboy. I tested to the space moon and folded some napkins. Do you carry a banana cabbage?";

        // Remove all madlibifiable words from txt file
        Madlibifier.removeMadlibifiables(annotatedText, outputFile1.toString(), 1);

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

        Madlibifier.fillInMadlib(outputFile1.toString(), outputFile2.toString(), replacementWords);
        assertEquals(test1, Files.readString(outputFile2));

    }
}
