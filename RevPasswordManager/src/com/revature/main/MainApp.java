package com.revature.main;


import java.util.Scanner;
import java.util.List;

import com.revature.model.User;
import com.revature.model.PasswordEntry;
import com.revature.service.UserService;
import com.revature.service.VerificationCodeService;
import com.revature.util.PasswordValidatorUtil;
import com.revature.service.PasswordService;
import com.revature.service.SecurityService;

public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();

        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine();

         
         // -------- REGISTER --------
         // -------- REGISTER --------
            if (choice == 1) {

                System.out.print("Name: ");
                String name = sc.nextLine();

                String email;
                String password;

                // ---------- EMAIL LOOP ----------
                while (true) {
                    System.out.print("Email: ");
                    email = sc.nextLine();

                    if (!email.endsWith("@gmail.com")) {
                        System.out.println("Invalid email. Only @gmail.com is allowed.");
                        continue;
                    }

                    if (userService.getUserByEmail(email) != null) {
                        System.out.println("An account is already registered with this email.");
                        continue;
                    }

                    // email is valid & unique
                    break;
                }

                // ---------- PASSWORD LOOP ----------
                while (true) {
                    System.out.print(
                        "Password (min 8 chars, Upper, Lower, Number, Special): ");
                    password = sc.nextLine();

                    if (!PasswordValidatorUtil.isValidMasterPassword(password)) {
                        System.out.println(
                            "Invalid password. Must be at least 8 characters with Upper, Lower, Number & Special character."
                        );
                        continue;
                    }

                    // password valid
                    break;
                }

                // ---------- REGISTER USER ----------
                int userId = userService.registerAndReturnUserId(name, email, password);

                if (userId <= 0) {
                    System.out.println("Registration failed due to system error.");
                    continue;
                }

                // ---------- SECURITY QUESTION ----------
                SecurityService securityService = new SecurityService();

                System.out.println("Security Question:");
                System.out.println("1. What is your favorite color?");
                System.out.print("Answer: ");
                String answer = sc.nextLine();

                securityService.saveSecurityAnswer(userId, 1, answer);

                System.out.println("Registration successful!");
            }

            // -------- LOGIN --------
            else if (choice == 2) {
                System.out.print("Email: ");
                String email = sc.nextLine();

                System.out.print("Password: ");
                String pass = sc.nextLine();

                User user = userService.login(email, pass);

                if (user != null) {
                    System.out.println("Welcome " + user.getName());

                    PasswordService passwordService = new PasswordService();

                    // -------- USER DASHBOARD --------
                    while (true) {
                        System.out.println("\n1. Add Password");
                        System.out.println("2. View All Passwords");
                        System.out.println("3. View Single Password");
                        System.out.println("4. Update Password");
                        System.out.println("5. Delete Password");
                        System.out.println("6. Generate Password");
                        System.out.println("7. Update Profile");
                        System.out.println("8. Search Password");
                        System.out.println("9. Update Security Question");
                        System.out.println("10. Logout");
                        System.out.print("Choose: ");

                        int option = sc.nextInt();
                        sc.nextLine();

                        // ADD PASSWORD
                        if (option == 1) {
                            System.out.print("Account Name: ");
                            String acc = sc.nextLine();

                            System.out.print("Username: ");
                            String uname = sc.nextLine();

                            System.out.print("Password: ");
                            String pwd = sc.nextLine();

                            if (passwordService.addPassword(
                                    user.getUserId(), acc, uname, pwd))
                                System.out.println("Password added successfully");
                            else
                                System.out.println("Failed to add password");
                        }

                        // VIEW ALL PASSWORDS
                        else if (option == 2) {
                            List<PasswordEntry> passwords =
                                    passwordService.getAllPasswords(user.getUserId());

                            if (passwords.isEmpty()) {
                                System.out.println("No passwords stored");
                            } else {
                                System.out.println("\n--- Saved Passwords ---");
                                for (PasswordEntry p : passwords) {
                                    System.out.println(
                                            "Account: " + p.getAccountName() +
                                            " | Username: " + p.getUsername()
                                    );
                                }
                            }
                        }

                        // VIEW SINGLE PASSWORD
                        else if (option == 3) {
                            System.out.print("Enter Account Name: ");
                            String accName = sc.nextLine();

                            System.out.print("Re-enter Master Password: ");
                            String master = sc.nextLine();

                            if (userService.login(user.getEmail(), master) != null) {
                                String encryptedPwd =
                                        passwordService.getPasswordByAccount(
                                                user.getUserId(), accName);

                                if (encryptedPwd != null)
                                    System.out.println("Encrypted Password: " + encryptedPwd);
                                else
                                    System.out.println("Account not found");
                            } else {
                                System.out.println("Master password incorrect");
                            }
                        }

                        // UPDATE PASSWORD
                        else if (option == 4) {
                            System.out.print("Enter Account Name: ");
                            String accName = sc.nextLine();

                            System.out.print("Enter New Password: ");
                            String newPwd = sc.nextLine();

                            if (passwordService.updatePassword(
                                    user.getUserId(), accName, newPwd))
                                System.out.println("Password updated successfully");
                            else
                                System.out.println("Account not found or update failed");
                        }

                        // DELETE PASSWORD
                        else if (option == 5) {
                            System.out.print("Enter Account Name to Delete: ");
                            String accName = sc.nextLine();

                            System.out.print("Are you sure? (yes/no): ");
                            String confirm = sc.nextLine();

                            if (confirm.equalsIgnoreCase("yes")) {
                                if (passwordService.deletePassword(
                                        user.getUserId(), accName))
                                    System.out.println("Password deleted successfully");
                                else
                                    System.out.println("Account not found or delete failed");
                            } else {
                                System.out.println("Delete operation cancelled");
                            }
                        }

                        // PASSWORD GENERATOR
                        else if (option == 6) {
                            System.out.print("Enter password length: ");
                            int length = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Include uppercase letters? (yes/no): ");
                            boolean upper = sc.nextLine().equalsIgnoreCase("yes");

                            System.out.print("Include numbers? (yes/no): ");
                            boolean numbers = sc.nextLine().equalsIgnoreCase("yes");

                            System.out.print("Include special characters? (yes/no): ");
                            boolean special = sc.nextLine().equalsIgnoreCase("yes");

                            String generated =
                                    passwordService.generatePassword(
                                            length, upper, numbers, special);

                            System.out.println("Generated Password: " + generated);
                        }

                        // UPDATE PROFILE
                        else if (option == 7) {
                            System.out.print("Enter new name: ");
                            String newName = sc.nextLine();

                            System.out.print("Enter new email: ");
                            String newEmail = sc.nextLine();

                            if (userService.updateProfile(
                                    user.getUserId(), newName, newEmail)) {

                                user.setName(newName);
                                user.setEmail(newEmail);
                                System.out.println("Profile updated successfully");
                            } else {
                                System.out.println("Profile update failed");
                            }
                        }

                        // SEARCH PASSWORD
                        else if (option == 8) {
                            System.out.print("Enter account name to search: ");
                            String keyword = sc.nextLine();

                            List<PasswordEntry> results =
                                    passwordService.searchPasswords(
                                            user.getUserId(), keyword);

                            if (results.isEmpty()) {
                                System.out.println("No matching accounts found");
                            } else {
                                System.out.println("\n--- Search Results ---");
                                for (PasswordEntry p : results) {
                                    System.out.println(
                                            "Account: " + p.getAccountName() +
                                            " | Username: " + p.getUsername()
                                    );
                                }
                            }
                        }
                     // -------- UPDATE SECURITY QUESTION --------
                        else if (option == 9) {

                            System.out.print("Re-enter Master Password: ");
                            String master = sc.nextLine();

                            if (userService.login(user.getEmail(), master) == null) {
                                System.out.println("Master password incorrect");
                                continue;
                            }

                            SecurityService securityService = new SecurityService();

                            System.out.println("Security Question:");
                            System.out.println("1. What is your favorite color?");
                            System.out.print("Enter new answer: ");
                            String newAnswer = sc.nextLine();

                            if (securityService.updateSecurityAnswer(
                                    user.getUserId(), 1, newAnswer)) {
                                System.out.println("Security question updated successfully");
                            } else {
                                System.out.println("Failed to update security question");
                            }
                        }


                        // LOGOUT
                        else if (option == 10) {
                            System.out.println("Logged out successfully");
                            break;
                        } else {
                            System.out.println("Invalid option");
                        }
                    }

                } else {
                    System.out.println("Invalid login");
                }
            }
         // -------- FORGOT PASSWORD --------
            else if (choice == 3) {

                System.out.print("Enter registered email: ");
                String email = sc.nextLine();

                User user = userService.getUserByEmail(email);

                if (user == null) {
                    System.out.println("User not found");
                } else {

                    SecurityService securityService = new SecurityService();

                    // ---- STEP 1: SECURITY QUESTION ----
                    System.out.println("Security Question:");
                    System.out.println("1. What is your favorite color?");
                    System.out.print("Answer: ");
                    String answer = sc.nextLine();

                    boolean isAnswerValid =
                            securityService.verifySecurityAnswer(
                                    user.getUserId(), 1, answer);

                    if (!isAnswerValid) {
                        System.out.println("Security answer incorrect");
                        return;
                    }

                    // ---- STEP 2: OTP VERIFICATION ----
                    VerificationCodeService otpService = new VerificationCodeService();

                    otpService.generateAndSendCode(user.getUserId());

                    System.out.print("Enter OTP: ");
                    String otp = sc.nextLine();

                    if (!otpService.verifyCode(user.getUserId(), otp)) {
                        System.out.println("Invalid or expired OTP");
                        return;
                    }

                    // ---- STEP 3: RESET PASSWORD ----
                    System.out.print("Enter new master password: ");
                    String newPwd = sc.nextLine();

                    if (userService.resetPassword(user.getUserId(), newPwd)) {
                        System.out.println("Password reset successful");
                    } else {
                        System.out.println("Password reset failed");
                    }
                }
            }

            // -------- EXIT --------
            else if (choice == 4) {
                System.out.println("Thank you! Application closed.");
                break;
            } else {
                System.out.println("Invalid choice");
            }
        }
        sc.close();
    }
}
