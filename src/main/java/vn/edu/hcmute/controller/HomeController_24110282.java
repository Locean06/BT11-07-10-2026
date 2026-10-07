package vn.edu.hcmute.controller;

import vn.edu.hcmute.model.Author_24110282;
import vn.edu.hcmute.service.IBookService_24110282;
import vn.edu.hcmute.service.impl.BookServiceImpl_24110282;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeController_24110282 extends HttpServlet {

    private final IBookService_24110282 bookService =
            new BookServiceImpl_24110282();

    private static final int PAGE_SIZE = 3;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        List<Author_24110282> authors =
                bookService.getAllAuthors();

        if (authors.isEmpty()) {
            request.setAttribute("authors", authors);
            request.setAttribute("books", List.of());

            request.getRequestDispatcher(
                    "/WEB-INF/views/home.jsp"
            ).forward(request, response);

            return;
        }

        int requestedAuthorId = parseInt(
                request.getParameter("authorId"),
                authors.get(0).getAuthorId()
        );

        int page = Math.max(
                1,
                parseInt(request.getParameter("page"), 1)
        );

        Author_24110282 selectedAuthor = authors.stream()
                .filter(author ->
                        author.getAuthorId() == requestedAuthorId)
                .findFirst()
                .orElse(authors.get(0));

        int authorId = selectedAuthor.getAuthorId();

        int totalPages =
                bookService.getTotalPagesByAuthor(
                        authorId,
                        PAGE_SIZE
                );

        if (page > totalPages) {
            page = totalPages;
        }

        request.setAttribute(
                "authors",
                authors
        );

        request.setAttribute(
                "selectedAuthor",
                selectedAuthor
        );

        request.setAttribute(
                "books",
                bookService.getBooksByAuthor(
                        authorId,
                        page,
                        PAGE_SIZE
                )
        );

        request.setAttribute(
                "currentPage",
                page
        );

        request.setAttribute(
                "totalPages",
                totalPages
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/home.jsp"
        ).forward(request, response);
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}