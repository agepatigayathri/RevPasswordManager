package com.revature.util;

import java.util.Random;

public class PasswordGeneratorUtil {

    public static String generatePassword(
            int length,
            boolean useUpper,
            boolean useNumbers,
            boolean useSpecial) {

        String lower = "abcdefghijklmnopqrstuvwxyz";
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String special = "!@#$%^&*()_+";

        StringBuilder chars = new StringBuilder(lower);

        if (useUpper) chars.append(upper);
        if (useNumbers) chars.append(numbers);
        if (useSpecial) chars.append(special);

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }
}
