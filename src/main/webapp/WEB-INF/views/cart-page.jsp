<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Giỏ hàng</title>

    <style>
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
            color: #111827;
        }

        .secondary-btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 11px 18px;
            border: 1px solid #d8dce6;
            border-radius: 9px;
            text-decoration: none;
            color: #374151;
            background: #ffffff;
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
            font-size: 14px;
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
            grid-template-columns: minmax(0, 1fr) 330px;
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
            grid-template-columns: 145px minmax(180px, 1fr) 185px 155px;
            gap: 20px;
            align-items: center;

            padding: 18px;

            background: #ffffff;
            border: 1px solid #e7eaf0;
            border-radius: 14px;

            box-shadow: 0 8px 24px rgba(15, 23, 42, 0.05);
        }

        .cart-item > img {
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
            color: #111827;
        }

        .cart-item-info p {
            margin: 0;
            color: #6b7280;
            font-size: 14px;
        }

        .cart-item-info strong {
            color: #6c5ce7;
            font-size: 17px;
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
            grid-template-columns: 38px 1fr 38px;
            height: 40px;

            overflow: hidden;

            border: 1px solid #d7dce5;
            border-radius: 9px;
            background: #ffffff;
        }

        .qty-control button {
            border: none;
            background: #6c5ce7;
            color: #ffffff;

            font-size: 18px;
            font-weight: 700;

            cursor: pointer;
        }

        .qty-control button:hover {
            background: #5846d9;
        }

        .qty-control input {
            width: 100%;
            min-width: 0;

            border: none;
            outline: none;

            text-align: center;
            font-size: 14px;
            font-weight: 600;

            -moz-appearance: textfield;
        }

        .qty-control input::-webkit-inner-spin-button,
        .qty-control input::-webkit-outer-spin-button {
            -webkit-appearance: none;
            margin: 0;
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

        .cart-item-subtotal > span {
            font-size: 13px;
            color: #6b7280;
        }

        .cart-item-subtotal > strong {
            font-size: 18px;
            color: #111827;
        }

        .cart-item-subtotal form {
            margin: 8px 0 0;
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
            top: 96px;

            padding: 22px;

            border: 1px solid #e7eaf0;
            border-radius: 14px;

            background: #ffffff;
            box-shadow: 0 8px 24px rgba(15, 23, 42, 0.05);
        }

        .cart-summary h3 {
            margin: 0 0 20px;
            font-size: 20px;
            color: #111827;
        }

        .cart-summary > div {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;

            padding: 10px 0;

            color: #4b5563;
            font-size: 14px;
        }

        .cart-summary > div strong {
            color: #111827;
        }

        .summary-total {
            margin-top: 4px;
            padding-top: 16px !important;

            border-top: 1px solid #e5e7eb;
        }

        .summary-total span,
        .summary-total strong {
            font-size: 18px !important;
            font-weight: 700;
        }

        .summary-total strong {
            color: #6c5ce7 !important;
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

            color: #ffffff;
            text-decoration: none;
            font-weight: 700;
        }

        .checkout-btn:hover {
            box-shadow: 0 8px 18px rgba(108, 92, 231, 0.25);
            transform: translateY(-1px);
        }

        .cart-summary small {
            display: block;
            margin-top: 10px;

            text-align: center;
            color: #6b7280;
            line-height: 1.5;
        }

        .empty-state {
            padding: 60px 20px;

            text-align: center;

            background: #ffffff;
            border: 1px solid #e7eaf0;
            border-radius: 14px;
        }

        .empty-icon {
            margin-bottom: 12px;
            font-size: 44px;
        }

        .empty-state h3 {
            margin: 0 0 8px;
        }

        .empty-state p {
            color: #6b7280;
        }

        .empty-state .btn {
            display: inline-flex;
            margin-top: 12px;
            padding: 10px 18px;

            border-radius: 8px;

            background: #6c5ce7;
            color: #ffffff;

            text-decoration: none;
            font-weight: 650;
        }

        @media (max-width: 1000px) {
            .cart-layout {
                grid-template-columns: 1fr;
            }

            .cart-summary {
                position: static;
            }

            .cart-item {
                grid-template-columns: 120px 1fr 170px;
            }

            .cart-item-subtotal {
                grid-column: 2 / 4;
                flex-direction: row;
                align-items: center;
                justify-content: flex-end;
            }

            .cart-item-subtotal form {
                margin: 0 0 0 8px;
            }
        }

        @media (max-width: 700px) {
            .cart-page {
                padding: 24px 14px 40px;
            }

            .section-header {
                align-items: flex-start;
                flex-direction: column;
            }

            .cart-item {
                grid-template-columns: 100px 1fr;
            }

            .cart-item > img {
                width: 100px;
                height: 135px;
            }

            .quantity-form {
                grid-column: 1 / 3;
            }

            .cart-item-subtotal {
                grid-column: 1 / 3;
                justify-content: space-between;
            }
        }
    </style>
</head>

<body>

<section class="cart-page">

    <!-- HEADER -->
    <div class="section-header">

        <div>
            <span class="section-small">
                GIỎ HÀNG
            </span>

            <h1>
                Giỏ hàng của bạn
            </h1>
        </div>

        <a
                class="secondary-btn"
                href="${pageContext.request.contextPath}/home"
        >
            ← Tiếp tục mua
        </a>

    </div>


    <!-- THÔNG BÁO -->
    <c:if test="${not empty sessionScope.cartMessage}">

        <div class="success cart-alert">
                ${sessionScope.cartMessage}
        </div>

        <c:remove
                var="cartMessage"
                scope="session"
        />

    </c:if>


    <c:if test="${not empty sessionScope.cartError}">

        <div class="error cart-alert">
                ${sessionScope.cartError}
        </div>

        <c:remove
                var="cartError"
                scope="session"
        />

    </c:if>


    <c:choose>

        <!-- GIỎ TRỐNG -->
        <c:when test="${empty cart}">

            <div class="empty-state">

                <div class="empty-icon">
                    🛒
                </div>

                <h3>
                    Giỏ hàng đang trống
                </h3>

                <p>
                    Hãy chọn sách bạn muốn mua.
                </p>

                <a
                        class="btn"
                        href="${pageContext.request.contextPath}/home"
                >
                    Xem sách
                </a>

            </div>

        </c:when>


        <!-- CÓ SẢN PHẨM -->
        <c:otherwise>

            <div class="cart-layout">


                <!-- DANH SÁCH -->
                <div class="cart-list">

                    <c:forEach
                            var="entry"
                            items="${cart}"
                    >

                        <c:set
                                var="item"
                                value="${entry.value}"
                        />


                        <div class="cart-item">


                            <!-- ẢNH -->
                            <img
                                    src="${pageContext.request.contextPath}/book-image/${item.book.coverImage}"

                                    onerror="
                                        this.onerror=null;
                                        this.src='${pageContext.request.contextPath}/assets/images/placeholder.svg';
                                    "

                                    alt="${item.book.title}"
                            >


                            <!-- THÔNG TIN -->
                            <div class="cart-item-info">

                                <h3>
                                        ${item.book.title}
                                </h3>

                                <p>
                                        ${item.book.authorName}
                                </p>

                                <strong>

                                    <fmt:formatNumber
                                            value="${item.book.price * 1000}"
                                            pattern="#,##0"
                                    />

                                    ₫

                                </strong>

                                <span class="stock-text">

                                    Còn ${item.book.quantity} cuốn

                                </span>

                            </div>


                            <!-- SỐ LƯỢNG -->
                            <form
                                    class="quantity-form"

                                    method="post"

                                    action="${pageContext.request.contextPath}/cart"
                            >

                                <input
                                        type="hidden"
                                        name="action"
                                        value="update"
                                >

                                <input
                                        type="hidden"
                                        name="bookId"
                                        value="${item.book.bookId}"
                                >


                                <label>
                                    Số lượng
                                </label>


                                <div class="qty-control">

                                    <button
                                            type="button"
                                            onclick="changeQty(this,-1)"
                                    >
                                        −
                                    </button>


                                    <input
                                            type="number"

                                            name="quantity"

                                            value="${item.quantity}"

                                            min="1"

                                            max="${item.book.quantity}"

                                            required
                                    >


                                    <button
                                            type="button"
                                            onclick="changeQty(this,1)"
                                    >
                                        +
                                    </button>

                                </div>


                                <button
                                        class="link-btn"
                                        type="submit"
                                >
                                    Cập nhật
                                </button>

                            </form>


                            <!-- THÀNH TIỀN -->
                            <div class="cart-item-subtotal">

                                <span>
                                    Thành tiền
                                </span>

                                <strong>

                                    <fmt:formatNumber
                                            value="${item.subtotal * 1000}"
                                            pattern="#,##0"
                                    />

                                    ₫

                                </strong>


                                <form
                                        method="post"
                                        action="${pageContext.request.contextPath}/cart"
                                >

                                    <input
                                            type="hidden"
                                            name="action"
                                            value="remove"
                                    >

                                    <input
                                            type="hidden"
                                            name="bookId"
                                            value="${item.book.bookId}"
                                    >


                                    <button
                                            class="remove-btn"
                                            type="submit"
                                    >
                                        Xóa
                                    </button>

                                </form>

                            </div>


                        </div>

                    </c:forEach>

                </div>


                <!-- TÓM TẮT -->
                <aside class="cart-summary">

                    <h3>
                        Tóm tắt đơn hàng
                    </h3>


                    <div>

                        <span>
                            Tổng số lượng
                        </span>

                        <strong>
                                ${sessionScope.cartCount}
                        </strong>

                    </div>


                    <div class="summary-total">

                        <span>
                            Tổng cộng
                        </span>

                        <strong>

                            <fmt:formatNumber
                                    value="${sessionScope.cartTotal * 1000}"
                                    pattern="#,##0"
                            />

                            ₫

                        </strong>

                    </div>


                    <a
                            class="checkout-btn"

                            href="${pageContext.request.contextPath}/checkout"
                    >
                        Thanh toán COD
                    </a>


                    <small>
                        Thanh toán bằng tiền mặt khi nhận hàng.
                    </small>

                </aside>


            </div>

        </c:otherwise>

    </c:choose>

</section>


<script>

    function changeQty(button, delta) {

        const wrapper =
            button.closest('.qty-control');

        const input =
            wrapper.querySelector(
                'input[type="number"]'
            );

        const min =
            Number(input.min || 1);

        const max =
            Number(input.max || 9999);

        let current =
            Number(input.value || min);

        current += delta;

        if (current < min) {
            current = min;
        }

        if (current > max) {
            current = max;
        }

        input.value = current;
    }

</script>

</body>
</html>