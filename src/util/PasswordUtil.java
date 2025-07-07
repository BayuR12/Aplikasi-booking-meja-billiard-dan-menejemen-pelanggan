package util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Method untuk mengenkripsi (hash) password
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Method untuk memverifikasi password
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}