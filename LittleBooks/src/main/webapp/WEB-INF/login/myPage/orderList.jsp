<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


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
  <style>
    .card-header {
      background-color: #f8f9fa;
      font-weight: bold;
      color: #333;
    }

    .badge {
      font-size: 0.85rem;
      padding: 6px 12px;
      border-radius: 10px;
    }

    .img-thumbnail {
      border: 1px solid #ddd;
    }

    .section-title {
      text-align: center;
      font-weight: bold;
      font-size: 1.5rem;
      color: #444;
      margin-bottom: 2rem;
    }
  </style>
</head>
<body>

<jsp:include page="../../header1.jsp" />

<br>
<br>
<br>
<br>
<br><br><br>

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
      <div class="section-title" style="text-align:left;">주문 상세 내역</div>


      <c:if test="${not empty orderDetailList}">
        <!-- 주문 상품 목록 -->
        <div class="card shadow-sm border-0 mb-4 ">
          <div class="card-header" style="background-color: #fff9db;">주문 상품</div>
          <div class="card-body">
            <c:forEach var="item" items="${orderDetailList}">
              <div class="row border-bottom pb-3 mb-3 align-items-center">
                <div class="col-md-2 text-center">
                  <a href="<%= ctxPath %>/myshop/bookdetail.go?bookseq=${item.book.bookseq}">
                   <img src="<%= ctxPath %>/images/${item.book.bimage}" class="img-thumbnail" style="max-height:90px;" alt="${item.book.bname}" />
                  </a>
                	</div>
                	<div class="col-md-4">
                  <a href="<%= ctxPath %>/myshop/bookdetail.go?bookseq=${item.book.bookseq}" class="text-dark font-weight-bold" style="text-decoration: none;">
                    ${item.book.bname}
                  </a>
                  <div class="text-muted small">도서번호: ${item.book.bookseq}</div>
                  <div class="text-muted small">저자: ${item.book.author}</div>
                </div>
                <div class="col-md-1 text-center">
                  <div class="text-muted small">수량</div>
                  <div class="font-weight-bold">${item.oqty}</div>
                </div>
                <div class="col-md-2 text-center">
                  <div class="text-muted small">포인트</div>
                  <div class="text-primary font-weight-bold">
                    <fmt:formatNumber value="${item.odrprice * item.oqty * 0.1}" type="number" maxFractionDigits="0" />P
                  </div>
                </div>
                <div class="col-md-2 text-center">
                  <div class="text-muted small">금액</div>
                  <div class="text-success font-weight-bold">
                    <fmt:formatNumber value="${item.odrprice}" type="currency" currencySymbol="₩" />
                  </div>
                </div>
                <div class="col-md-1 text-center">
                  <span class="badge 
                    <c:choose>
                      <c:when test="${item.deliverstatus == '1'}">badge-warning text-dark</c:when>
                      <c:otherwise>badge-success</c:otherwise>
                    </c:choose>">
                    <c:choose>
                      <c:when test="${item.deliverstatus == '1'}">배송중</c:when>
                      <c:otherwise>배송완료</c:otherwise>
                    </c:choose>
                  </span>
                </div>
              </div>
            </c:forEach>
          </div>
        </div>

        <!-- 배송 정보 -->
        <div class="card shadow-sm border-0 mb-4">
          <div class="card-header" style="background-color: #fff9db;">배송 정보</div>
          <div class="card-body">
            <ul class="list-unstyled mb-1 delivery-info">
              <li><strong>수령인:&nbsp;</strong> <c:out value="${loginuser.name}" /></li>
               <li><strong>연락처:&nbsp;</strong> ${fn:substring(loginuser.mobile, 0, 3)}-${fn:substring(loginuser.mobile, 3, 7)}-${fn:substring(loginuser.mobile, 7, 11)}</li>
              <li><strong>주소:&nbsp;</strong> <c:out value="${loginuser.address}" /></li>
              <li><strong>배송 요청사항:&nbsp;</strong> <c:out value="${deliveryInfo.memo}" default="없음" /></li>
            </ul>
          </div>
        </div>

        <!-- 결제 요약 -->
        <div class="card shadow-sm border-0">
          <div class="card-header" style="background-color: #fff9db;">결제 요약</div>
          <div class="card-body">
            <div class="row">
              <div class="col-md-6">
                총 주문 수량: <strong>${totalQty}</strong> 개
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
        <div class="text-center text-muted py-5">
          <h5>주문 내역이 없습니다.</h5>
        </div>
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
