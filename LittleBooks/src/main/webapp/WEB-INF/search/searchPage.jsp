<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String ctxPath = request.getContextPath();
%>

<jsp:include page="../header1.jsp" />

<c:set var="ctxPath" value="${pageContext.request.contextPath}" />

<style>
/* 기존 스타일 유지 + 검색창 스타일 추가 */
.page-title {
    text-align: center;
    font-size: 28px;
    font-weight: bold;
    margin-top: 60px;  /* 위 여백 살짝 줄임 */
    margin-bottom: 20px;
    color: #333;
}
.search-box {
    max-width: 600px;
    margin: 0 auto 40px auto;
    display: flex;
    justify-content: center;
    gap: 0;
}
.search-select {
    height: 45px;
    border: 2px solid #f4c900;
    border-right: none;
    border-radius: 30px 0 0 30px;
    padding: 0 12px;
    font-size: 14px;
    background-color: #fffef5;
    cursor: pointer;
    outline: none;
}
.search-input {
    height: 45px;
    border: 2px solid #f4c900;
    border-left: none;
    border-right: none;
    padding: 0 15px;
    font-size: 14px;
    flex-grow: 1;
    outline: none;
    background-color: #fffef5;
}
.search-button {
    height: 45px;
    border: 2px solid #f4c900;
    border-left: none;
    border-radius: 0 30px 30px 0;
    background-color: #f4c900;
    color: #333;
    font-weight: bold;
    padding: 0 25px;
    cursor: pointer;
    transition: background-color 0.2s ease;
}
.search-button:hover {
    background-color: #ffcc00;
}
.container {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(230px, 1fr));
    gap: 25px;
    padding: 40px;
    justify-content: center;
    background-color: #fff;
    margin: 0 auto;
    max-width: 1000px;
}
.card {
    width: 240px;
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
.card-author {
    font-size: 13px;
    color: #666;
    margin-bottom: 5px;
}
</style>

<!-- 도서목록 제목 -->
<div class="page-title text-center" style="margin-top: 5%;">도서목록</div>

<!-- 검색창 -->
<form method="get" action="<c:url value='/search/searchPage.go' />" class="search-box">
    <select name="searchType" class="search-select" required>
        <option value="">검색대상</option>
         <option value="bname">제목</option>
         <option value="author">저자</option>

    </select>
    <input type="text" name="searchWord" class="search-input"
           value="${param.searchWord}"
           placeholder="검색어를 입력하세요" required />
    <button type="submit" class="search-button">검색</button>
</form>

<!-- 도서 카드 목록 -->
<c:choose>
    <c:when test="${empty bookList}">
        <div class="text-center mt-5 mb-5" style="font-size: 18px; color: #777;">
            🔍 검색 결과가 없습니다.
        </div>
    </c:when>
    <c:otherwise>
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

                            <div class="card-author">
                                저자: ${book.author}
                            </div>

                            <div class="card-price">
                                <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₩" />
                            </div>
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

  <!-- 추천 도서 영역 -->
<div class="mt-5 p-4 bg-light border rounded shadow-sm" 
     style="max-width: 900px; margin: 50px auto;">  <%-- max-width 조정 + 중앙 정렬 --%>
  <a href="${ctxPath}/myshop/bookdetail.go?bookseq=166" style="text-decoration: none; color: inherit;">
    <h4>📚 이번 주의 추천 도서</h4>
    <div class="d-flex mt-4">
      <img src="${ctxPath}/images/동화8.jpg" alt="추천도서"
           style="width: 120px; height: auto; margin-right: 20px; border-radius: 10px;">
      <div>
        <h5>사과가 쿵!</h5>
        <p>어느 날 커다란 사과가 떨어졌어요. 두더지부터 코끼리까지 사과를 갉아먹어요. 그런데 갑자기 비가 내려요…</p>
        <small>저자: 다다 히로시 | 출판사: 꿈나무출판</small>
      </div>
    </div>
  </a>
</div>

<jsp:include page="/WEB-INF/footer.jsp" />

<script>
//도서 검색 유효성 검사 함수
function validateBookSearch() {
  // 검색 대상 (제목/저자) 선택 값 가져오기
  const searchType = document.book_search_frm.searchType.value.trim();
//검색어 입력 값 가져오기
  const searchWord = document.book_search_frm.searchWord.value.trim();

  if (searchType === "") {
    alert("검색 대상을 선택하세요.");
    document.book_search_frm.searchType.focus();
    return false;
  }

  if (searchWord === "") {
    alert("검색어를 입력하세요.");
    document.book_search_frm.searchWord.focus();
    return false;
  }
  //둘 다 입력되었으면 true 반환 → 폼 정상 제출
  return true;
}
</script>
