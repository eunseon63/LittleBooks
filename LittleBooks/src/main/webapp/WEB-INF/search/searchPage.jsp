<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String ctxPath = request.getContextPath();
%>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" />

<jsp:include page="../header1.jsp" />

<div class="container py-5" style="background-color: #fffdf4;">

<div class="card shadow-lg border-warning rounded-lg p-4 mb-5"
     style="max-width: 800px; margin-top: 100px; margin-left: auto; margin-right: auto; border: 2px solid #ffcc00;">

    
    <h3 class="text-center mb-4 font-weight-bold" style="color: #ffbb00;">📚 원하는 책을 검색해보세요</h3>
    
    <form name="book_search_frm" class="form-inline justify-content-center"
          action="<%= ctxPath %>/search/searchPage.go" method="get"
          onsubmit="return validateBookSearch();">

      <div class="form-group mr-3">
        <select class="form-control mr-2" name="searchType"
                style="height: 50px; border-radius: 50px 0 0 50px; border: 2px solid #ffcc00;">
          	<option value="">검색대상</option>
		   <option value="bname">제목</option>
		   <option value="author">저자</option>

        </select>

        <input type="text" class="form-control mr-3" name="searchWord"
       value="${searchWord}"
       placeholder="책 제목 또는 저자를 입력하세요"
       style="height: 50px; border: 2px solid #ffcc00; border-radius: 0;">


        <button type="submit" class="btn"
                style="background-color: #ffcc00; color: black; font-weight: bold;
                       border-radius: 0 50px 50px 0; border: 2px solid #ffcc00; border-left: none; height: 50px;">
          검색
        </button>
      </div>
    </form>
  </div>

  <!-- 검색 결과 도서 목록 영역 -->
  <div class="mt-5">
    <h3 class="mb-5 text-center">📖 도서 목록</h3>

    <c:choose>
      <c:when test="${empty bookList}">
        <p>🔍 검색 결과가 없습니다.</p>
      </c:when>
      <c:otherwise>
        <div class="row">
          <c:forEach var="book" items="${bookList}">
            <div class="col-md-4 mb-4">
              <div class="card h-100">
                <img src="${pageContext.request.contextPath}/images/${book.bimage}"
                     class="card-img-top" alt="${book.bname}"
                     style="height: 200px; object-fit: cover;">
                <div class="card-body">
                  <h5 class="card-title">${book.bname}</h5>
                  <p class="card-text">저자: ${book.author}</p>
                  <p class="card-text">가격: ${book.price}원</p>
                  <a href="${pageContext.request.contextPath}/myshop/bookdetail.go?bookseq=${book.bookseq}"
                     class="btn btn-warning btn-block">상세보기</a>
                </div>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>
  </div>

  <!-- 추천 도서 영역 -->
  <div class="mt-5 p-4 bg-light border rounded shadow-sm">
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

</div>

<script>
function validateBookSearch() {
  const searchType = document.book_search_frm.searchType.value.trim();
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

  return true;
}
</script>
