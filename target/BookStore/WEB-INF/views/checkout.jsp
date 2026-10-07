<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html><head><title>Thanh toán COD</title></head><body>
<section class="checkout-page">
    <div class="section-header"><div><span class="section-small">THANH TOÁN</span><h1>Đặt hàng COD</h1></div></div>
    <c:if test="${not empty error}"><div class="error cart-alert">${error}</div></c:if>
    <div class="checkout-layout">
        <form class="checkout-form" method="post" action="${pageContext.request.contextPath}/checkout">
            <h3>Thông tin nhận hàng</h3>
            <label>Họ và tên *</label>
            <input name="customerName" value="${not empty param.customerName ? param.customerName : sessionScope.user.fullname}" required>
            <label>Số điện thoại *</label>
            <input name="phone" value="${param.phone}" maxlength="15" required>
            <label>Địa chỉ nhận hàng *</label>
            <textarea name="address" rows="3" required>${param.address}</textarea>
            <label>Ghi chú</label>
            <textarea name="note" rows="3" placeholder="Ví dụ: giao giờ hành chính">${param.note}</textarea>
            <div class="payment-box">
                <strong>Phương thức thanh toán</strong>
                <label class="cod-option"><input type="radio" checked disabled> COD - Thanh toán khi nhận hàng</label>
            </div>
            <div class="checkout-actions">
                <a class="btn secondary-btn" href="${pageContext.request.contextPath}/cart">← Quay lại giỏ</a>
                <button class="btn" type="submit">Xác nhận đặt hàng</button>
            </div>
        </form>
        <aside class="cart-summary checkout-summary">
            <h3>Đơn hàng</h3>
            <c:forEach var="entry" items="${cart}">
                <c:set var="item" value="${entry.value}"/>
                <div class="checkout-line"><span>${item.book.title} × ${item.quantity}</span>
                    <strong><fmt:formatNumber value="${item.subtotal * 1000}" pattern="#,##0"/> ₫</strong></div>
            </c:forEach>
            <div class="summary-total"><span>Tổng thanh toán</span>
                <strong><fmt:formatNumber value="${total * 1000}" pattern="#,##0"/> ₫</strong></div>
        </aside>
    </div>
</section>
</body></html>
