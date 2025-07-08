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
  <title>주문 상세 내역</title>
  <link rel="stylesheet" href="<%= ctxPath %>/bootstrap-4.6.2-dist/css/bootstrap.min.css" />
</head>
<body>

<jsp:include page="../header1.jsp" />

<div class="container mt-5">
  <h3>주문 상세 내역 - 주문코드: <c:out value="${selectedOrdercode}" /></h3>

  <!-- 주문 상품 -->
  <div class="card mb-3">
    <div class="card-header bg-light font-weight-bold">🛒 주문 상품</div>
    <div class="card-body">
      <c:forEach var="item" items="${detailList}">
        <div class="row mb-3 align-items-center border-bottom pb-2">
          <div class="col-md-2">
            <img src="${ctxPath}/images/${item.bimage}" class="img-fluid" alt="${item.bname}" />
          </div>
          <div class="col-md-4">
            <strong><c:out value="${item.bname}" /></strong>
          </div>
          <div class="col-md-2">
            수량: <c:out value="${item.oqty}" />
          </div>
          <div class="col-md-2 text-right">
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
      <p><strong>수령인:</strong> <c:out value="${deliveryInfo.recipient}" /></p>
      <p><strong>연락처:</strong> <c:out value="${deliveryInfo.phone}" /></p>
      <p><strong>주소:</strong> <c:out value="${deliveryInfo.address}" /></p>
      <p><strong>배송 요청사항:</strong> <c:out value="${deliveryInfo.memo}" /></p>
    </div>
  </div>

  <!-- 결제 요약 -->
  <div class="card">
    <div class="card-header bg-light font-weight-bold">💳 결제 요약</div>
    <div class="card-body d-flex justify-content-between">
      <div>총 주문 상품: <strong><c:out value="${totalQty}" />개</strong></div>
      <div>총 결제 금액: <strong class="text-success"><fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="₩" /></strong></div>
    </div>
  </div>

  <a href="<%= ctxPath %>/orderList.go" class="btn btn-secondary mt-3">목록으로 돌아가기</a>
</div>

<jsp:include page="../footer.jsp" />
<script src="<%= ctxPath %>/js/jquery-3.7.1.min.js"></script>
<script src="<%= ctxPath %>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
