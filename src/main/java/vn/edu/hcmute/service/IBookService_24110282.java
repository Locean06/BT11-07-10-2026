package vn.edu.hcmute.service;

import vn.edu.hcmute.model.Author_24110282;
import vn.edu.hcmute.model.Book_24110282;

import java.util.List;

public interface IBookService_24110282 {
    List<Author_24110282> getAllAuthors();
    List<Book_24110282> getBooksByAuthor(int authorId, int page, int pageSize);
    int getTotalPagesByAuthor(int authorId, int pageSize);

    Book_24110282 getBookById(int bookId);

    List<Book_24110282> getAllBooks(int page, int pageSize);
    int getTotalPages(int pageSize);

    boolean addBook(Book_24110282 book);
    boolean updateBook(Book_24110282 book);
    boolean deleteBook(int bookId);
}
