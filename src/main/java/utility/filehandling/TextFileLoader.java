package utility.filehandling;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import utility.exceptions.NullPathException;
import utility.exceptions.TextNotProcessedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Utility class that operates file loading from other classes to return the text that will be operated on */
public abstract class TextFileLoader {

    /** Returns the text of various states of madlib text so CLI and Madlib operate on Strings instead of files */
    @Contract("null -> fail")
    public static @NotNull String loadTextFile(Path path) throws IOException, NullPathException, TextNotProcessedException {

        if (path == null) throw new NullPathException("Path cannot be null");

        if (!Files.exists(path)) {
            throw new TextNotProcessedException("txt file could not be parsed. You blew it. Exiting program.");
        }

        return Files.readString(path);
    }
}
