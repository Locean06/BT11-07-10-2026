<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>${action == 'edit' ? 'Cập nhật sách' : 'Thêm sách'}</title>
</head>

<body>

<div class="form-card wide">

    <h1>${action == 'edit' ? 'Cập nhật sách' : 'Thêm sách'}</h1>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form
        method="post"
        action="${pageContext.request.contextPath}/admin/books"
        enctype="multipart/form-data"
    >

        <input
            type="hidden"
            name="action"
            value="${action}"
        >

        <input
            type="hidden"
            name="page"
            value="${currentPage}"
        >

        <c:if test="${action == 'edit'}">
            <input
                type="hidden"
                name="bookId"
                value="${book.bookId}"
            >
        </c:if>

        <input
            type="hidden"
            name="oldCoverImage"
            value="${book.coverImage}"
        >

        <div class="form-group">
            <label>ISBN</label>

            <input
                type="number"
                name="isbn"
                value="${book.isbn}"
            >
        </div>

        <div class="form-group">
            <label>Tiêu đề</label>

            <input
                type="text"
                name="title"
                value="${book.title}"
                required
            >
        </div>

        <div class="form-group">
            <label>Tác giả</label>

            <select
                name="authorId"
                required
            >
                <c:forEach
                    var="author"
                    items="${authors}"
                >
                    <option
                        value="${author.authorId}"
                        ${author.authorId == book.authorId ? 'selected' : ''}
                    >
                        ${author.authorName}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Nhà xuất bản</label>

            <input
                type="text"
                name="publisher"
                value="${book.publisher}"
            >
        </div>

        <div class="form-group">
            <label>Giá (nghìn đồng)</label>

            <input
                type="number"
                step="0.01"
                min="0"
                max="9999.99"
                name="price"
                value="${book.price}"
                placeholder="Ví dụ: 95 = 95.000 ₫"
            >

            <small class="form-note">
                Ví dụ nhập 95 sẽ hiển thị thành 95.000 ₫.
            </small>
        </div>

        <div class="form-group">
            <label>Ngày xuất bản</label>

            <fmt:formatDate
                value="${book.publishDate}"
                pattern="yyyy-MM-dd"
                var="publishDateValue"
            />

            <input
                type="date"
                name="publishDate"
                value="${publishDateValue}"
            >
        </div>

        <div class="form-group">
            <label>Số lượng</label>

            <input
                type="number"
                min="0"
                name="quantity"
                value="${book.quantity}"
            >
        </div>

        <div class="form-group">
            <label>Mô tả</label>

            <textarea
                name="description"
                rows="5"
            >${book.description}</textarea>
        </div>

        <div class="form-group">

            <label>Ảnh bìa</label>

            <div class="current-cover">
                <c:choose>

                    <c:when test="${not empty book.coverImage}">
                        <img
                            id="coverPreview"
                            src="${pageContext.request.contextPath}/assets/images/${book.coverImage}"
                            onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/placeholder.svg'"
                            alt="Ảnh bìa"
                        >
                    </c:when>

                    <c:otherwise>
                        <img
                            id="coverPreview"
                            src="${pageContext.request.contextPath}/assets/images/placeholder.svg"
                            alt="Ảnh bìa"
                        >
                    </c:otherwise>

                </c:choose>
            </div>

            <label class="upload-box">

                <span>Chọn ảnh từ máy</span>

                <small>
                    JPG, JPEG, PNG, WEBP - tối đa 5MB
                </small>

                <input
                    type="file"
                    name="coverFile"
                    id="coverFile"
                    accept=".jpg,.jpeg,.png,.webp,image/jpeg,image/png,image/webp"
                >

            </label>

        </div>

        <div class="form-actions">

            <button
                type="submit"
                class="btn save-book-btn"
            >
                Lưu sách
            </button>

            <a
                class="btn secondary"
                href="${pageContext.request.contextPath}/admin/books?page=${currentPage}"
            >
                Hủy
            </a>

        </div>

    </form>

</div>

<script>
    const coverFile = document.getElementById("coverFile");
    const coverPreview = document.getElementById("coverPreview");

    coverFile.addEventListener("change", function () {
        const file = this.files[0];

        if (!file) {
            return;
        }

        const reader = new FileReader();

        reader.onload = function (e) {
            coverPreview.src = e.target.result;
        };

        reader.readAsDataURL(file);
    });
</script>

</body>
</html>