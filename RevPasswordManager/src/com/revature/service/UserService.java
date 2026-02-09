package com.revature.service;

import com.revature.DAO.UserDAO;
import com.revature.model.User;
import com.revature.util.EmailValidatorUtil;
import com.revature.util.PasswordUtil;
import com.revature.util.PasswordValidatorUtil;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    // -------- REGISTER + RETURN USER ID --------
    public int registerAndReturnUserId(String name, String email, String password) {

        // Gmail validation
        if (!EmailValidatorUtil.isValidGmail(email)) {
            System.out.println("Invalid email. Only @gmail.com is allowed.");
            return -1;
        }

        // Email already exists
        if (userDAO.emailExists(email)) {
            System.out.println("An account is already registered with this email.");
            return -1;
        }

        // Password validation
        if (!PasswordValidatorUtil.isValidMasterPassword(password)) {
            System.out.println(
                "Invalid password. Password must be at least 8 characters " +
                "with Uppercase, Lowercase, Number, and Special character."
            );
            return -1;
        }

        // Create user
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setMasterPassword(PasswordUtil.encrypt(password));

        boolean result = userDAO.registerUser(user);

        if (!result) {
            System.out.print("Registration failed due to database error.");
            return -1;
        }

        // Fetch saved user to get userId
        User savedUser = userDAO.getUserByEmail(email);

        if (savedUser == null) {
            System.out.println("Registration successful, but failed to fetch user details.");
            return -1;
        }

        System.out.println("Registration successful.");
        return savedUser.getUserId();
    }

    // -------- LOGIN --------
    public User login(String email, String password) {

        User user = userDAO.login(email, PasswordUtil.encrypt(password));

        if (user == null) {
            System.out.println("Invalid email or password.");
        }

        return user;
    }

    // -------- RESET MASTER PASSWORD --------
    public boolean resetPassword(int userId, String newPassword) {

        if (!PasswordValidatorUtil.isValidMasterPassword(newPassword)) {
            System.out.println(
                "Invalid password. Password must be at least 8 characters " +
                "with Uppercase, Lowercase, Number, and Special character."
            );
            return false;
        }

        boolean result = userDAO.updateMasterPassword(
                userId, PasswordUtil.encrypt(newPassword));

        if (result) {
            System.out.println("Master password reset successful.");
        } else {
            System.out.println("Password reset failed.");
        }

        return result;
    }

    // -------- GET USER BY EMAIL --------
    public User getUserByEmail(String email) {

        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            System.out.println("No user found with this email.");
        }

        return user;
    }

    // -------- UPDATE PROFILE --------
    public boolean updateProfile(int userId, String name, String email) {

        if (!EmailValidatorUtil.isValidGmail(email)) {
            System.out.println("Invalid email. Only @gmail.com is allowed.");
            return false;
        }

        boolean result = userDAO.updateProfile(userId, name, email);

        if (result) {
            System.out.println("Profile updated successfully.");
        } else {
            System.out.println("Profile update failed.");
        }

        return result;
    }
}
