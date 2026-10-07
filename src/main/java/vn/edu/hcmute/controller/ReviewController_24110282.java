package vn.edu.hcmute.controller;

import vn.edu.hcmute.model.Rating_24110282;
import vn.edu.hcmute.model.User_24110282;
import vn.edu.hcmute.service.IRatingService_24110282;
import vn.edu.hcmute.service.impl.RatingServiceImpl_24110282;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/review")
public class ReviewController_24110282 extends HttpServlet {
    private final IRatingService_24110282 ratingService = new RatingServiceImpl_24110282();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User_24110282 user =
                (User_24110282) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int score = Integer.parseInt(request.getParameter("rating"));
            String reviewText = request.getParameter("reviewText");

            if (score < 1 || score > 5) {
                score = 5;
            }

            Rating_24110282 rating = new Rating_24110282();
            rating.setUserId(user.getId());
            rating.setBookId(bookId);
            rating.setRating(score);
            rating.setReviewText(reviewText);

            ratingService.saveOrUpdate(rating);

            response.sendRedirect(request.getContextPath() + "/book?id=" + bookId);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}
