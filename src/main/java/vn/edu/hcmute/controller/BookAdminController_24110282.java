package vn.edu.hcmute.controller;

import vn.edu.hcmute.config.UploadConfig_24110282;
import vn.edu.hcmute.model.Book_24110282;
import vn.edu.hcmute.service.IBookService_24110282;
import vn.edu.hcmute.service.impl.BookServiceImpl_24110282;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;

@WebServlet("/admin/books")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class BookAdminController_24110282 extends HttpServlet {

    private final IBookService_24110282 bookService =
            new BookServiceImpl_24110282();

    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String action = request.getParameter("action");

        int page = Math.max(
                1,
                parseInt(request.getParameter("page"), 1)
        );

        if ("create".equals(action)) {

            request.setAttribute(
                    "currentPage",
                    page
            );

            showForm(
                    request,
                    response,
                    new Book_24110282(),
                    "create"
            );

            return;
        }

        if ("edit".equals(action)) {

            int id = parseInt(
                    request.getParameter("id"),
                    0
            );

            Book_24110282 book =
                    bookService.getBookById(id);

            if (book == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/books?page="
                                + page
                );

                return;
            }

            request.setAttribute(
                    "currentPage",
                    page
            );

            showForm(
                    request,
                    response,
                    book,
                    "edit"
            );

            return;
        }

        showList(
                request,
                response
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action =
                request.getParameter("action");

        int page = Math.max(
                1,
                parseInt(
                        request.getParameter("page"),
                        1
                )
        );

        if ("delete".equals(action)) {

            int id = parseInt(
                    request.getParameter("id"),
                    0
            );

            if (id > 0) {
                bookService.deleteBook(id);
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/books?page="
                            + page
            );

            return;
        }

        try {

            Book_24110282 book =
                    readBookFromRequest(request);

            boolean success;

            if ("edit".equals(action)) {

                book.setBookId(
                        Integer.parseInt(
                                request.getParameter("bookId")
                        )
                );

                success =
                        bookService.updateBook(book);

            } else {

                success =
                        bookService.addBook(book);
            }

            if (success) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/books?page="
                                + page
                );

                return;
            }

            request.setAttribute(
                    "error",
                    "Không thể lưu sách."
            );

            request.setAttribute(
                    "currentPage",
                    page
            );

            showForm(
                    request,
                    response,
                    book,
                    action
            );

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Dữ liệu không hợp lệ: "
                            + e.getMessage()
            );

            request.setAttribute(
                    "currentPage",
                    page
            );

            Book_24110282 book =
                    new Book_24110282();

            if ("edit".equals(action)) {

                int id = parseInt(
                        request.getParameter("bookId"),
                        0
                );

                Book_24110282 oldBook =
                        bookService.getBookById(id);

                if (oldBook != null) {
                    book = oldBook;
                }
            }

            showForm(
                    request,
                    response,
                    book,
                    action
            );
        }
    }

    private void showList(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int page = Math.max(
                1,
                parseInt(
                        request.getParameter("page"),
                        1
                )
        );

        int totalPages =
                bookService.getTotalPages(
                        PAGE_SIZE
                );

        if (page > totalPages) {
            page = totalPages;
        }

        request.setAttribute(
                "books",
                bookService.getAllBooks(
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
                "/WEB-INF/views/admin/books.jsp"
        ).forward(
                request,
                response
        );
    }

    private void showForm(
            HttpServletRequest request,
            HttpServletResponse response,
            Book_24110282 book,
            String action
    ) throws ServletException, IOException {

        request.setAttribute(
                "book",
                book
        );

        request.setAttribute(
                "action",
                action == null
                        ? "create"
                        : action
        );

        request.setAttribute(
                "authors",
                bookService.getAllAuthors()
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/book-form.jsp"
        ).forward(
                request,
                response
        );
    }

    private Book_24110282 readBookFromRequest(
            HttpServletRequest request
    ) throws Exception {

        Book_24110282 book =
                new Book_24110282();

        String isbn =
                request.getParameter("isbn");

        book.setIsbn(
                isbn == null || isbn.isBlank()
                        ? null
                        : Integer.valueOf(isbn)
        );

        book.setTitle(
                request.getParameter("title")
        );

        book.setPublisher(
                request.getParameter("publisher")
        );

        String price =
                request.getParameter("price");

        book.setPrice(
                price == null || price.isBlank()
                        ? null
                        : new BigDecimal(price)
        );

        book.setDescription(
                request.getParameter("description")
        );

        String publishDate =
                request.getParameter("publishDate");

        book.setPublishDate(
                publishDate == null || publishDate.isBlank()
                        ? null
                        : Date.valueOf(publishDate)
        );

        String quantity =
                request.getParameter("quantity");

        book.setQuantity(
                quantity == null || quantity.isBlank()
                        ? null
                        : Integer.valueOf(quantity)
        );

        book.setAuthorId(
                Integer.parseInt(
                        request.getParameter("authorId")
                )
        );

        String oldCoverImage =
                request.getParameter("oldCoverImage");

        book.setCoverImage(
                saveCoverImage(
                        request,
                        oldCoverImage
                )
        );

        return book;
    }

    private String saveCoverImage(
            HttpServletRequest request,
            String oldCoverImage
    ) throws Exception {

        Part filePart =
                request.getPart("coverFile");

        if (filePart == null
                || filePart.getSize() == 0) {

            if (oldCoverImage != null
                    && !oldCoverImage.isBlank()) {

                return oldCoverImage;
            }

            return null;
        }

        String originalFileName =
                Paths.get(
                        filePart.getSubmittedFileName()
                )
                        .getFileName()
                        .toString();

        String extension = "";

        int dotIndex =
                originalFileName.lastIndexOf('.');

        if (dotIndex >= 0) {

            extension =
                    originalFileName
                            .substring(dotIndex)
                            .toLowerCase();
        }

        if (!extension.equals(".jpg")
                && !extension.equals(".jpeg")
                && !extension.equals(".png")
                && !extension.equals(".webp")) {

            throw new IllegalArgumentException(
                    "Chỉ cho phép ảnh JPG, JPEG, PNG hoặc WEBP."
            );
        }

        String fileName =
                "book_"
                        + System.currentTimeMillis()
                        + extension;

        Path uploadDirectory =
                UploadConfig_24110282
                        .getUploadPath();

        Files.createDirectories(
                uploadDirectory
        );

        Path target =
                uploadDirectory.resolve(
                        fileName
                );

        Files.copy(
                filePart.getInputStream(),
                target,
                StandardCopyOption.REPLACE_EXISTING
        );

        return fileName;
    }

    private int parseInt(
            String value,
            int defaultValue
    ) {

        try {

            return Integer.parseInt(
                    value
            );

        } catch (Exception e) {

            return defaultValue;
        }
    }
}