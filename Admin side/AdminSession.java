package agapay;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class AdminSession {
    private static final String FILE = "admin_session.txt";

    public static boolean isLoggedIn() {
        File f = new File(FILE);
        if (!f.exists()) return false;
        try {
            String content = Files.readString(f.toPath()).trim();
            String[] parts = content.split(",");
            boolean loggedIn = Boolean.parseBoolean(parts[0]);
            boolean rememberMe = parts.length > 1 && parts[1].equalsIgnoreCase("yes");

            // Kung naka-login pero hindi pinili ang 'Remember Me', 
            // i-auto logout natin pag-restart ng app.
            if (loggedIn && !rememberMe) {
                setLoggedIn(false, false);
                return false;
            }
            return loggedIn;
        } catch (IOException e) { 
            return false; 
        }
    }

    public static void setLoggedIn(boolean val, boolean rememberMe) {
        try (FileWriter fw = new FileWriter(FILE)) {
            String status = val ? "true" : "false";
            String remember = rememberMe ? "yes" : "no";
            fw.write(status + "," + remember);
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    }
}