<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>

<head>
    <title>Trang chủ</title>

    <style>
        .book-card {
            display: flex;
            flex-direction: column;
            height: 100%;
        }

        .book-content {
            display: flex;
            flex-direction: column;
            flex: 1;
        }

        .book-footer {
            margin-top: auto;
            padding-top: 17px;

            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            gap: 15px;
        }

        .book-price {
            display: flex;
            flex-direction: column;
            gap: 3px;
        }

        .book-price span {
            color: #6b7280;
            font-size: 11px;
        }

        .book-price strong {
            color: #6c5ce7;
            font-size: 18px;
            white-space: nowrap;
        }

        .book-actions {
            width: 125px;

            display: flex;
            flex-direction: column;
            gap: 7px;
        }

        .book-actions form {
            width: 100%;
            margin: 0;
        }

        .book-actions .detail-btn,
        .book-actions .cart-btn {
            width: 100%;
            min-height: 36px;

            display: flex;
            align-items: center;
            justify-content: center;

            padding: 8px 10px;
            margin: 0;

            border: none;
            border-radius: 8px;

            background: linear-gradient(
                    135deg,
                    #6c5ce7,
                    #8b5cf6
            );

            color: white;

            font-size: 12px;
            font-weight: 650;
            text-decoration: none;

            box-sizing: border-box;
            cursor: pointer;
        }

        .book-actions .detail-btn:hover,
        .book-actions .cart-btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 6px 15px rgba(108, 92, 231, 0.25);
        }

        .out-of-stock-btn {
            width: 100%;
            min-height: 36px;

            display: flex;
            align-items: center;
            justify-content: center;

            padding: 8px 10px;

            border-radius: 8px;

            background: #9ca3af;
            color: white;

            font-size: 12px;
            font-weight: 650;
        }

        @media (max-width: 600px) {
            .book-footer {
                align-items: stretch;
                flex-direction: column;
            }

            .book-actions {
                width: 100%;
            }
        }
    </style>

</head>

<body>


<section class="hero">

    <div>

        <span class="hero-label">
            BOOKSTORE
        </span>

        <h1>
            Khám phá những cuốn sách hay
        </h1>

        <p>
            Tuyển chọn sách theo từng tác giả.
            Tìm cuốn sách phù hợp với bạn ngay hôm nay.
        </p>

        <a href="#books" class="btn hero-btn">
            Khám phá sách
        </a>

    </div>

    <div class="hero-decoration">
        📚
    </div>

</section>


<section class="author-section">

    <div class="section-header">

        <div>

            <span class="section-small">
                TÁC GIẢ
            </span>

            <h2>
                Khám phá theo tác giả
            </h2>

        </div>

    </div>


    <div class="author-tabs">

        <c:forEach var="author" items="${authors}">

            <a
                    class="${author.authorId == selectedAuthor.authorId ? 'active' : ''}"

                    href="${pageContext.request.contextPath}/home?authorId=${author.authorId}"
            >

                    ${author.authorName}

            </a>

        </c:forEach>

    </div>

</section>


<section id="books">

    <div class="section-header book-section-header">

        <div>

            <span class="section-small">
                DANH SÁCH SÁCH
            </span>

            <h2>
                    ${selectedAuthor.authorName}
            </h2>

        </div>


        <span class="book-count">

            Trang ${currentPage} / ${totalPages}

        </span>

    </div>


    <!-- KHÔNG CÓ SÁCH -->

    <c:if test="${empty books}">

        <div class="empty-state">

            <div class="empty-icon">
                📖
            </div>

            <h3>
                Chưa có sách
            </h3>

            <p>
                Hiện chưa có sách của tác giả này.
            </p>

        </div>

    </c:if>


    <!-- DANH SÁCH SÁCH -->

    <div class="book-grid">

        <c:forEach var="book" items="${books}">

            <article class="book-card">


                <!-- ẢNH SÁCH -->

                <div class="book-image">

                    <img
                            src="${pageContext.request.contextPath}/book-image/${book.coverImage}"

                            onerror="
                            this.onerror=null;
                            this.src='${pageContext.request.contextPath}/assets/images/placeholder.svg';
                            "

                            alt="${book.title}"
                    >


                    <span class="book-badge">

                        <c:choose>

                            <c:when test="${book.quantity > 0}">
                                Còn ${book.quantity}
                            </c:when>

                            <c:otherwise>
                                Hết hàng
                            </c:otherwise>

                        </c:choose>

                    </span>

                </div>


                <!-- NỘI DUNG -->

                <div class="book-content">


                    <!-- TÁC GIẢ -->

                    <span class="book-author">
                            ${book.authorName}
                    </span>


                    <!-- TÊN SÁCH -->

                    <h3>

                        <a href="${pageContext.request.contextPath}/book?id=${book.bookId}">

                                ${book.title}

                        </a>

                    </h3>


                    <!-- THÔNG TIN SÁCH -->

                    <div class="book-meta">


                        <div>

                            <span>
                                ISBN
                            </span>

                            <strong>
                                    ${book.isbn}
                            </strong>

                        </div>


                        <div>

                            <span>
                                Nhà xuất bản
                            </span>

                            <strong>
                                    ${book.publisher}
                            </strong>

                        </div>


                        <div>

                            <span>
                                Ngày xuất bản
                            </span>

                            <strong>
                                    ${book.publishDate}
                            </strong>

                        </div>


                    </div>


                    <!-- GIÁ + NÚT -->

                    <div class="book-footer">


                        <!-- GIÁ -->

                        <div class="book-price">

                            <span>
                                Giá
                            </span>

                            <strong>

                                <fmt:formatNumber
                                        value="${book.price * 1000}"
                                        pattern="#,##0"
                                />

                                ₫

                            </strong>

                        </div>


                        <!-- NÚT -->

                        <div class="book-actions">


                            <!-- CHI TIẾT -->

                            <a
                                    class="detail-btn"

                                    href="${pageContext.request.contextPath}/book?id=${book.bookId}"
                            >

                                Xem chi tiết →

                            </a>


                            <!-- CÒN HÀNG -->

                            <c:if test="${book.quantity > 0}">

                                <form
                                        method="post"

                                        action="${pageContext.request.contextPath}/cart"
                                >

                                    <input
                                            type="hidden"
                                            name="action"
                                            value="add"
                                    >


                                    <input
                                            type="hidden"
                                            name="bookId"
                                            value="${book.bookId}"
                                    >


                                    <input
                                            type="hidden"
                                            name="quantity"
                                            value="1"
                                    >


                                    <button
                                            class="cart-btn"
                                            type="submit"
                                    >

                                        + Giỏ hàng

                                    </button>

                                </form>

                            </c:if>


                            <!-- HẾT HÀNG -->

                            <c:if test="${book.quantity <= 0}">

                                <span class="out-of-stock-btn">

                                    Hết hàng

                                </span>

                            </c:if>


                        </div>


                    </div>


                </div>

            </article>

        </c:forEach>

    </div>


    <!-- PHÂN TRANG -->

    <c:if test="${totalPages > 1}">

        <div class="pagination">


            <!-- TRANG TRƯỚC -->

            <c:if test="${currentPage > 1}">

                <a
                        class="page-nav"

                        href="${pageContext.request.contextPath}/home?authorId=${selectedAuthor.authorId}&page=${currentPage - 1}"
                >

                    ← Trang trước

                </a>

            </c:if>


            <!-- SỐ TRANG -->

            <c:forEach
                    begin="1"
                    end="${totalPages}"
                    var="p"
            >

                <a
                        class="${p == currentPage ? 'active' : ''}"

                        href="${pageContext.request.contextPath}/home?authorId=${selectedAuthor.authorId}&page=${p}"
                >

                        ${p}

                </a>

            </c:forEach>


            <!-- TRANG SAU -->

            <c:if test="${currentPage < totalPages}">

                <a
                        class="page-nav"

                        href="${pageContext.request.contextPath}/home?authorId=${selectedAuthor.authorId}&page=${currentPage + 1}"
                >

                    Trang sau →

                </a>

            </c:if>


        </div>

    </c:if>


</section>


</body>

</html>