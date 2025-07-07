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

<!-- 캐러셀 감싸는 컨테이너: height 제거 -->
<div class="container" style="width: 80%; margin: 100px auto 0 auto; padding: 20px;">
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
                                <a href="${pageContext.request.contextPath}/myshop/bookdetail.go?bookseq=${book.bookseq}" style="text-decoration: none; color: inherit;">

                                    <div class="book-card">
                                        <img src="${pageContext.request.contextPath}/images/${book.bimage}" class="book-img" alt="${book.bname}" />
                                        <div class="book-overlay">
                                            <div class="book-comment">${book.bcontent}</div>
                                        </div>
                                    </div>
                                    <div class="carousel-caption d-none d-md-block" style="background: rgba(0,0,0,0.4); border-radius: 5px;">
                                        <h5>${book.bname}</h5>
                                        <p>${book.author}</p>
                                    </div>
                                </a>
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

<!-- 캐러셀과 추가 콘텐츠 사이 적당한 간격 주기 -->
<div class="container mt-5 mb-5">

    <!-- 📚 이번 주의 추천 도서 -->
    <div class="mb-5 p-4 bg-light border rounded shadow-sm">
    <a href="${pageContext.request.contextPath}/myshop/bookdetail.go?bookseq=166" style="text-decoration: none; color: inherit;">
        <h4>📚 이번 주의 추천 도서</h4>
        <div class="d-flex mt-3">
            <img src="${pageContext.request.contextPath}/images/동화8.jpg" alt="추천도서" style="width: 120px; height: auto; margin-right: 20px; border-radius: 10px;">
            <div>
                <h5>사과가 쿵!</h5>
                <p>어느 날 커다란 사과가 떨어졌어요. 가장 먼저 두더지가 사과를 갉아먹었어요. 토끼, 다람쥐, 돼지도 찾아와 사과를 갉아먹었지요. 악어, 사자, 기린, 그리고 코끼리도 와서 사과를 갉아먹었답니다. 그런데 갑자기 비가 내리는데…….</p>
                <small>저자: 다다 히로시 | 출판사: 꿈나무출판</small>
            </div>
        </div>
    </a>
    </div>

    <!-- 🗂 카테고리 바로가기 -->
    <div class="mb-5">
        <h4>🗂 카테고리 둘러보기</h4>
        <div class="d-flex flex-wrap gap-2 mt-3">

            <a href="<%= ctxPath %>/myshop/booklist.go?category=위인전" class="btn btn-outline-warning">위인전</a>
            <a href="<%= ctxPath %>/myshop/booklist.go?category=전래동화" class="btn btn-outline-warning">전래동화</a>
            <a href="<%= ctxPath %>/myshop/booklist.go?category=세계동화" class="btn btn-outline-warning">세계동화</a>
            <a href="<%= ctxPath %>/myshop/booklist.go?category=만화책" class="btn btn-outline-warning">만화책 시리즈</a>
        </div>
    </div>
	
	<h3>BEST 도서 목록</h3>
	<div class="book-list">
	    <c:forEach var="book" items="${bestBooks}" varStatus="status">
		    <c:if test="${status.index < 5}">
		    <a href="${pageContext.request.contextPath}/myshop/bookdetail.go?bookseq=${book.bookseq}" style="text-decoration: none; color: inherit;">
		        <div class="book-item">
		            <span class="book-title">${book.bname}</span>
		            <span class="book-author"> - ${book.author}</span>
		        </div>
		    </a>
		    </c:if>
		</c:forEach>
	</div>

	<h3>NEW 도서 목록</h3>
	<div class="book-list">
	    <c:forEach var="book" items="${newBooks}" varStatus="status">
		    <c:if test="${status.index < 5}">
		    <a href="${pageContext.request.contextPath}/myshop/bookdetail.go?bookseq=${book.bookseq}" style="text-decoration: none; color: inherit;">
		        <div class="book-item">
		            <span class="book-title">${book.bname}</span>
		            <span class="book-author"> - ${book.author}</span>
		        </div>
		    </a>
		    </c:if>
		</c:forEach>

	</div>
    
</div>

<style>
/* 전체 배경 및 폰트 */
body {
    background-color: #fafafa;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    color: #333;
}

/* 캐러셀 내 책 카드 */
.book-card {
    position: relative;
    overflow: hidden;
    border-radius: 12px;
    box-shadow: 0 6px 14px rgba(0,0,0,0.1);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    cursor: pointer;
    background-color: #fff;
}

.book-card:hover {
    transform: translateY(-8px);
    box-shadow: 0 14px 24px rgba(0,0,0,0.15);
}

.book-img {
    width: 100%;
    height: 430px;
    object-fit: cover;
    border-radius: 12px;
}

/* 책 설명 오버레이 */
.book-overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    padding: 10px 12px;
    background: linear-gradient(transparent, rgba(0,0,0,0.7));
    color: #fff;
    font-size: 0.9rem;
    opacity: 0;
    transition: opacity 0.3s ease;
    border-radius: 0 0 12px 12px;
}

.book-card:hover .book-overlay {
    opacity: 1;
}

/* 캐러셀 캡션 */
.carousel-caption {
    background: rgba(0,0,0,0.45);
    border-radius: 6px;
    padding: 8px 14px;
    max-width: 90%;
    left: 50%;
    transform: translateX(-50%);
}

.carousel-caption h5 {
    font-weight: 700;
    font-size: 1.1rem;
    margin-bottom: 4px;
    color: #fff;
}

.carousel-caption p {
    font-size: 0.9rem;
    color: #ddd;
    margin: 0;
}

/* 아래 추천 도서 박스 */
.container.mt-5.mb-5 > div {
    background: #fff;
    border-radius: 14px;
    box-shadow: 0 8px 20px rgba(0,0,0,0.05);
    padding: 30px 30px;
    transition: box-shadow 0.3s ease;
}

.container.mt-5.mb-5 > div:hover {
    box-shadow: 0 12px 32px rgba(0,0,0,0.1);
}

/* 추천 도서 이미지 */
.container.mt-5.mb-5 img {
    border-radius: 14px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

/* 카테고리 버튼 스타일 */
.btn-outline-warning {
    color: #d18e00;
    border-color: #d18e00;
    font-weight: 600;
    transition: all 0.25s ease;
}

.btn-outline-warning:hover,
.btn-outline-warning:focus {
    background-color: #d18e00;
    color: #fff;
    border-color: #b07900;
}

/* 사용자 리뷰 리스트 */
.list-group-item {
    font-size: 1rem;
    line-height: 1.6;
    background: transparent;
    border: none;
    padding-left: 0;
    padding-right: 0;
    border-left: 4px solid #f4c900;
    margin-bottom: 12px;
    color: #555;
    font-style: italic;
    transition: color 0.3s ease;
}

.list-group-item:hover {
    color: #222;
}

/* 제목 스타일 */
h4 {
    font-weight: 700;
    color: #444;
    margin-bottom: 24px;
    border-bottom: 2px solid #f4c900;
    padding-bottom: 8px;
    letter-spacing: 0.03em;
}

.list-group-item {
    font-size: 15px;
    line-height: 1.6;
}
.book-list {
    margin-bottom: 40px;
    padding-left: 0;
}

.book-item {
    background-color: #fff;
    padding: 12px 20px;
    margin-bottom: 12px;
    border-radius: 8px;
    box-shadow: 0 3px 7px rgba(0,0,0,0.1);
    font-size: 1.1rem;
    font-weight: 600;
    display: flex;
    justify-content: space-between;
    align-items: center;
    transition: background-color 0.2s ease;
    cursor: default;
}

.book-item:hover {
    background-color: #f9f1c9; /* 부드러운 노란 배경으로 하이라이트 */
}

.book-title {
    color: #333;
}

.book-author {
    color: #666;
    font-weight: 400;
    font-style: italic;
}

</style>

<jsp:include page="footer.jsp" />
