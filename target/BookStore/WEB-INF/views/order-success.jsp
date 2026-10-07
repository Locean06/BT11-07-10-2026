<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html><head><title>Đặt hàng thành công</title></head><body>
<div class="order-success">
    <div class="success-icon">✓</div>
    <h1>Đặt hàng thành công</h1>
    <p>Mã đơn hàng của bạn là <strong>#${orderId}</strong>.</p>
    <p>Phương thức thanh toán: <strong>COD - thanh toán khi nhận hàng</strong>.</p>
    <a class="btn" href="${pageContext.request.contextPath}/home">Tiếp tục mua sắm</a>
</div>
</body></html>
