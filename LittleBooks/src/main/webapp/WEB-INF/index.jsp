<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String ctxPath = request.getContextPath();
%>

<!--  Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" > 

<!--  Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script> 

<!-- jQueryUI -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
<script type="text/javascript" src="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script>

<!-- Custom CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/index/index.css" />

<jsp:include page="header1.jsp" />

<div class="container" style="width: 80%; height: 600px; margin: 100px auto 0 auto; padding: 20px;">
    <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">

        <!-- 총 책 수와 슬라이드당 아이템 수 설정 -->
        <c:set var="totalBooks" value="${fn:length(youthBooks)}" />
        <c:set var="itemsPerSlide" value="3" />
        
        <!-- 캐러셀 인디케이터 -->
        <ol class="carousel-indicators">
            <c:forEach var="i" begin="0" end="${totalBooks - 1}" step="${itemsPerSlide}" varStatus="status">
                <li data-target="#carouselExampleIndicators" data-slide-to="${status.index}" 
                    class="${status.index == 0 ? 'active' : ''}"></li>
            </c:forEach>
        </ol>

        <!-- 캐러셀 아이템 (3개씩 묶음) -->
        <div class="carousel-inner">
            <c:forEach var="i" begin="0" end="${totalBooks - 1}" step="${itemsPerSlide}" varStatus="status">
                <div class="carousel-item ${status.index == 0 ? 'active' : ''}">
                    <div class="row">
                        <c:forEach var="j" begin="${i}" end="${i + itemsPerSlide - 1}">
                            <c:if test="${j < totalBooks}">
                                <c:set var="book" value="${youthBooks[j]}" />
                                <div class="col-md-4">
                                    <div class="book-card">
                                        <img src="${pageContext.request.contextPath}/images/${book.image}" class="book-img" alt="${book.title}" />
                                        <div class="book-overlay">
                                            <div class="book-comment">${book.comment}</div>
                                        </div>
                                    </div>
                                    <div class="carousel-caption d-none d-md-block" style="background: rgba(0,0,0,0.4); border-radius: 5px;">
                                        <h5>${book.title}</h5>
                                        <p>${book.author}</p>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- 이전, 다음 버튼 -->
        <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>

    </div>
</div>

<jsp:include page="footer.jsp" />
