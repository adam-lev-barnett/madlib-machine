import userinterface.UserController;
import utility.exceptions.NullEntryException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, NullEntryException {
        UserController.initiateMadlibCreation();
        // System.out.println("Writing file to: " + new File("boop").getAbsolutePath());


    }
}
