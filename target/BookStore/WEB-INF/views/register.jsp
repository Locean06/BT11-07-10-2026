<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Đăng ký</title>
</head>
<body>

<div class="form-card">
    <h1>Đăng ký tài khoản</h1>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <c:if test="${not empty message}">
        <div class="success">${message}</div>
    </c:if>

    <c:choose>
        <c:when test="${otpSent}">
            <form method="post" action="${pageContext.request.contextPath}/register">
                <input type="hidden" name="action" value="verify" />

                <label>Email</label>
                <input type="text" value="${email}" disabled />

                <label>Nhập OTP</label>
                <input type="text" name="otp" maxlength="6" required />

                <button type="submit">Xác nhận OTP</button>
            </form>
        </c:when>

        <c:otherwise>
            <form method="post" action="${pageContext.request.contextPath}/register">
                <input type="hidden" name="action" value="send" />

                <label>Họ tên</label>
                <input type="text" name="fullname" required />

                <label>Email</label>
                <input type="email" name="email" required />

                <label>Số điện thoại</label>
                <input type="number" name="phone" />

                <label>Mật khẩu</label>
                <input type="password" name="password" required />

                <button type="submit">Gửi OTP</button>
            </form>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
