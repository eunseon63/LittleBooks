<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String ctxPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
  <link rel="stylesheet" href="<%= ctxPath %>/bootstrap-4.6.2-dist/css/bootstrap.min.css" />
  <link rel="stylesheet" href="<%= ctxPath %>/css/mypage_custom.css" />
  <title>주문 상세 내역</title>
</head>
<body>

<jsp:include page="../../header1.jsp" />
<br>
<br>
<br>

<br>
<div class="container mt-5 mb-5">
  <div class="row">

    <!-- 좌측 네비게이션 -->
    <div class="col-md-3">
      <div class="card shadow-sm p-3">
        <div class="text-center">
          <div class="rounded-circle bg-warning text-white d-inline-block"
               style="width:70px; height:70px; line-height:70px; font-size:20px; font-weight:bold;">
            <c:out value="${loginuser.name}" />
          </div>
          <div class="mt-2 font-weight-bold">
            <c:out value="${loginuser.name}" />님
          </div>
          <div style="font-size: 0.9rem; color: gray;">
            POINT: <c:out value="${loginuser.point}" />
          </div>
        </div>
        <ul class="nav flex-column mt-4">
          <li class="nav-item mb-2">
            <button type="button" class="btn btn-outline-secondary btn-block"
                    onclick="location.href='myPage.go'">내 정보 수정</button>
          </li>
          <li class="nav-item mb-2">
            <button type="button" class="btn btn-outline-secondary btn-block"
                    onclick="location.href='orderList.go'">나의 주문 내역</button>
          </li>
          <li class="nav-item">
            <button type="button" class="btn btn-outline-danger btn-block"
                    onclick="location.href='deleteMember.go'">회원 탈퇴</button>
          </li>
        </ul>
      </div>
    </div>

    <!-- 우측 주문 상세 내역 -->
    <div class="col-md-9">
      <h4 class="mb-4 font-weight-bold">주문 상세 내역</h4>

      <c:if test="${not empty orderDetailList}">
        <!-- 주문 상품 목록 -->
        <div class="card mb-3">
          <div class="card-header bg-light font-weight-bold">주문 상품</div>
          <div class="card-body">
			<c:forEach var="item" items="${orderDetailList}">
			  <div class="row mb-4 pb-2 border-bottom align-items-center">
			
			    <!-- 책 이미지 -->
			    <div class="col-md-2 text-center">
			      <img src="<%= ctxPath %>/images/${item.book.bimage}"
			           class="img-thumbnail"
			           style="max-height: 90px;"
			           alt="${item.book.bname}" />
			    </div>
			
			    <!-- 책 정보 -->
			    <div class="col-md-4">
			      <div class="font-weight-bold mb-1">
			        <c:out value="${item.book.bname}" />
			      </div>
			      <div class="text-muted small">번호: <c:out value="${item.book.bookseq}" /></div>
			      <div class="text-muted small">저자: <c:out value="${item.book.author}" /></div>
			    </div>
			
			    <!-- 수량 -->
			    <div class="col-md-1 text-center">
			      <div>수량</div>
			      <div class="font-weight-bold">
			        <c:out value="${item.oqty}" />
			      </div>
			    </div>
			
			    <!-- 포인트 -->
			    <div class="col-md-2 text-center">
			      <div>포인트</div>
			      <div class="font-weight-bold">
			        <fmt:formatNumber value="${item.odrprice * item.oqty * 0.1}" type="number" maxFractionDigits="0" groupingUsed="false" />p
			      </div>
			    </div>
			
			    <!-- 주문금액 -->
			    <div class="col-md-2 text-center">
			      <div>주문금액</div>
			      <div class="text-success font-weight-bold">
			        <fmt:formatNumber value="${item.odrprice}" type="currency" currencySymbol="₩" />
			      </div>
			    </div>
			
			    <!-- 배송상태 -->
			    <div class="col-md-1 text-center">
			      <span class="badge
			        <c:choose>
			          <c:when test="${orderDetail.deliverstatus == '1'}">badge-warning text-dark</c:when>
			          <c:otherwise>badge-success</c:otherwise>
			        </c:choose>">
			        <c:choose>
			          <c:when test="${orderDetail.deliverstatus == '1'}">배송중</c:when>
			          <c:otherwise>배송완료</c:otherwise>
			        </c:choose>
			      </span>
			    </div>
			
			  </div>
			</c:forEach>

          </div>
        </div>

        <!-- 배송 정보 -->
        <div class="card mb-3">
          <div class="card-header bg-light font-weight-bold">배송 정보</div>
          <div class="card-body">
            <p><strong>수령인:</strong> <c:out value="${loginuser.name}" default="정보 없음" /></p>
            <p><strong>연락처:</strong> <c:out value="${loginuser.mobile}" default="정보 없음" /></p>
            <p><strong>주소:</strong> <c:out value="${loginuser.address}" default="정보 없음" /></p>
            <p><strong>배송 요청사항:</strong> <c:out value="${deliveryInfo.memo}" default="없음" /></p>
          </div>
        </div>

        <!-- 결제 요약 -->
        <div class="card">
          <div class="card-header bg-light font-weight-bold">결제 요약</div>
          <div class="card-body">
            <div class="row">
              <div class="col-md-6">
                총 주문 상품: <strong><c:out value="${totalQty}" />개</strong>
              </div>
              <div class="col-md-6 text-right">
                총 결제 금액: 
                <strong class="text-success">
                  <fmt:formatNumber value="${totalPrice}" type="currency" currencySymbol="₩" />
                </strong>
              </div>
            </div>
          </div>
        </div>
      </c:if>

      <c:if test="${empty orderDetailList}">
        <div class="text-center py-4 text-muted">주문내역이 존재하지 않습니다.</div>
      </c:if>
    </div>
  </div>
</div>

<jsp:include page="../../footer.jsp" />

<script src="<%= ctxPath %>/js/jquery-3.7.1.min.js"></script>
<script src="<%= ctxPath %>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>
<script src="<%= ctxPath %>/js/member/deleteMember.js"></script>

</body>
</html>
