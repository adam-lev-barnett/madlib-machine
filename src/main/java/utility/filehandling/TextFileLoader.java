package utility.filehandling;

import utility.exceptions.NullPathException;
import utility.exceptions.TextNotProcessedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class TextFileLoader {

    public static String loadTextFile(Path path) throws IOException, NullPathException, TextNotProcessedException {

        if (path == null) throw new NullPathException("Path cannot be null");

        if (!Files.exists(path)) {
            throw new TextNotProcessedException("txt file could not be parsed. You blew it. Exiting program.");
        }

        return Files.readString(path);
    }
}
