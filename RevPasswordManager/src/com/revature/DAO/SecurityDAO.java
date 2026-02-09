package com.revature.DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revature.util.DBConnection;

public class SecurityDAO {

    public boolean saveAnswer(int userId, int questionId, String answer) {

        String sql = "INSERT INTO user_security_answers(user_id, question_id, answer) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, questionId);
            ps.setString(3, answer);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyAnswer(int userId, int questionId, String answer) {

        String sql = "SELECT * FROM user_security_answers WHERE user_id=? AND question_id=? AND answer=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, questionId);
            ps.setString(3, answer);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 // -------- UPDATE SECURITY ANSWER --------
    public boolean updateSecurityAnswer(int userId, int questionId, String newAnswer) {

        String sql = "UPDATE user_security_answers SET answer=? WHERE user_id=? AND question_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newAnswer);
            ps.setInt(2, userId);
            ps.setInt(3, questionId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

