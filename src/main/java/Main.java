import userinterface.UserController;
import utility.exceptions.NullEntryException;
import utility.exceptions.NullPOSListException;
import utility.exceptions.TextNotProcessedException;

import java.io.IOException;

public class Main {

    public static final boolean TEST_MODE = false;

    public static void main(String[] args) throws IOException, InterruptedException, NullEntryException, TextNotProcessedException, NullPOSListException {
        UserController.initiateMadlibCreation();
        // System.out.println("Writing file to: " + new File("boop").getAbsolutePath());


    }
}
