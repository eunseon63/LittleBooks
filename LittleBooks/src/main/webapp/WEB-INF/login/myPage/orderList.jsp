<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctxPath = request.getContextPath();
%>

<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath %>/bootstrap-4.6.2-dist/css/bootstrap.min.css">

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath %>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath %>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>

<link rel="stylesheet" href="<%= ctxPath %>/css/mypage_custom.css" />

<jsp:include page="../../header1.jsp" />

<br><br><br>

<div class="container mt-5 mb-5">
  <div class="row">
    <!-- 왼쪽 네비게이션 -->
    <div class="col-md-3">
      <div class="card shadow-sm rounded-lg p-3">
        <div class="text-center mb-3">
          <div class="rounded-circle bg-warning text-white d-inline-block" style="width:70px; height:70px; line-height:70px; font-size:20px; font-weight:bold;">
            ${loginuser.name}
          </div>
          <div class="mt-2 font-weight-bold">${loginuser.name}님</div>
          <div style="font-size: 0.9rem; color: gray;">POINT: ${loginuser.point}</div>
        </div>
        <ul class="nav flex-column">
          <li class="nav-item mb-2">
            <button class="btn btn-outline-secondary btn-block" onclick="location.href='myPage.go'">내 정보 수정</button>
          </li>
          <li class="nav-item mb-2">
            <button class="btn btn-outline-secondary btn-block" onclick="location.href='orderList.go'">나의 주문 내역</button>
          </li>
          <li class="nav-item">
            <button class="btn btn-outline-danger btn-block" onclick="location.href='deleteMember.go'">회원 탈퇴</button>
          </li>
        </ul>
      </div>
    </div>

    <!-- 오른쪽 주문내역 -->
    <div class="col-md-9">
      <div class="card shadow-sm rounded-lg p-4">
        <h4 class="mb-4">나의 주문 내역</h4>

        <c:choose>
          <c:when test="${empty orderList}">
            <p>주문 내역이 없습니다.</p>
          </c:when>
          <c:otherwise>
            <div class="table-responsive">
              <table class="table table-bordered text-center">
                <thead class="thead-light">
                  <tr>
                    <th>주문번호</th>
                    <th>상품명</th>
                    <th>수량</th>
                    <th>결제금액</th>
                    <th>주문일자</th>
                    <th>주문상태</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="order" items="${orderList}">
                    <tr>
                      <td>${order.orderNo}</td>
                      <td>${order.productName}</td>
                      <td>${order.quantity}</td>
                      <td><fmt:formatNumber value="${order.totalPrice}" type="number"/> 원</td>
                      <td>${order.orderDate}</td>
                      <td>${order.status}</td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>
</div>

<jsp:include page="../../footer.jsp" />
