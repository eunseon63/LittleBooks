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

<br>
<br>
<br>

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
            <button class="btn btn-outline-danger btn-block" onclick="location.href='#'">회원 탈퇴</button>
          </li>
        </ul>
      </div>
    </div>

    <!-- 오른쪽 정보수정 폼 -->
    <div class="col-md-9">
      <div class="card shadow-sm rounded-lg p-4">
        <h4 class="mb-4">내 정보 수정</h4>
        <form action="mypageUpdate.go" method="post">
          <div class="form-group">
            <label>이름</label>
            <input type="text" class="form-control" name="name" value="${loginuser.name}" readonly>
          </div>
          <div class="form-group">
            <label>비밀번호 <span class="text-danger">*</span></label>
            <input type="password" class="form-control" name="pwd" required>
          </div>
          <div class="form-group">
            <label>비밀번호 확인 <span class="text-danger">*</span></label>
            <input type="password" class="form-control" name="pwdConfirm" required>
          </div>
          <div class="form-group">
            <label>이메일 <span class="text-danger">*</span></label>
            <input type="email" class="form-control" name="email" value="${loginuser.email}" required>
          </div>
          <div class="form-group">
            <label>전화번호</label>
            <input type="text" class="form-control" name="mobile" value="${loginuser.mobile}">
          </div>
          <div class="form-group">
            <label>주소</label>
            <input type="text" class="form-control" name="address" value="${loginuser.address}">
          </div>
          <button type="submit" class="btn btn-warning text-white mt-3">수정하기</button>
        </form>
      </div>
    </div>
  </div>
</div>

<jsp:include page="../../footer.jsp" />
	