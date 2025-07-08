<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctxPath = request.getContextPath();
%>

<jsp:include page="/WEB-INF/header1.jsp" />

<!-- ✅ 인라인 CSS 스타일 -->
<style>
    .page-title {
        font-size: 2rem;
        font-weight: bold;
        text-align: center;
        margin: 100px 0 40px 0;
        color: #343a40;
    }

    .container {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        gap: 20px;
        padding: 0 20px 60px 20px;
    }

    .card-link {
        text-decoration: none;
        color: inherit;
    }

    .card {
        width: 200px;
        border: 2px solid #ffe082; /* 연노랑 테두리 */
        border-radius: 10px;
        overflow: hidden;
        background-color: #fffdf7;
        transition: transform 0.2s ease-in-out, box-shadow 0.2s, border-color 0.2s;
    }

    .card:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 16px rgba(255, 193, 7, 0.3); /* 노란 그림자 */
        border-color: #ffc107; /* hover 시 진한 노랑 */
    }

    .image-box {
        width: 100%;
        height: 270px;
        overflow: hidden;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: #fff8e1; /* 살짝 노란 배경 */
    }

    .image-box img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .card-body {
        padding: 10px 15px;
        text-align: center;
    }

    .card-title {
        font-size: 1.05rem;
        font-weight: 600;
        margin-bottom: 5px;
        color: #343a40;
    }

    .card-price {
        font-size: 0.95rem;
        
        font-weight: 500;
    }
</style>

<!-- ✅ 페이지 제목 -->
<div class="page-title">
    BEST 도서 목록
</div>

<!-- ✅ 도서 목록 출력 -->
<div class="container">
    <c:forEach var="book" items="${bestBooks}">
        <a href="<c:url value='/myshop/bookdetail.go?bookseq=${book.bookseq}' />" class="card-link">
            <div class="card">
                <div class="image-box">
                    <c:choose>
                        <c:when test="${not empty book.bimage}">
                            <img src="${pageContext.request.contextPath}/images/${book.bimage}" alt="${book.bname}" />
                        </c:when>
                        <c:otherwise>
                            <span>이미지 없음</span>
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
