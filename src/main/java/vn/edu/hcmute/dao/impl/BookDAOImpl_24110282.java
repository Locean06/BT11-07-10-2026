package vn.edu.hcmute.dao.impl;

import vn.edu.hcmute.config.DBConnection_24110282;
import vn.edu.hcmute.dao.IBookDAO_24110282;
import vn.edu.hcmute.model.Author_24110282;
import vn.edu.hcmute.model.Book_24110282;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl_24110282 implements IBookDAO_24110282 {

    @Override
    public List<Author_24110282> findAllAuthors() {
        List<Author_24110282> list = new ArrayList<>();
        String sql = "SELECT author_id, author_name, date_of_birth FROM author ORDER BY author_name";

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Author_24110282 author = new Author_24110282();
                author.setAuthorId(rs.getInt("author_id"));
                author.setAuthorName(rs.getString("author_name"));
                author.setDateOfBirth(rs.getDate("date_of_birth"));
                list.add(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Book_24110282> findByAuthor(int authorId, int offset, int limit) {
        List<Book_24110282> list = new ArrayList<>();

        String sql = """
                SELECT b.*, a.author_id, a.author_name
                FROM books b
                JOIN book_author ba ON b.bookid = ba.bookid
                JOIN author a ON ba.author_id = a.author_id
                WHERE a.author_id = ?
                ORDER BY b.bookid
                OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                """;

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, authorId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapBook(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public int countByAuthor(int authorId) {
        String sql = """
                SELECT COUNT(*)
                FROM books b
                JOIN book_author ba ON b.bookid = ba.bookid
                WHERE ba.author_id = ?
                """;

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, authorId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public Book_24110282 findById(int bookId) {
        String sql = """
                SELECT b.*, a.author_id, a.author_name
                FROM books b
                LEFT JOIN book_author ba ON b.bookid = ba.bookid
                LEFT JOIN author a ON ba.author_id = a.author_id
                WHERE b.bookid = ?
                """;

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapBook(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Book_24110282> findAll(int offset, int limit) {
        List<Book_24110282> list = new ArrayList<>();

        String sql = """
                SELECT b.*, a.author_id, a.author_name
                FROM books b
                LEFT JOIN book_author ba ON b.bookid = ba.bookid
                LEFT JOIN author a ON ba.author_id = a.author_id
                ORDER BY b.bookid
                OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                """;

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, offset);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapBook(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM books";

        try (Connection conn = DBConnection_24110282.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean insert(Book_24110282 book) {
        String insertBook = """
                INSERT INTO books(isbn, title, publisher, price, description, publish_date, cover_image, quantity)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String insertAuthor = "INSERT INTO book_author(bookid, author_id) VALUES (?, ?)";

        try (Connection conn = DBConnection_24110282.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psBook = conn.prepareStatement(insertBook, Statement.RETURN_GENERATED_KEYS)) {
                setBookParameters(psBook, book);
                psBook.executeUpdate();

                try (ResultSet keys = psBook.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return false;
                    }

                    int newBookId = keys.getInt(1);

                    try (PreparedStatement psAuthor = conn.prepareStatement(insertAuthor)) {
                        psAuthor.setInt(1, newBookId);
                        psAuthor.setInt(2, book.getAuthorId());
                        psAuthor.executeUpdate();
                    }
                }

                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Book_24110282 book) {
        String updateBook = """
                UPDATE books
                SET isbn = ?, title = ?, publisher = ?, price = ?, description = ?,
                    publish_date = ?, cover_image = ?, quantity = ?
                WHERE bookid = ?
                """;

        String deleteBookAuthor = "DELETE FROM book_author WHERE bookid = ?";
        String insertBookAuthor = "INSERT INTO book_author(bookid, author_id) VALUES (?, ?)";

        try (Connection conn = DBConnection_24110282.getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement ps = conn.prepareStatement(updateBook)) {
                    setBookParameters(ps, book);
                    ps.setInt(9, book.getBookId());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(deleteBookAuthor)) {
                    ps.setInt(1, book.getBookId());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(insertBookAuthor)) {
                    ps.setInt(1, book.getBookId());
                    ps.setInt(2, book.getAuthorId());
                    ps.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(int bookId) {
        String deleteRating = "DELETE FROM rating WHERE bookid = ?";
        String deleteBookAuthor = "DELETE FROM book_author WHERE bookid = ?";
        String deleteBook = "DELETE FROM books WHERE bookid = ?";

        try (Connection conn = DBConnection_24110282.getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement ps = conn.prepareStatement(deleteRating)) {
                    ps.setInt(1, bookId);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(deleteBookAuthor)) {
                    ps.setInt(1, bookId);
                    ps.executeUpdate();
                }

                int affected;
                try (PreparedStatement ps = conn.prepareStatement(deleteBook)) {
                    ps.setInt(1, bookId);
                    affected = ps.executeUpdate();
                }

                conn.commit();
                return affected > 0;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void setBookParameters(PreparedStatement ps, Book_24110282 book) throws Exception {
        if (book.getIsbn() == null) {
            ps.setNull(1, Types.INTEGER);
        } else {
            ps.setInt(1, book.getIsbn());
        }

        ps.setString(2, book.getTitle());
        ps.setString(3, book.getPublisher());
        ps.setBigDecimal(4, book.getPrice());
        ps.setString(5, book.getDescription());
        ps.setDate(6, book.getPublishDate());
        ps.setString(7, book.getCoverImage());

        if (book.getQuantity() == null) {
            ps.setNull(8, Types.INTEGER);
        } else {
            ps.setInt(8, book.getQuantity());
        }
    }

    private Book_24110282 mapBook(ResultSet rs) throws Exception {
        Book_24110282 book = new Book_24110282();
        book.setBookId(rs.getInt("bookid"));

        int isbn = rs.getInt("isbn");
        book.setIsbn(rs.wasNull() ? null : isbn);

        book.setTitle(rs.getString("title"));
        book.setPublisher(rs.getString("publisher"));
        book.setPrice(rs.getBigDecimal("price"));
        book.setDescription(rs.getString("description"));
        book.setPublishDate(rs.getDate("publish_date"));
        book.setCoverImage(rs.getString("cover_image"));

        int quantity = rs.getInt("quantity");
        book.setQuantity(rs.wasNull() ? null : quantity);

        int authorId = rs.getInt("author_id");
        book.setAuthorId(rs.wasNull() ? 0 : authorId);
        book.setAuthorName(rs.getString("author_name"));

        return book;
    }
}
