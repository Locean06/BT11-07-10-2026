<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="vi_VN"/>

<html>
<head>
    <title>Quản lý Books</title>
</head>

<body>

<div class="admin-heading">

    <h1>Quản lý sách</h1>

    <a
        class="btn add-book-btn"
        href="${pageContext.request.contextPath}/admin/books?action=create&page=${currentPage}"
    >
        + Thêm sách
    </a>

</div>

<div class="table-wrap">

    <table>

        <thead>
        <tr>
            <th>ID</th>
            <th>ISBN</th>
            <th>Tiêu đề</th>
            <th>Tác giả</th>
            <th>Nhà xuất bản</th>
            <th>Giá</th>
            <th>Số lượng</th>
            <th>Thao tác</th>
        </tr>
        </thead>

        <tbody>

        <c:forEach var="book" items="${books}">

            <tr>

                <td>${book.bookId}</td>

                <td>${book.isbn}</td>

                <td>${book.title}</td>

                <td>${book.authorName}</td>

                <td>${book.publisher}</td>

                <td>
                    <fmt:formatNumber
                        value="${book.price * 1000}"
                        pattern="#,##0"
                    /> ₫
                </td>

                <td>${book.quantity}</td>

                <td class="actions">

                    <a
                        class="btn edit small"
                        href="${pageContext.request.contextPath}/admin/books?action=edit&id=${book.bookId}&page=${currentPage}"
                    >
                        Sửa
                    </a>

                    <form
                        method="post"
                        action="${pageContext.request.contextPath}/admin/books"
                        onsubmit="return confirm('Bạn có chắc muốn xóa sách này?');"
                    >

                        <input
                            type="hidden"
                            name="action"
                            value="delete"
                        >

                        <input
                            type="hidden"
                            name="id"
                            value="${book.bookId}"
                        >

                        <input
                            type="hidden"
                            name="page"
                            value="${currentPage}"
                        >

                        <button
                            class="btn danger small"
                            type="submit"
                        >
                            Xóa
                        </button>

                    </form>

                </td>

            </tr>

        </c:forEach>

        </tbody>

    </table>

</div>

<div class="pagination">

    <c:if test="${currentPage > 1}">

        <a
            href="${pageContext.request.contextPath}/admin/books?page=${currentPage - 1}"
        >
            ← Trang trước
        </a>

    </c:if>

    <c:forEach
        begin="1"
        end="${totalPages}"
        var="p"
    >

        <a
            class="${p == currentPage ? 'active' : ''}"
            href="${pageContext.request.contextPath}/admin/books?page=${p}"
        >
            ${p}
        </a>

    </c:forEach>

    <c:if test="${currentPage < totalPages}">

        <a
            href="${pageContext.request.contextPath}/admin/books?page=${currentPage + 1}"
        >
            Trang sau →
        </a>

    </c:if>

</div>

</body>
</html>