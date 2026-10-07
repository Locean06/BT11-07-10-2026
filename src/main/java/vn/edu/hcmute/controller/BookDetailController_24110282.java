package vn.edu.hcmute.controller;

import vn.edu.hcmute.model.Book_24110282;
import vn.edu.hcmute.service.IBookService_24110282;
import vn.edu.hcmute.service.IRatingService_24110282;
import vn.edu.hcmute.service.impl.BookServiceImpl_24110282;
import vn.edu.hcmute.service.impl.RatingServiceImpl_24110282;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/book")
public class BookDetailController_24110282 extends HttpServlet {
    private final IBookService_24110282 bookService = new BookServiceImpl_24110282();
    private final IRatingService_24110282 ratingService = new RatingServiceImpl_24110282();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int bookId;

        try {
            bookId = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Book_24110282 book = bookService.getBookById(bookId);

        if (book == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("book", book);
        request.setAttribute("reviews", ratingService.getByBookId(bookId));
        request.setAttribute("reviewCount", ratingService.countByBookId(bookId));

        request.getRequestDispatcher("/WEB-INF/views/book-detail.jsp").forward(request, response);
    }
}
