<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctxPath = request.getContextPath();
%>

<!-- Custom CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/book/bookList.css" />

<jsp:include page="/WEB-INF/header1.jsp" />



<style>
.page-title {
    text-align: center;
    font-size: 28px;
    font-weight: bold;
    margin-top: 100px;
    margin-right: 20px;
    color: #333;
}

.container {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 25px;
    padding: 40px;
    margin: 0 auto;  /* ✅ 중앙 정렬 */
    justify-items: center;
    background-color: #fff;
    max-width: 1200px; /* 선택사항: 너무 넓어지지 않도록 */
}


.card {
    width: 230px;
    height: 400px; 
    background-color: #fffef5;
    border: 1px solid #f4c900;
    border-radius: 16px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    transition: transform 0.2s ease;
}
.card:hover {
    transform: translateY(-5px);
}
.image-box {
    width: 100%;
    height: 300px;
    background-color: #fafafa;
    display: flex;
    align-items: center;
    justify-content: center;
    border-bottom: 1px solid #eee;
}
.image-box img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}
.card-body {
    padding: 13px;
    text-align: center;
}
.card-title {
    font-size: 14px;
    font-weight: bold;
    margin-bottom: 8px;
    color: #222;
}
.card-price {
    font-size: 16px;
    color: #666;
}
.card-link {
    text-decoration: none;
    color: inherit;
    display: block;
    width: 100%;
    height: 100%;
}
.sort-box {
    margin: 20px 0 0 4006px;  /* 책 목록과 정렬 위치 맞춤 */

}

.sort-select {
    padding: 6px 12px;
    font-size: 14px;
    border: 1px solid #f4c900;
    border-radius: 8px;
    background-color: #fffef5;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    cursor: pointer;
    transition: all 0.2s ease-in-out;
}

.sort-select:hover {
    background-color: #fff9d6;
}

</style>

>>>>>>> refs/heads/jjunhyung
<!-- 카테고리 제목 출력 -->
<c:if test="${not empty bookList}">
    <div class="page-title">
        <c:choose>
            <c:when test="${category eq 'all'}">
                전체 도서
            </c:when>
            <c:otherwise>
                ${bookList[0].cvo.categoryname}
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<!-- 정렬 기준 선택 드롭다운 -->
<div class="sort-box">
    <form method="get" action="<c:url value='/myshop/booklist.go' />">
        <input type="hidden" name="category" value="${category}" />
        <select name="sort" onchange="this.form.submit()" class="sort-select">
            <option value="">-- 정렬 선택 --</option>
            <option value="new" ${sort eq 'new' ? 'selected' : ''}>입고일 순</option>
            <option value="sales" ${sort eq 'sales' ? 'selected' : ''}>판매순</option> <!-- 이 부분 추가 -->
        </select>
    </form>
</div>



<div class="container">
    <c:forEach var="book" items="${bookList}">
    <a href="<c:url value='/myshop/bookdetail.go?bookseq=${book.bookseq}' />" class="card-link">
        <div class="card">
            <div class="image-box">
                <c:choose>
                    <c:when test="${not empty book.bimage}">
                        <img src="${pageContext.request.contextPath}/images/${book.bimage}" alt="책 이미지" />
                    </c:when>
                    <c:otherwise>
                        책 이미지
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="card-body">
                <div class="card-title">${book.bname}</div>
                <div class="card-price">
                    <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₩" />
                </div>
            </div>
        </div>
    </a>
    </c:forEach>
</div>

<jsp:include page="/WEB-INF/footer.jsp" />
