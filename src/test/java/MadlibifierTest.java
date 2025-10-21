import generator.Madlibifier;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import tagger.TextAnnotater;
import utility.exceptions.TextNotProcessedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MadlibifierTest {

    @Test
    void testRemoveMadlibifiables(@TempDir Path tempDir) throws IOException, TextNotProcessedException {

        //TODO remove opening space
        String expectedOutput1 = " [pluralNoun], [noun]. I [verbPast] to the [noun] [noun] and [verbPast] some [noun]. Do you [verb] a [noun] [noun]?";
        String expectedOutput2 = " Greetings, [noun]. I ran to the [noun] today and [verbPast] some gum. Do you [verb] a baloney [noun]?";
        String expectedOutput3 = " Greetings, person. I [verbPast] to the gym today and [verbPast] some gum. Do you want a [noun] sandwich?";
        String expectedOutput4 = " Greetings, person. I ran to the [noun] today and chewed some gum. Do you [verb] a baloney sandwich?";

        // Large input should preserve original text
        String expectedOutput5 = " Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?";


        // Create temp directory and file to store text to be madlibified
        Path testTextFile = tempDir.resolve("outText.txt");

        // Test outputs for each skipper interval
        Path outputFile1 = tempDir.resolve("test1.txt");
        Path outputFile2 = tempDir.resolve("test2.txt");
        Path outputFile3 = tempDir.resolve("test3.txt");
        Path outputFile4 = tempDir.resolve("test4.txt");
        Path outputFile5 = tempDir.resolve("test5.txt");

        String textForFile = "Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?";
        Files.writeString(testTextFile, textForFile);

        // Convert text file to TextAnnotator object so CoreNLP can identify parts of speech and tag each word in the file with the POS (CoreNLP refers to it as annotating)
        TextAnnotater annotatedText = new TextAnnotater(testTextFile);

        // Remove every madlibifiable word from textForFile, and continue skipping 1 more per test
        Madlibifier.removeMadlibifiables(annotatedText, outputFile1.toString(), 1);
        String madlibifiedText1 = Files.readString(outputFile1);

        Madlibifier.removeMadlibifiables(annotatedText, outputFile2.toString(), 2);
        String madlibifiedText2 = Files.readString(outputFile2);

        Madlibifier.removeMadlibifiables(annotatedText, outputFile3.toString(), 3);
        String madlibifiedText3 = Files.readString(outputFile3);

        Madlibifier.removeMadlibifiables(annotatedText, outputFile4.toString(), 4);
        String madlibifiedText4 = Files.readString(outputFile4);

        Madlibifier.removeMadlibifiables(annotatedText, outputFile5.toString(), 99);
        String madlibifiedText5 = Files.readString(outputFile5);

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

    }
}
