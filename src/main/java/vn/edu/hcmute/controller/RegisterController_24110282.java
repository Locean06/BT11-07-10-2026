package vn.edu.hcmute.controller;

import vn.edu.hcmute.model.User_24110282;
import vn.edu.hcmute.service.IUserService_24110282;
import vn.edu.hcmute.service.impl.UserServiceImpl_24110282;
import vn.edu.hcmute.util.EmailUtil_24110282;
import vn.edu.hcmute.util.OtpUtil_24110282;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/register")
public class RegisterController_24110282 extends HttpServlet {
    private final IUserService_24110282 userService = new UserServiceImpl_24110282();
    private static final long OTP_EXPIRE_MS = 5 * 60 * 1000L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("verify".equals(action)) {
            verifyOtp(request, response);
        } else {
            sendOtp(request, response);
        }
    }

    private void sendOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String fullname = request.getParameter("fullname");
        String phoneText = request.getParameter("phone");
        String password = request.getParameter("password");

        if (email == null || email.isBlank()
                || fullname == null || fullname.isBlank()
                || password == null || password.isBlank()) {

            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        if (userService.existsByEmail(email)) {
            request.setAttribute("error", "Email đã tồn tại.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        Integer phone = null;
        try {
            if (phoneText != null && !phoneText.isBlank()) {
                phone = Integer.valueOf(phoneText);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Số điện thoại không hợp lệ.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        User_24110282 pendingUser = new User_24110282();
        pendingUser.setEmail(email);
        pendingUser.setFullname(fullname);
        pendingUser.setPhone(phone);
        pendingUser.setPasswd(password);
        pendingUser.setSignupDate(new Timestamp(System.currentTimeMillis()));
        pendingUser.setAdmin(false);

        String otp = OtpUtil_24110282.generateOtp();

        if (!EmailUtil_24110282.sendOtp(email, otp)) {
            request.setAttribute(
                    "error",
                    "Không gửi được OTP. Hãy cấu hình Gmail App Password trong EmailUtil_24110282.java."
            );
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("registerOtp", otp);
        session.setAttribute("registerOtpExpire", System.currentTimeMillis() + OTP_EXPIRE_MS);
        session.setAttribute("pendingUser", pendingUser);

        request.setAttribute("otpSent", true);
        request.setAttribute("email", email);
        request.setAttribute("message", "OTP đã được gửi đến " + email);
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    private void verifyOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String expectedOtp = (String) session.getAttribute("registerOtp");
        Long expire = (Long) session.getAttribute("registerOtpExpire");
        User_24110282 pendingUser =
                (User_24110282) session.getAttribute("pendingUser");

        String inputOtp = request.getParameter("otp");

        if (expectedOtp == null || expire == null || pendingUser == null) {
            request.setAttribute("error", "Phiên đăng ký không tồn tại. Vui lòng gửi lại OTP.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        if (System.currentTimeMillis() > expire) {
            clearOtpSession(session);
            request.setAttribute("error", "OTP đã hết hạn. Vui lòng đăng ký lại.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        if (!expectedOtp.equals(inputOtp)) {
            request.setAttribute("otpSent", true);
            request.setAttribute("email", pendingUser.getEmail());
            request.setAttribute("error", "OTP không đúng.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        boolean success = userService.register(pendingUser);
        clearOtpSession(session);

        if (!success) {
            request.setAttribute("error", "Đăng ký thất bại.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/login?registered=1");
    }

    private void clearOtpSession(HttpSession session) {
        session.removeAttribute("registerOtp");
        session.removeAttribute("registerOtpExpire");
        session.removeAttribute("pendingUser");
    }
}
