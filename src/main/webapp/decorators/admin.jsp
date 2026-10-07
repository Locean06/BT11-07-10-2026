<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="vi">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Admin -
        <% out.write("<sitemesh:write property=\"title\"/>"); %>
    </title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">

    <% out.write("<sitemesh:write property=\"head\"/>"); %>

</head>

<body>

<header class="site-header admin-header">

    <div class="container header-inner">

        <a class="brand"
           href="${pageContext.request.contextPath}/admin">
            BookStore Admin
        </a>

        <nav>

            <a href="${pageContext.request.contextPath}/admin/books">
                Quản lý Books
            </a>

            <a href="${pageContext.request.contextPath}/home">
                Trang User
            </a>

            <a href="${pageContext.request.contextPath}/logout">
                Đăng xuất
            </a>

        </nav>

    </div>

</header>


<main class="container content">

    <% out.write("<sitemesh:write property=\"body\"/>"); %>

</main>


<footer class="site-footer">

    <div class="container">

        Họ tên: Kiên Nguyễn Xuân Lưu|
        MSSV: 24110282 |
        Mã đề: 2

    </div>

</footer>

</body>

</html>