<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Đăng nhập</title>
</head>
<body>

<div class="form-card">
    <h1>Đăng nhập</h1>

    <c:if test="${param.registered == '1'}">
        <div class="success">Đăng ký thành công. Bạn có thể đăng nhập.</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <label>Email</label>
        <input type="email" name="email" value="${email}" required />

        <label>Mật khẩu</label>
        <input type="password" name="password" required />

        <button type="submit">Đăng nhập</button>
    </form>

    <p>
        Chưa có tài khoản?
        <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
    </p>
</div>

</body>
</html>
