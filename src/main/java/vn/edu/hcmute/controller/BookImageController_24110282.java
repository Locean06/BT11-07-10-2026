package vn.edu.hcmute.controller;

import vn.edu.hcmute.config.UploadConfig_24110282;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/book-image/*")
public class BookImageController_24110282 extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            showPlaceholder(request, response);
            return;
        }

        String fileName = Paths
                .get(pathInfo.substring(1))
                .getFileName()
                .toString();

        Path imagePath = UploadConfig_24110282
                .getUploadPath()
                .resolve(fileName);

        if (!Files.exists(imagePath)) {
            showPlaceholder(request, response);
            return;
        }

        String contentType = Files.probeContentType(imagePath);

        if (contentType == null) {
            String lower = fileName.toLowerCase();

            if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (lower.endsWith(".png")) {
                contentType = "image/png";
            } else if (lower.endsWith(".webp")) {
                contentType = "image/webp";
            } else {
                contentType = "application/octet-stream";
            }
        }

        response.reset();
        response.setContentType(contentType);
        response.setContentLengthLong(Files.size(imagePath));

        try (OutputStream out = response.getOutputStream()) {
            Files.copy(imagePath, out);
            out.flush();
        }
    }

    private void showPlaceholder(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/assets/images/placeholder.svg"
        );
    }
}