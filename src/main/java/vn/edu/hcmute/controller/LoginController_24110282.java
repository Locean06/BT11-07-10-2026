package vn.edu.hcmute.controller;

import vn.edu.hcmute.model.User_24110282;
import vn.edu.hcmute.service.IUserService_24110282;
import vn.edu.hcmute.service.impl.UserServiceImpl_24110282;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginController_24110282 extends HttpServlet {
    private final IUserService_24110282 userService = new UserServiceImpl_24110282();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession().getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User_24110282 user = userService.login(email, password);

        if (user == null) {
            request.setAttribute("error", "Email hoặc mật khẩu không đúng.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        request.getSession().setAttribute("user", user);

        if (user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/admin/books");
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}
