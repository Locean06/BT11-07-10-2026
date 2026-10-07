package vn.edu.hcmute.dao.impl;

import vn.edu.hcmute.config.DBConnection_24110282;
import vn.edu.hcmute.dao.IRatingDAO_24110282;
import vn.edu.hcmute.model.Rating_24110282;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RatingDAOImpl_24110282 implements IRatingDAO_24110282 {

    @Override
    public List<Rating_24110282> findByBookId(int bookId) {
        List<Rating_24110282> list = new ArrayList<>();

        String sql = """
                SELECT r.userid, r.bookid, r.rating, r.review_text, u.fullname
                FROM rating r
                JOIN users u ON r.userid = u.id
                WHERE r.bookid = ?
                ORDER BY u.fullname
                """;

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rating_24110282 rating = new Rating_24110282();
                    rating.setUserId(rs.getInt("userid"));
                    rating.setBookId(rs.getInt("bookid"));
                    rating.setRating(rs.getInt("rating"));
                    rating.setReviewText(rs.getString("review_text"));
                    rating.setUserName(rs.getString("fullname"));
                    list.add(rating);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public int countByBookId(int bookId) {
        String sql = "SELECT COUNT(*) FROM rating WHERE bookid = ?";

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean saveOrUpdate(Rating_24110282 rating) {
        String check = "SELECT COUNT(*) FROM rating WHERE userid = ? AND bookid = ?";
        String update = "UPDATE rating SET rating = ?, review_text = ? WHERE userid = ? AND bookid = ?";
        String insert = "INSERT INTO rating(userid, bookid, rating, review_text) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection_24110282.getConnection()) {
            boolean exists;

            try (PreparedStatement ps = conn.prepareStatement(check)) {
                ps.setInt(1, rating.getUserId());
                ps.setInt(2, rating.getBookId());

                try (ResultSet rs = ps.executeQuery()) {
                    exists = rs.next() && rs.getInt(1) > 0;
                }
            }

            if (exists) {
                try (PreparedStatement ps = conn.prepareStatement(update)) {
                    ps.setInt(1, rating.getRating());
                    ps.setString(2, rating.getReviewText());
                    ps.setInt(3, rating.getUserId());
                    ps.setInt(4, rating.getBookId());
                    return ps.executeUpdate() > 0;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setInt(1, rating.getUserId());
                ps.setInt(2, rating.getBookId());
                ps.setInt(3, rating.getRating());
                ps.setString(4, rating.getReviewText());
                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
