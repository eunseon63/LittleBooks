<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
    margin-left: 190px;
    justify-items: center;
    background-color: #fff;
}

.card {
    width: 230px;
    height: 370x; 
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

</style>

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

<div class="container">
    <c:forEach var="book" items="${bookList}">
    <a href="<c:url value='/myshop/bookdetail.go?bookseq=${book.bookseq}' />" class="card-link">
        <div class="card">
            <div class="image-box">
                <c:choose>
                    <c:when test="${not empty book.bimage}">
                        <img src="<c:url value='/images/${book.bimage}'/>" alt="책 이미지" />
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
