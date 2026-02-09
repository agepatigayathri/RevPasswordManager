package com.revature.service;


import java.time.LocalDateTime;
import java.util.Random;

import com.revature.DAO.VerificationCodeDAO;



public class VerificationCodeService {

    private VerificationCodeDAO dao = new VerificationCodeDAO();

    public String generateAndSendCode(int userId) {

        String code = String.valueOf(100000 + new Random().nextInt(900000));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        dao.saveCode(userId, code, expiry);

        // Console app print OTP
        System.out.println("Your OTP is: " + code +
                " (valid for 5 minutes)");

        return code;
    }

    public boolean verifyCode(int userId, String code) {
        return dao.verifyCode(userId, code);
    }
}
