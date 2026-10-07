package vn.edu.hcmute.controller;

import vn.edu.hcmute.model.Book_24110282;
import vn.edu.hcmute.model.CartItem_24110282;
import vn.edu.hcmute.service.IBookService_24110282;
import vn.edu.hcmute.service.impl.BookServiceImpl_24110282;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/cart")
public class CartController_24110282 extends HttpServlet {

    private final IBookService_24110282 bookService =
            new BookServiceImpl_24110282();

    @SuppressWarnings("unchecked")
    private Map<Integer, CartItem_24110282> getCart(HttpSession session) {

        Object existing = session.getAttribute("cart");

        if (existing instanceof Map) {
            return (Map<Integer, CartItem_24110282>) existing;
        }

        Map<Integer, CartItem_24110282> cart =
                new LinkedHashMap<>();

        session.setAttribute("cart", cart);

        return cart;
    }

    private void updateCartSummary(
            HttpSession session,
            Map<Integer, CartItem_24110282> cart
    ) {

        int count = cart.values()
                .stream()
                .mapToInt(CartItem_24110282::getQuantity)
                .sum();

        BigDecimal total = cart.values()
                .stream()
                .map(CartItem_24110282::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        session.setAttribute("cartCount", count);
        session.setAttribute("cartTotal", total);
    }

    // ============================
    // GET - HIỂN THỊ GIỎ HÀNG
    // ============================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        HttpSession session = request.getSession();

        Map<Integer, CartItem_24110282> cart =
                getCart(session);

        updateCartSummary(session, cart);

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        try (PrintWriter out = response.getWriter()) {

            renderCartPage(
                    request,
                    session,
                    cart,
                    out
            );
        }
    }

    // ============================
    // RENDER HTML TRỰC TIẾP
    // KHÔNG DÙNG JSP
    // ============================

    private void renderCartPage(
            HttpServletRequest request,
            HttpSession session,
            Map<Integer, CartItem_24110282> cart,
            PrintWriter out
    ) {

        String ctx = request.getContextPath();

        int cartCount =
                session.getAttribute("cartCount")
                        instanceof Integer
                        ? (Integer) session.getAttribute("cartCount")
                        : 0;

        BigDecimal cartTotal =
                session.getAttribute("cartTotal")
                        instanceof BigDecimal
                        ? (BigDecimal) session.getAttribute("cartTotal")
                        : BigDecimal.ZERO;


        out.println("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Giỏ hàng</title>

                    <style>

                        * {
                            box-sizing: border-box;
                        }

                        body {
                            margin: 0;
                            font-family: Arial, sans-serif;
                            background: #f7f8fc;
                            color: #111827;
                        }

                        .topbar {
                            height: 72px;
                            background: linear-gradient(
                                135deg,
                                #11182f,
                                #21194b
                            );

                            display: flex;
                            align-items: center;
                        }

                        .topbar-inner {
                            width: 1180px;
                            max-width: calc(100% - 48px);
                            margin: auto;

                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                        }

                        .logo {
                            color: white;
                            font-size: 26px;
                            font-weight: 800;
                            text-decoration: none;
                        }

                        .nav {
                            display: flex;
                            gap: 32px;
                        }

                        .nav a {
                            color: white;
                            text-decoration: none;
                            font-weight: 600;
                        }

                        .cart-page {
                            max-width: 1180px;
                            margin: 0 auto;
                            padding: 36px 24px 60px;
                        }

                        .section-header {
                            display: flex;
                            align-items: center;
                            justify-content: space-between;
                            gap: 20px;

                            margin-bottom: 28px;
                        }

                        .section-small {
                            display: block;

                            margin-bottom: 6px;

                            font-size: 12px;
                            font-weight: 700;
                            letter-spacing: 1.3px;

                            color: #6c5ce7;
                        }

                        .section-header h1 {
                            margin: 0;

                            font-size: 30px;
                        }

                        .secondary-btn {
                            padding: 11px 18px;

                            border: 1px solid #d8dce6;
                            border-radius: 9px;

                            text-decoration: none;

                            color: #374151;
                            background: white;

                            font-weight: 600;
                        }

                        .secondary-btn:hover {
                            border-color: #6c5ce7;
                            color: #6c5ce7;
                        }

                        .cart-alert {
                            margin-bottom: 18px;

                            padding: 12px 16px;

                            border-radius: 9px;
                        }

                        .success {
                            background: #ecfdf3;
                            color: #166534;
                            border: 1px solid #bbf7d0;
                        }

                        .error {
                            background: #fef2f2;
                            color: #b91c1c;
                            border: 1px solid #fecaca;
                        }

                        .cart-layout {
                            display: grid;

                            grid-template-columns:
                                minmax(0, 1fr) 330px;

                            gap: 24px;

                            align-items: start;
                        }

                        .cart-list {
                            display: flex;
                            flex-direction: column;

                            gap: 18px;
                        }

                        .cart-item {
                            display: grid;

                            grid-template-columns:
                                145px
                                minmax(180px, 1fr)
                                185px
                                155px;

                            gap: 20px;

                            align-items: center;

                            padding: 18px;

                            background: white;

                            border: 1px solid #e7eaf0;

                            border-radius: 14px;

                            box-shadow:
                                0 8px 24px
                                rgba(15, 23, 42, 0.05);
                        }

                        .cart-item img {
                            width: 145px;
                            height: 180px;

                            object-fit: contain;

                            border-radius: 10px;

                            background: #f5f6fa;
                        }

                        .cart-item-info {
                            display: flex;
                            flex-direction: column;

                            gap: 8px;
                        }

                        .cart-item-info h3 {
                            margin: 0;

                            font-size: 20px;
                        }

                        .cart-item-info p {
                            margin: 0;

                            color: #6b7280;
                        }

                        .price {
                            color: #6c5ce7;

                            font-size: 17px;
                            font-weight: 700;
                        }

                        .stock-text {
                            width: fit-content;

                            padding: 5px 9px;

                            border-radius: 999px;

                            background: #eef2ff;
                            color: #4f46e5;

                            font-size: 12px;
                            font-weight: 600;
                        }

                        .quantity-form {
                            display: flex;
                            flex-direction: column;

                            gap: 9px;
                        }

                        .quantity-form label {
                            font-size: 13px;
                            font-weight: 600;

                            color: #4b5563;
                        }

                        .qty-control {
                            display: grid;

                            grid-template-columns:
                                38px 1fr 38px;

                            height: 40px;

                            overflow: hidden;

                            border: 1px solid #d7dce5;
                            border-radius: 9px;

                            background: white;
                        }

                        .qty-control button {
                            border: none;

                            background: #6c5ce7;
                            color: white;

                            font-size: 18px;
                            font-weight: 700;

                            cursor: pointer;
                        }

                        .qty-control button:hover {
                            background: #5948dc;
                        }

                        .qty-control input {
                            width: 100%;

                            min-width: 0;

                            border: none;

                            outline: none;

                            text-align: center;

                            font-weight: 600;
                        }

                        .link-btn {
                            height: 36px;

                            border: none;

                            border-radius: 8px;

                            background: #eef2ff;
                            color: #4f46e5;

                            font-weight: 650;

                            cursor: pointer;
                        }

                        .link-btn:hover {
                            background: #e0e7ff;
                        }

                        .cart-item-subtotal {
                            display: flex;
                            flex-direction: column;

                            align-items: flex-end;

                            gap: 8px;
                        }

                        .cart-item-subtotal span {
                            font-size: 13px;

                            color: #6b7280;
                        }

                        .cart-item-subtotal strong {
                            font-size: 18px;
                        }

                        .remove-btn {
                            padding: 8px 14px;

                            border: 1px solid #fecaca;

                            border-radius: 8px;

                            background: #fff5f5;
                            color: #dc2626;

                            font-weight: 650;

                            cursor: pointer;
                        }

                        .remove-btn:hover {
                            background: #fee2e2;
                        }

                        .cart-summary {
                            position: sticky;

                            top: 30px;

                            padding: 22px;

                            border: 1px solid #e7eaf0;
                            border-radius: 14px;

                            background: white;

                            box-shadow:
                                0 8px 24px
                                rgba(15, 23, 42, 0.05);
                        }

                        .cart-summary h3 {
                            margin: 0 0 20px;
                        }

                        .cart-summary > div {
                            display: flex;

                            justify-content: space-between;

                            padding: 10px 0;
                        }

                        .summary-total {
                            margin-top: 4px;

                            padding-top: 16px !important;

                            border-top: 1px solid #e5e7eb;
                        }

                        .summary-total strong {
                            color: #6c5ce7;

                            font-size: 18px;
                        }

                        .checkout-btn {
                            width: 100%;
                            height: 44px;

                            margin-top: 16px;

                            display: flex;

                            align-items: center;
                            justify-content: center;

                            border-radius: 9px;

                            background: linear-gradient(
                                135deg,
                                #6c5ce7,
                                #8b5cf6
                            );

                            color: white;

                            text-decoration: none;

                            font-weight: 700;
                        }

                        .checkout-btn:hover {
                            box-shadow:
                                0 8px 18px
                                rgba(108, 92, 231, 0.25);
                        }

                        .cart-summary small {
                            display: block;

                            margin-top: 10px;

                            text-align: center;

                            color: #6b7280;
                        }

                        .empty-state {
                            padding: 60px 20px;

                            text-align: center;

                            background: white;

                            border: 1px solid #e7eaf0;

                            border-radius: 14px;
                        }

                        .empty-state a {
                            display: inline-block;

                            margin-top: 12px;

                            padding: 10px 18px;

                            border-radius: 8px;

                            background: #6c5ce7;
                            color: white;

                            text-decoration: none;

                            font-weight: 650;
                        }

                        @media(max-width:1000px) {

                            .cart-layout {
                                grid-template-columns: 1fr;
                            }

                            .cart-summary {
                                position: static;
                            }

                            .cart-item {
                                grid-template-columns:
                                    120px 1fr 170px;
                            }

                            .cart-item-subtotal {
                                grid-column: 2 / 4;

                                flex-direction: row;

                                justify-content: flex-end;

                                align-items: center;
                            }
                        }

                        @media(max-width:700px) {

                            .cart-page {
                                padding: 24px 14px;
                            }

                            .section-header {
                                align-items: flex-start;

                                flex-direction: column;
                            }

                            .cart-item {
                                grid-template-columns:
                                    100px 1fr;
                            }

                            .cart-item img {
                                width: 100px;
                                height: 135px;
                            }

                            .quantity-form,
                            .cart-item-subtotal {
                                grid-column: 1 / 3;
                            }
                        }

                    </style>

                </head>

                <body>
                """);


        // HEADER
        out.println(
                "<header class='topbar'>" +

                        "<div class='topbar-inner'>" +

                        "<a class='logo' href='" +
                        ctx +
                        "/home'>BookStore</a>" +

                        "<nav class='nav'>" +

                        "<a href='" +
                        ctx +
                        "/home'>Trang chủ</a>" +

                        "<a href='" +
                        ctx +
                        "/home'>Sản phẩm</a>" +

                        "<a href='" +
                        ctx +
                        "/cart'>Giỏ hàng (" +
                        cartCount +
                        ")</a>" +

                        "</nav>" +

                        "</div>" +

                        "</header>"
        );


        out.println(
                "<section class='cart-page'>"
        );


        out.println(
                "<div class='section-header'>" +

                        "<div>" +

                        "<span class='section-small'>" +
                        "GIỎ HÀNG" +
                        "</span>" +

                        "<h1>" +
                        "Giỏ hàng của bạn" +
                        "</h1>" +

                        "</div>" +

                        "<a class='secondary-btn' href='" +
                        ctx +
                        "/home'>" +

                        "← Tiếp tục mua" +

                        "</a>" +

                        "</div>"
        );


        // MESSAGE

        Object msg =
                session.getAttribute(
                        "cartMessage"
                );

        if (msg != null) {

            out.println(
                    "<div class='success cart-alert'>" +

                            esc(
                                    String.valueOf(msg)
                            ) +

                            "</div>"
            );

            session.removeAttribute(
                    "cartMessage"
            );
        }


        Object err =
                session.getAttribute(
                        "cartError"
                );

        if (err != null) {

            out.println(
                    "<div class='error cart-alert'>" +

                            esc(
                                    String.valueOf(err)
                            ) +

                            "</div>"
            );

            session.removeAttribute(
                    "cartError"
            );
        }


        // GIỎ TRỐNG

        if (cart.isEmpty()) {

            out.println(
                    "<div class='empty-state'>" +

                            "<div style='font-size:44px'>" +
                            "🛒" +
                            "</div>" +

                            "<h3>" +
                            "Giỏ hàng đang trống" +
                            "</h3>" +

                            "<p>" +
                            "Hãy chọn sách bạn muốn mua." +
                            "</p>" +

                            "<a href='" +
                            ctx +
                            "/home'>" +

                            "Xem sách" +

                            "</a>" +

                            "</div>"
            );

        } else {

            // CÓ SẢN PHẨM

            out.println(
                    "<div class='cart-layout'>" +

                            "<div class='cart-list'>"
            );


            for (
                    CartItem_24110282 item
                    : cart.values()
            ) {

                Book_24110282 book =
                        item.getBook();


                int stock =
                        book.getQuantity() == null
                                ? 0
                                : book.getQuantity();


                String image =
                        book.getCoverImage() == null
                                ? ""
                                : book.getCoverImage();


                out.println(
                        "<div class='cart-item'>"
                );


                // IMAGE

                out.println(
                        "<img " +

                                "src='" +
                                ctx +
                                "/book-image/" +
                                escAttr(image) +
                                "' " +

                                "alt='" +
                                escAttr(
                                        book.getTitle()
                                ) +
                                "'>"
                );


                // INFO

                out.println(
                        "<div class='cart-item-info'>" +

                                "<h3>" +
                                esc(
                                        book.getTitle()
                                ) +
                                "</h3>" +

                                "<p>" +
                                esc(
                                        book.getAuthorName()
                                ) +
                                "</p>" +

                                "<span class='price'>" +

                                money(
                                        book.getPrice()
                                ) +

                                " ₫" +

                                "</span>" +

                                "<span class='stock-text'>" +

                                "Còn " +
                                stock +
                                " cuốn" +

                                "</span>" +

                                "</div>"
                );


                // QUANTITY

                out.println(
                        "<form " +

                                "class='quantity-form' " +

                                "method='post' " +

                                "action='" +
                                ctx +
                                "/cart'>" +


                                "<input " +
                                "type='hidden' " +
                                "name='action' " +
                                "value='update'>" +


                                "<input " +
                                "type='hidden' " +
                                "name='bookId' " +
                                "value='" +
                                book.getBookId() +
                                "'>" +


                                "<label>" +
                                "Số lượng" +
                                "</label>" +


                                "<div class='qty-control'>" +


                                "<button " +
                                "type='button' " +
                                "onclick='changeQty(this,-1)'>" +

                                "−" +

                                "</button>" +


                                "<input " +
                                "type='number' " +
                                "name='quantity' " +

                                "value='" +
                                item.getQuantity() +
                                "' " +

                                "min='1' " +

                                "max='" +
                                stock +
                                "' " +

                                "required>" +


                                "<button " +
                                "type='button' " +
                                "onclick='changeQty(this,1)'>" +

                                "+" +

                                "</button>" +


                                "</div>" +


                                "<button " +
                                "class='link-btn' " +
                                "type='submit'>" +

                                "Cập nhật" +

                                "</button>" +


                                "</form>"
                );


                // SUBTOTAL

                out.println(
                        "<div class='cart-item-subtotal'>" +

                                "<span>" +
                                "Thành tiền" +
                                "</span>" +

                                "<strong>" +

                                money(
                                        item.getSubtotal()
                                ) +

                                " ₫" +

                                "</strong>" +


                                "<form " +
                                "method='post' " +
                                "action='" +
                                ctx +
                                "/cart'>" +


                                "<input " +
                                "type='hidden' " +
                                "name='action' " +
                                "value='remove'>" +


                                "<input " +
                                "type='hidden' " +
                                "name='bookId' " +

                                "value='" +
                                book.getBookId() +
                                "'>" +


                                "<button " +
                                "class='remove-btn' " +
                                "type='submit'>" +

                                "Xóa" +

                                "</button>" +


                                "</form>" +

                                "</div>"
                );


                out.println(
                        "</div>"
                );
            }


            out.println(
                    "</div>"
            );


            // SUMMARY

            out.println(
                    "<aside class='cart-summary'>" +

                            "<h3>" +
                            "Tóm tắt đơn hàng" +
                            "</h3>" +


                            "<div>" +

                            "<span>" +
                            "Tổng số lượng" +
                            "</span>" +

                            "<strong>" +
                            cartCount +
                            "</strong>" +

                            "</div>" +


                            "<div class='summary-total'>" +

                            "<span>" +
                            "Tổng cộng" +
                            "</span>" +

                            "<strong>" +

                            money(
                                    cartTotal
                            ) +

                            " ₫" +

                            "</strong>" +

                            "</div>" +


                            "<a " +
                            "class='checkout-btn' " +

                            "href='" +
                            ctx +
                            "/checkout'>" +

                            "Thanh toán COD" +

                            "</a>" +


                            "<small>" +
                            "Thanh toán bằng tiền mặt khi nhận hàng." +
                            "</small>" +


                            "</aside>"
            );


            out.println(
                    "</div>"
            );
        }


        out.println(
                "</section>"
        );


        out.println("""
                <script>

                function changeQty(button, delta) {

                    const input =
                        button.parentElement
                              .querySelector(
                                  'input[type=number]'
                              );

                    const min =
                        Number(
                            input.min || 1
                        );

                    const max =
                        Number(
                            input.max || 9999
                        );

                    let value =
                        Number(
                            input.value || min
                        );

                    value += delta;

                    if (value < min) {
                        value = min;
                    }

                    if (value > max) {
                        value = max;
                    }

                    input.value = value;
                }

                </script>

                </body>
                </html>
                """);
    }


    // ============================
    // POST - THÊM / SỬA / XÓA
    // ============================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        HttpSession session =
                request.getSession();

        Map<Integer, CartItem_24110282> cart =
                getCart(session);

        String action =
                request.getParameter(
                        "action"
                );

        try {

            int bookId =
                    Integer.parseInt(
                            request.getParameter(
                                    "bookId"
                            )
                    );


            Book_24110282 book =
                    bookService.getBookById(
                            bookId
                    );


            if (book == null) {

                session.setAttribute(
                        "cartError",
                        "Không tìm thấy sách."
                );

                response.sendRedirect(
                        request.getContextPath()
                                + "/cart"
                );

                return;
            }


            int stock =
                    book.getQuantity() == null
                            ? 0
                            : book.getQuantity();


            // XÓA

            if ("remove".equals(action)) {

                cart.remove(bookId);

                session.setAttribute(
                        "cartMessage",
                        "Đã xóa sách khỏi giỏ hàng."
                );
            }


            // UPDATE

            else if (
                    "update".equals(action)
            ) {

                int quantity =
                        parseQuantity(
                                request.getParameter(
                                        "quantity"
                                ),
                                1
                        );


                if (quantity < 1) {
                    quantity = 1;
                }


                if (quantity > stock) {

                    quantity = stock;

                    session.setAttribute(
                            "cartError",

                            "Số lượng tối đa của '"
                                    + book.getTitle()
                                    + "' là "
                                    + stock
                                    + " cuốn."
                    );
                }


                if (stock <= 0) {

                    cart.remove(bookId);

                } else {

                    cart.put(
                            bookId,

                            new CartItem_24110282(
                                    book,
                                    quantity
                            )
                    );


                    if (
                            session.getAttribute(
                                    "cartError"
                            ) == null
                    ) {

                        session.setAttribute(
                                "cartMessage",
                                "Đã cập nhật số lượng."
                        );
                    }
                }
            }


            // ADD

            else {

                int quantity =
                        parseQuantity(
                                request.getParameter(
                                        "quantity"
                                ),
                                1
                        );


                if (stock <= 0) {

                    session.setAttribute(
                            "cartError",

                            "Sách '"
                                    + book.getTitle()
                                    + "' đã hết hàng."
                    );

                } else {

                    int current =
                            cart.containsKey(bookId)
                                    ? cart.get(bookId)
                                          .getQuantity()
                                    : 0;


                    int desired =
                            current
                                    + Math.max(
                                            quantity,
                                            1
                                    );


                    int accepted =
                            Math.min(
                                    desired,
                                    stock
                            );


                    cart.put(
                            bookId,

                            new CartItem_24110282(
                                    book,
                                    accepted
                            )
                    );


                    if (desired > stock) {

                        session.setAttribute(
                                "cartError",

                                "Đã giới hạn '"
                                        + book.getTitle()
                                        + "' ở tối đa "
                                        + stock
                                        + " cuốn."
                        );

                    } else {

                        session.setAttribute(
                                "cartMessage",
                                "Đã thêm sách vào giỏ hàng."
                        );
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            session.setAttribute(
                    "cartError",
                    "Không thể cập nhật giỏ hàng."
            );
        }


        updateCartSummary(
                session,
                cart
        );


        response.sendRedirect(
                request.getContextPath()
                        + "/cart"
        );
    }


    // ============================
    // HELPER
    // ============================

    private int parseQuantity(
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


    private String money(
            BigDecimal value
    ) {

        if (value == null) {
            return "0";
        }


        BigDecimal result =
                value.multiply(
                        BigDecimal.valueOf(
                                1000
                        )
                );


        return String.format(
                        "%,.0f",
                        result
                )
                .replace(
                        ',',
                        '.'
                );
    }


    private String esc(
            String value
    ) {

        if (value == null) {
            return "";
        }


        return value
                .replace(
                        "&",
                        "&amp;"
                )
                .replace(
                        "<",
                        "&lt;"
                )
                .replace(
                        ">",
                        "&gt;"
                )
                .replace(
                        "\"",
                        "&quot;"
                )
                .replace(
                        "'",
                        "&#39;"
                );
    }


    private String escAttr(
            String value
    ) {

        return esc(value);
    }
}