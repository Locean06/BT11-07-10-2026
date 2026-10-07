<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="vi_VN"/>

<html>
<head>
    <title>${book.title}</title>
</head>
<body>

<div class="detail-card">
    <div class="detail-cover">
        <img
            src="${pageContext.request.contextPath}/book-image/${book.coverImage}"
            onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/placeholder.svg'"
            alt="${book.title}"
        />
    </div>

    <div class="detail-info">
        <h1>${book.title}</h1>

        <p><b>Mã ISBN:</b> ${book.isbn}</p>
        <p><b>Tác giả:</b> ${book.authorName}</p>
        <p><b>Nhà xuất bản:</b> ${book.publisher}</p>

        <p>
            <b>Ngày xuất bản:</b>
            <fmt:formatDate
                value="${book.publishDate}"
                pattern="dd/MM/yyyy"
            />
        </p>

        <p><b>Số lượng:</b> ${book.quantity}</p>

        <p>
            <b>Giá:</b>
            <span class="detail-price">
                <fmt:formatNumber
                    value="${book.price * 1000}"
                    pattern="#,##0"
                /> ₫
            </span>
        </p>

        <p><b>Mô tả:</b> ${book.description}</p>
        <p><b>Reviews:</b> ${reviewCount}</p>

        <c:choose>
            <c:when test="${book.quantity > 0}">
                <form class="add-cart-form" method="post" action="${pageContext.request.contextPath}/cart">
                    <input type="hidden" name="action" value="add" />
                    <input type="hidden" name="bookId" value="${book.bookId}" />
                    <label for="buyQty"><b>Số lượng mua:</b></label>
                    <input id="buyQty" type="number" name="quantity" value="1" min="1" max="${book.quantity}" required />
                    <button class="btn" type="submit">🛒 Thêm vào giỏ hàng</button>
                </form>
            </c:when>
            <c:otherwise>
                <div class="error cart-alert">Sách hiện đã hết hàng.</div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="reviews">
    <h2>Đánh giá</h2>

    <c:if test="${empty reviews}">
        <p>Chưa có đánh giá.</p>
    </c:if>

    <c:forEach var="r" items="${reviews}">
        <div class="review-item">
            <b>${r.userName}</b>
            <span> - ${r.rating}/5</span>
            <p>${r.reviewText}</p>
        </div>
    </c:forEach>
</div>

<div class="form-card">
    <h2>Thêm đánh giá</h2>

    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <form method="post" action="${pageContext.request.contextPath}/review">
                <input type="hidden" name="bookId" value="${book.bookId}" />

                <label>Điểm đánh giá</label>

                <select name="rating">
                    <option value="5">5 - Rất tốt</option>
                    <option value="4">4 - Tốt</option>
                    <option value="3">3 - Bình thường</option>
                    <option value="2">2 - Không tốt</option>
                    <option value="1">1 - Kém</option>
                </select>

                <label>Nội dung đánh giá</label>

                <textarea
                    name="reviewText"
                    rows="5"
                    placeholder="Nhập đánh giá của bạn..."
                    required
                ></textarea>

                <button type="submit">
                    Gửi đánh giá
                </button>
            </form>
        </c:when>

        <c:otherwise>
            <p>
                Vui lòng
                <a href="${pageContext.request.contextPath}/login">
                    đăng nhập
                </a>
                để thêm đánh giá.
            </p>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>