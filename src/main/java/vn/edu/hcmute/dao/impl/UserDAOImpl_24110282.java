package vn.edu.hcmute.dao.impl;

import vn.edu.hcmute.config.DBConnection_24110282;
import vn.edu.hcmute.dao.IUserDAO_24110282;
import vn.edu.hcmute.model.User_24110282;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class UserDAOImpl_24110282 implements IUserDAO_24110282 {

    @Override
    public User_24110282 findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean insert(User_24110282 user) {
        String sql = """
                INSERT INTO users(email, fullname, phone, passwd, signup_date, last_login, is_admin)
                VALUES (?, ?, ?, ?, ?, NULL, ?)
                """;

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullname());

            if (user.getPhone() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, user.getPhone());
            }

            ps.setString(4, user.getPasswd());
            ps.setTimestamp(5,
                    user.getSignupDate() == null
                            ? new Timestamp(System.currentTimeMillis())
                            : user.getSignupDate());
            ps.setBoolean(6, user.isAdmin());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = GETDATE() WHERE id = ?";

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private User_24110282 mapUser(ResultSet rs) throws Exception {
        User_24110282 user = new User_24110282();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setFullname(rs.getString("fullname"));

        int phone = rs.getInt("phone");
        user.setPhone(rs.wasNull() ? null : phone);

        user.setPasswd(rs.getString("passwd"));
        user.setSignupDate(rs.getTimestamp("signup_date"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setAdmin(rs.getBoolean("is_admin"));
        return user;
    }
}
