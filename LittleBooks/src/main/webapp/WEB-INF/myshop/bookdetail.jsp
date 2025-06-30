<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/header1.jsp" />

<style>
body {
    font-family: 'Noto Sans KR', 'Segoe UI', sans-serif;
    background-color: #fefefe;
    color: #333;
}

.detail-wrapper {
    max-width: 1100px;
    margin: 100px auto 60px;
    display: flex;
    gap: 60px;
    background-color: #fff;
    padding: 40px;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.05);
}

.left-box {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
}
.left-box img {
    width: 100%;
    max-width: 360px;
    border: 1px solid #eee;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    background: #fff;
}

.right-box {
    flex: 1.4;
    display: flex;
    flex-direction: column;
    gap: 18px;
}

.right-box h2 {
    font-size: 30px;
    font-weight: 700;
    margin-bottom: 6px;
    color: #222;
}

.book-info {
    font-size: 15px;
    line-height: 1.8;
}
.book-info div {
    margin-bottom: 4px;
}
.book-info strong {
    color: #555;
    margin-right: 6px;
}

.select-box label {
    font-weight: 500;
    margin-right: 8px;
}
.select-box select {
    padding: 10px 16px;
    font-size: 16px;
    border-radius: 6px;
    border: 1px solid #ccc;
    margin-top: 5px;
}

.price-section {
    font-size: 18px;
    font-weight: bold;
    color: #222;
    margin-top: 10px;
}

.button-group {
    display: flex;
    gap: 14px;
    margin-top: 20px;
}
.button-group button {
    padding: 12px 26px;
    font-size: 16px;
    font-weight: 600;
    border: none;
    border-radius: 8px;
    background-color: #f4c900;
    color: #222;
    cursor: pointer;
    box-shadow: 0 3px 8px rgba(0,0,0,0.1);
    transition: all 0.2s ease;
}
.button-group button:hover {
    background-color: #ddb900;
}

.section-box {
    max-width: 1000px;
    margin: 40px auto;
    background-color: #fffef3;
    border-radius: 12px;
    padding: 30px 40px;
    border: 1px solid #f4c900;
}

.section-box h3 {
    font-size: 22px;
    font-weight: bold;
    margin-bottom: 16px;
    border-bottom: 1px solid #eee;
    padding-bottom: 10px;
    color: #111;
}

.section-box p {
    font-size: 16px;
    line-height: 1.8;
}

.review-card {
    background-color: #fff;
    padding: 20px;
    border-radius: 10px;
    border: 1px solid #eee;
    margin-top: 12px;
}
.review-card .star {
    color: #f4c900;
    font-size: 18px;
}
.book-spec {
    color: #d00000;
    font-weight: bold;
    font-size: 14px;
    margin-bottom: 4px;
    letter-spacing: 0.5px;
}

</style>

<div class="detail-wrapper">
    <!-- 이미지 -->
    <div class="left-box">
        <c:choose>
            <c:when test="${not empty book.bimage}">
                <img src="<c:url value='/images/${book.bimage}' />" alt="책 이미지" />
            </c:when>
            <c:otherwise>
                <div style="width: 360px; height: 360px; display: flex; align-items: center; justify-content: center; border: 1px solid #eee; border-radius: 12px; background: #f9f9f9;">
                    책 이미지 없음
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- 상세 정보 -->
    <div class="right-box">
    	<!-- 🔴 스펙 표시 (BEST / NEW 등) -->
    	<c:choose>
	        <c:when test="${book.fk_snum == 2}">
	            <div class="book-spec">BEST(인기)!!</div>
	        </c:when>
	        <c:when test="${book.fk_snum == 3}">
	            <div class="book-spec">NEW(신상)!!</div>
	        </c:when>
    	</c:choose>
        <h2>${book.bname}</h2>
        <div class="book-info">
            <div><strong>출판사:</strong>${book.pvo.pname}</div>
            <div><strong>저자:</strong>${book.author}</div>
            <div><strong>가격:</strong> 
                <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₩" />
            </div>
        </div>

        <div class="select-box">
            <label for="qtySelect">수량 선택</label>
            <select id="qtySelect" onchange="calcTotalPrice()">
                <c:forEach var="i" begin="1" end="10">
                    <option value="${i}">${i}</option>
                </c:forEach>
            </select>
        </div>

        <div class="price-section">
            총 가격: <span id="totalPrice">
                <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₩" />
            </span>
        </div>

        <div class="button-group">
            <button>결제하기</button>
            <button>장바구니</button>
        </div>
    </div>
</div>

<!-- 책 설명 -->
<div class="section-box">
    <h3>책 설명</h3>
    <p>${book.bcontent}</p>
</div>

<!-- 리뷰 -->
<div class="section-box review">
    <h3>리뷰</h3>
    <div class="review-card">
        <p><strong>ID</strong> <span class="star">★★★★★</span><br>아이가 너무 좋아합니다!</p>
    </div>
</div>

<script>
function calcTotalPrice() {
    const qty = parseInt(document.getElementById('qtySelect').value);
    const price = ${book.price};
    const total = qty * price;
    document.getElementById('totalPrice').innerText = '₩' + total.toLocaleString();
}
</script>

<jsp:include page="/WEB-INF/footer.jsp" />
