package vn.edu.hcmute.controller;

import vn.edu.hcmute.dao.IOrderDAO_24110282;
import vn.edu.hcmute.dao.impl.OrderDAOImpl_24110282;
import vn.edu.hcmute.model.CartItem_24110282;
import vn.edu.hcmute.model.User_24110282;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet("/checkout")
public class CheckoutController_24110282 extends HttpServlet {
    private final IOrderDAO_24110282 orderDAO = new OrderDAOImpl_24110282();

    @SuppressWarnings("unchecked")
    private Map<Integer, CartItem_24110282> getCart(HttpSession session) {
        Object cart = session.getAttribute("cart");
        return cart instanceof Map ? (Map<Integer, CartItem_24110282>) cart : null;
    }

    private boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("user") == null) {
            request.getSession().setAttribute("loginMessage", "Vui lòng đăng nhập để thanh toán.");
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!requireLogin(request, response)) return;
        Map<Integer, CartItem_24110282> cart = getCart(request.getSession());
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        BigDecimal total = cart.values().stream().map(CartItem_24110282::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        request.setAttribute("cart", cart);
        request.setAttribute("total", total);
        request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!requireLogin(request, response)) return;
        HttpSession session = request.getSession();
        Map<Integer, CartItem_24110282> cart = getCart(session);
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        String customerName = trim(request.getParameter("customerName"));
        String phone = trim(request.getParameter("phone"));
        String address = trim(request.getParameter("address"));
        String note = trim(request.getParameter("note"));

        if (customerName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ họ tên, số điện thoại và địa chỉ nhận hàng.");
            doGet(request, response);
            return;
        }
        if (!phone.matches("[0-9+ .-]{9,15}")) {
            request.setAttribute("error", "Số điện thoại không hợp lệ.");
            doGet(request, response);
            return;
        }

        try {
            User_24110282 user = (User_24110282) session.getAttribute("user");
            int orderId = orderDAO.createCodOrder(user.getId(), customerName, phone, address, note, cart);
            session.removeAttribute("cart");
            session.setAttribute("cartCount", 0);
            session.setAttribute("cartTotal", BigDecimal.ZERO);
            request.setAttribute("orderId", orderId);
            request.getRequestDispatcher("/WEB-INF/views/order-success.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage() == null ? "Đặt hàng thất bại." : e.getMessage());
            doGet(request, response);
        }
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
}
