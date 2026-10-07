package vn.edu.hcmute.dao;

import vn.edu.hcmute.model.Author_24110282;
import vn.edu.hcmute.model.Book_24110282;

import java.util.List;

public interface IBookDAO_24110282 {
    List<Author_24110282> findAllAuthors();
    List<Book_24110282> findByAuthor(int authorId, int offset, int limit);
    int countByAuthor(int authorId);

    Book_24110282 findById(int bookId);

    List<Book_24110282> findAll(int offset, int limit);
    int countAll();

    boolean insert(Book_24110282 book);
    boolean update(Book_24110282 book);
    boolean delete(int bookId);
}
