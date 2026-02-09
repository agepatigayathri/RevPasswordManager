package com.revature.util;



public class PasswordValidatorUtil {

    public static boolean isValidMasterPassword(String password) {

        // must be AT LEAST 8 characters
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {

            if (Character.isUpperCase(ch)) {
                hasUpper = true;

            } else if (Character.isLowerCase(ch)) {
                hasLower = true;

            } else if (Character.isDigit(ch)) {
                hasDigit = true;

            } else if (!Character.isLetterOrDigit(ch)) {
                // accepts ANY special character (@, #, $, %, etc.)
                hasSpecial = true;
            }
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}

