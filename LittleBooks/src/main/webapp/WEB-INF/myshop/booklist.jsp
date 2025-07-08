<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctxPath = request.getContextPath();
%>

<!-- Custom CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/book/bookList.css" />

<jsp:include page="/WEB-INF/header1.jsp" />

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
