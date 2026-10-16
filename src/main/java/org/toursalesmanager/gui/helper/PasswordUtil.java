package org.toursalesmanager.gui.helper;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {
    private PasswordUtil(){

    }

    // hash password when create account
    public static String hash(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    // check pass when login
    public static boolean matches(String plainPassword, String hashedPassword){
        if (plainPassword == null || !isBCryptHash(hashedPassword)) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isBCryptHash(String value) {
        return value != null
                && value.matches("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");
    }

}
