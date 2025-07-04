<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String ctxPath = request.getContextPath();
%>

<!-- CSS 및 JS 로딩 -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath %>/bootstrap-4.6.2-dist/css/bootstrap.min.css">
<script type="text/javascript" src="<%= ctxPath %>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath %>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="<%= ctxPath %>/css/mypage_custom.css" />

<jsp:include page="/WEB-INF/header1.jsp" />

<br><br><br>

<div class="container mt-5 mb-5">
  <div class="row">
    <!-- 좌측 메뉴 -->
    <div class="col-md-3">
      <div class="card shadow-sm p-3">
        <div class="text-center">
          <div class="rounded-circle bg-warning text-white d-inline-block"
               style="width:70px; height:70px; line-height:70px; font-size:20px; font-weight:bold;">
            ${loginuser.name}
          </div>
          <div class="mt-2 font-weight-bold">${loginuser.name}님</div>
          <div style="font-size: 0.9rem; color: gray;">POINT: ${loginuser.point}</div>
        </div>
        <ul class="nav flex-column mt-4">
          <li class="nav-item mb-2"><button class="btn btn-outline-secondary btn-block" onclick="location.href='myPage.go'">내 정보 수정</button></li>
          <li class="nav-item mb-2"><button class="btn btn-outline-secondary btn-block" onclick="location.href='orderList.go'">나의 주문 내역</button></li>
          <li class="nav-item"><button class="btn btn-outline-danger btn-block" onclick="location.href='deleteMember.go'">회원 탈퇴</button></li>
        </ul>
      </div>
    </div>

    <!-- 우측 주문 상세 영역 -->
    <div class="col-md-9">
      <h4 class="mb-4 font-weight-bold">🧾 주문 상세 내역</h4>

      <!-- 주문 상품 목록 -->
      <div class="card mb-3">
        <div class="card-header bg-light font-weight-bold">🛒 주문 상품</div>
        <div class="card-body">
          <c:forEach var="item" items="${detailList}">
            <div class="row mb-3 border-bottom pb-2 align-items-center">
              <div class="col-md-2 text-center">
                <img src="${pageContext.request.contextPath}/images/${item.bimage}" class="img-fluid" style="max-height: 80px;">
              </div>
              <div class="col-md-4">
                <strong>${item.bname}</strong>
              </div>
              <div class="col-md-2">수량: ${item.oqty}</div>
              <div class="col-md-4 text-right">
                <fmt:formatNumber value="${item.odrprice}" type="currency" currencySymbol="₩" />
              </div>
            </div>
          </c:forEach>
        </div>
      </div>

      <!-- 배송 정보 -->
      <div class="card mb-3">
        <div class="card-header bg-light font-weight-bold">🚚 배송 정보</div>
        <div class="card-body">
          <p><strong>수령인:</strong> ${deliveryInfo.recipient}</p>
          <p><strong>연락처:</strong> ${deliveryInfo.phone}</p>
          <p><strong>주소:</strong> ${deliveryInfo.address}</p>
          <p><strong>배송 요청사항:</strong> ${deliveryInfo.memo}</p>
        </div>
      </div>

      <!-- 결제 정보 -->
      <div class="card">
        <div class="card-header bg-light font-weight-bold">💳 결제 요약</div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-6">총 주문 상품: <strong>${totalQty}개</strong></div>
            <div class="col-md-6 text-right">
              총 결제 금액:
              <strong class="text-success">
                <fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="₩" />
              </strong>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/footer.jsp" />
