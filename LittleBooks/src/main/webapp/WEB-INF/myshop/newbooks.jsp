<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctxPath = request.getContextPath();
%>

<!-- Custom CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath %>/css/book/bookList.css" />

<jsp:include page="/WEB-INF/header1.jsp" />

<!-- NEW 도서 목록 페이지 -->
<div class="page-title">
    NEW 도서 목록
</div>

<div class="container">
    <c:forEach var="book" items="${newBooks}">
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
