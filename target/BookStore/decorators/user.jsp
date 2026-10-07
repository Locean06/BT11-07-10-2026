<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        <% out.write("<sitemesh:write property=\"title\"/>"); %>
    </title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">

    <% out.write("<sitemesh:write property=\"head\"/>"); %>
</head>

<body>

<header class="site-header">

    <div class="container header-inner">

        <a class="brand"
           href="${pageContext.request.contextPath}/home">
            BookStore
        </a>

        <nav>

            <a href="${pageContext.request.contextPath}/home">
                Trang Chủ
            </a>

            <a href="${pageContext.request.contextPath}/home">
                Sản phẩm
            </a>

            <a class="cart-nav" href="${pageContext.request.contextPath}/cart">
                🛒 Giỏ hàng
                <c:if test="${not empty sessionScope.cartCount && sessionScope.cartCount > 0}">
                    <span class="cart-count">${sessionScope.cartCount}</span>
                </c:if>
            </a>

            <c:choose>

                <c:when test="${empty sessionScope.user}">

                    <a href="${pageContext.request.contextPath}/login">
                        Đăng nhập
                    </a>

                </c:when>

                <c:otherwise>

                    <span class="hello">
                        ${sessionScope.user.fullname}
                    </span>

                    <a href="${pageContext.request.contextPath}/logout">
                        Đăng xuất
                    </a>

                </c:otherwise>

            </c:choose>

            <c:if test="${not empty sessionScope.user && sessionScope.user.admin}">

                <a href="${pageContext.request.contextPath}/admin">
                    Trang quản trị
                </a>

            </c:if>

        </nav>

    </div>

</header>


<main class="container content">

    <% out.write("<sitemesh:write property=\"body\"/>"); %>

</main>


<footer class="site-footer">

    <div class="container">

        Họ tên: Kiên Nguyễn Xuân Lưu |
        MSSV: 24110282 |
        Mã đề: 2

    </div>

</footer>

</body>

</html>