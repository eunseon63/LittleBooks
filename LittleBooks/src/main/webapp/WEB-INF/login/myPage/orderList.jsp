<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

    <!-- 오른쪽 주문 상세 내역 -->
    <div class="col-md-9">
      <h3 class="mb-4">주문 상세 내역</h3>

      <!-- 주문 상품 목록 -->
<div class="card mb-4">
  <div class="card-header bg-light font-weight-bold d-flex align-items-center">
    <span class="mr-4">&nbsp;&nbsp;&nbsp;선택</span>
    <span class="mr-4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;주문 상품</span>
    
    <div class="d-flex gap-3">
    	<span style="margin-right: 60px;">&nbsp;&nbsp;코드</span>
    	<span style="margin-right: 60px;">주문일자</span>
       <span style="margin-right: 50px;">상품명</span>
      <span style="margin-right: 60px;">수량</span>
     <span style="margin-right: 60px;">가격</span>
    
    </div>
  </div>

  <div class="card-body">
    <!-- 주문 상품 데이터 영역 -->


 
 	  
        
         
  <!-- 첫 번째 책 -->
<div class="row mb-3 border-bottom pb-2 align-items-center">
  <div class="col-md-1 text-center">
    <input type="checkbox" name="productCheckbox" value="만화3" />
  </div>
  <div class="col-md-2 text-center">
    <img src="<%= ctxPath %>/images/만화3.jpg" class="img-fluid" style="max-height: 80px;">
  </div>
  <div class="col-md-3 align-self-center">
    <strong>만화 3</strong>
  </div>
  <div class="col-md-2 align-self-center">
    수량: 2
  </div>
  <div class="col-md-2 align-self-center text-right">
    25,000원
  </div>
</div>

<!-- 두 번째 책 -->
<div class="row mb-3 border-bottom pb-2 align-items-center">
  <div class="col-md-1 text-center">
    <input type="checkbox" name="productCheckbox" value="만화4" />
  </div>
  <div class="col-md-2 text-center">
    <img src="<%= ctxPath %>/images/만화4.jpg" class="img-fluid" style="max-height: 80px;">
  </div>
  <div class="col-md-3 align-self-center">
    <strong>만화 4</strong>
  </div>
  <div class="col-md-2 align-self-center">
    수량: 1
  </div>
  <div class="col-md-2 align-self-center text-right">
    15,000원
  </div>
</div>

<div class="mb-3">
  <input type="checkbox" id="checkAll" />
  <label for="checkAll">전체 선택</label>
</div>

      
      

      <!-- 배송정보 -->
      <div class="card mb-4">
        <div class="card-header bg-light font-weight-bold">배송 정보</div>
        <div class="card-body">
          <p><strong>수령인:</strong> ${deliveryInfo.recipient}</p>
          <p><strong>연락처:</strong> ${deliveryInfo.phone}</p>
          <p><strong>주소:</strong> ${deliveryInfo.address}</p>
          <p><strong>배송 요청사항:</strong> ${deliveryInfo.memo}</p>
        </div>
      </div>

      <!-- 주문 요약 -->
      <div class="card">
        <div class="card-header bg-light font-weight-bold">결제 요약</div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-6">
              총 주문 상품: <strong>${totalQty}개</strong>
            </div>
            <div class="col-md-6 text-right">
              총 결제 금액: 
              <strong class="text-primary">
                <fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="₩" />
              </strong>
              
              
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- col-md-9 끝 -->
  </div>
  <!-- row 끝 -->
</div>


<jsp:include page="../../footer.jsp" />
