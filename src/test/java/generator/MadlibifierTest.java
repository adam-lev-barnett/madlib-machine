package generator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import tagger.TextAnnotater;
import utility.exceptions.TextNotProcessedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

        // Write sample text
        String textForFile = "Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?";
        Files.writeString(testTextFile, textForFile);

        // Create the annotated text
        annotatedText = new TextAnnotater(testTextFile);

    }

    @Test
    void testRemoveMadlibifiables(@TempDir Path tempDir) throws IOException, TextNotProcessedException {

        //TODO remove opening space
        String expectedOutput1 = "[pluralNoun], [noun]. I [verbPast] to the [noun] [noun] and [verbPast] some [noun]. Do you [verb] a [noun] [noun]?";
        String expectedOutput2 = "Greetings, [noun]. I ran to the [noun] today and [verbPast] some gum. Do you [verb] a baloney [noun]?";
        String expectedOutput3 = "Greetings, person. I [verbPast] to the gym today and [verbPast] some gum. Do you want a [noun] sandwich?";
        String expectedOutput4 = "Greetings, person. I ran to the [noun] today and chewed some gum. Do you [verb] a baloney sandwich?";
        String expectedOutput5 = "Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?";
        String expectedOutput6 = "[pluralNoun], [noun]. I [verbPast] to the [noun] [noun] and [verbPast] some [noun]. Do you [verb] a [noun] [noun]?";

        // Remove every madlibifiable word from textForFile, and continue skipping 1 more per test
        Madlibifier.removeMadlibifiables(annotatedText, outputFile1.toString(), 1);
        String madlibifiedText1 = Files.readString(outputFile1);

        Madlibifier.removeMadlibifiables(annotatedText, outputFile2.toString(), 2);
        String madlibifiedText2 = Files.readString(outputFile2);

        Madlibifier.removeMadlibifiables(annotatedText, outputFile3.toString(), 3);
        String madlibifiedText3 = Files.readString(outputFile3);

        Madlibifier.removeMadlibifiables(annotatedText, outputFile4.toString(), 4);
        String madlibifiedText4 = Files.readString(outputFile4);

        // Large input (above file's word count) should preserve original text
        Madlibifier.removeMadlibifiables(annotatedText, outputFile5.toString(), 99);
        String madlibifiedText5 = Files.readString(outputFile5);

        // Skipper value below 1 should default to 1 (madlibify every word)
        Madlibifier.removeMadlibifiables(annotatedText, outputFile6.toString(), -10);
        String madlibifiedText6 = Files.readString(outputFile6);

        assertEquals(expectedOutput1, madlibifiedText1);
        assertNotEquals("Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?", madlibifiedText1);
        System.out.println("Skip 1 passed");

        assertEquals(expectedOutput2, madlibifiedText2);
        System.out.println("Skip 2 passed");

        assertEquals(expectedOutput3, madlibifiedText3);
        System.out.println("Skip 3 passed");

        assertEquals(expectedOutput4, madlibifiedText4);
        System.out.println("Skip 4 passed");

        assertEquals(expectedOutput5, madlibifiedText5);
        System.out.println("Big skip passed");

        assertEquals(expectedOutput6, madlibifiedText6);
        System.out.println("Skip default passed");

    }
}
