package madlibgeneration;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import tagger.TextAnnotater;
import utility.exceptions.InvalidPartOfSpeechException;
import utility.exceptions.TextNotProcessedException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MadlibCreatorTest {

    @TempDir
    Path tempDir;
    Path testTextFile;
    Path outputFile1, outputFile2, outputFile3, outputFile4, outputFile5, outputFile6;
    String textForFile;
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
        this.textForFile = "Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?";
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
        ArrayList<String> allPartsOfSpeech = populatePartsofSpeech();

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

        Madlib testMadlib1 = new Madlib(textForFile, outputFile1.toString(), 1);
        Madlib testMadlib2 = new Madlib(textForFile, outputFile2.toString(), 2);
        Madlib testMadlib3 = new Madlib(textForFile, outputFile3.toString(), 3);
        Madlib testMadlib4 = new Madlib(textForFile, outputFile4.toString(), 4);
        Madlib testMadlib5 = new Madlib(textForFile, outputFile5.toString(), 99);
        Madlib testMadlib6 = new Madlib(textForFile, outputFile6.toString(), -10);


        List<String> posList1 = testMadlib1.getPosList();
        String madlibifiedText1 = Files.readString(outputFile1);

        List<String> posList2 = testMadlib2.getPosList();
        String madlibifiedText2 = Files.readString(outputFile2);

        List<String> posList3 = testMadlib3.getPosList();
        String madlibifiedText3 = Files.readString(outputFile3);

        List<String> posList4 = testMadlib4.getPosList();
        String madlibifiedText4 = Files.readString(outputFile4);

        // Large input (above file's word count) should preserve original text
        List<String> posList5 = testMadlib5.getPosList();
        String madlibifiedText5 = Files.readString(outputFile5);

        // Skipper value below 1 should default to 1 (madlibify every word)
        List<String> posList6 = testMadlib6.getPosList();
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
        assertThrows(IOException.class,  () -> new Madlib("testing madlib exception", null, 3));

    }

    private static @NotNull ArrayList<String> populatePartsofSpeech() {
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
        return allPartsOfSpeech;
    }


}
