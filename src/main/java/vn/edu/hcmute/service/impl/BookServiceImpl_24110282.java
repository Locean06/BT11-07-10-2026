package vn.edu.hcmute.service.impl;

import vn.edu.hcmute.dao.IBookDAO_24110282;
import vn.edu.hcmute.dao.impl.BookDAOImpl_24110282;
import vn.edu.hcmute.model.Author_24110282;
import vn.edu.hcmute.model.Book_24110282;
import vn.edu.hcmute.service.IBookService_24110282;

import java.util.List;

public class BookServiceImpl_24110282 implements IBookService_24110282 {
    private final IBookDAO_24110282 bookDAO = new BookDAOImpl_24110282();

    @Override
    public List<Author_24110282> getAllAuthors() {
        return bookDAO.findAllAuthors();
    }

    @Override
    public List<Book_24110282> getBooksByAuthor(int authorId, int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int offset = (safePage - 1) * pageSize;
        return bookDAO.findByAuthor(authorId, offset, pageSize);
    }

    @Override
    public int getTotalPagesByAuthor(int authorId, int pageSize) {
        int total = bookDAO.countByAuthor(authorId);
        return Math.max(1, (int) Math.ceil(total / (double) pageSize));
    }

    @Override
    public Book_24110282 getBookById(int bookId) {
        return bookDAO.findById(bookId);
    }

    @Override
    public List<Book_24110282> getAllBooks(int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int offset = (safePage - 1) * pageSize;
        return bookDAO.findAll(offset, pageSize);
    }

    @Override
    public int getTotalPages(int pageSize) {
        int total = bookDAO.countAll();
        return Math.max(1, (int) Math.ceil(total / (double) pageSize));
    }

    @Override
    public boolean addBook(Book_24110282 book) {
        return bookDAO.insert(book);
    }

    @Override
    public boolean updateBook(Book_24110282 book) {
        return bookDAO.update(book);
    }

    @Override
    public boolean deleteBook(int bookId) {
        return bookDAO.delete(bookId);
    }
}
