<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  

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
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>


<link rel="stylesheet" href="<%= ctxPath %>/css/mypage_custom.css" />

<jsp:include page="../../header1.jsp" />

<script type="text/javascript" src="<%= ctxPath%>/js/login/myPage/myPage.js"></script>

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
        <form name="myPageUpdate">
          <div class="form-group">
            <label>이름<span class="text-danger">*</span></label><span class="error" style="color: red; margin-left: 3%">성명은 필수입력 사항입니다.</span>
            <input type="text" class="form-control requiredInfo" name="name" id="name" value="${loginuser.name}">
            <input type="hidden" name="userid" value="${loginuser.userid}" />
          </div>
          <div class="form-group">
            <label>비밀번호 <span class="text-danger">*</span></label><span class="error" style="color: red; margin-left: 3%">암호는 영문자,숫자,특수기호가 혼합된 8~15 글자로 입력하세요.</span>
            <input type="password" class="form-control requiredInfo" name="pwd" id="pwd" required>
            <span id="duplicate_pwd" style="color: red; margin-left: 1%;"></span>
          </div>
          <div class="form-group">
            <label>비밀번호 확인 <span class="text-danger">*</span></label><span class="error" style="color: red; margin-left: 3%">암호가 일치하지 않습니다.</span>
            <input type="password" class="form-control requiredInfo" id="pwdcheck" name="pwdConfirm" required>
          </div>
		  <div class="form-group">
		    <label>이메일 <span class="text-danger">*</span></label><span class="error" style="color: red; margin-left: 3%">이메일 형식에 맞지 않습니다.</span>
		    <div class="d-flex align-items-center">
		      <input type="email" class="form-control w-50" name="email" id="email" value="${loginuser.email}" required />
		      <span id="emailcheck" class="ml-2 btn btn-outline-secondary btn-sm" style="cursor: pointer;">이메일중복확인</span>
		      <span id="emailCheckResult"></span>
		    </div>
	 	  </div>
			<div class="form-group">
			  <label>전화번호</label><span class="error" style="color: red; margin-left: 3%">휴대폰 형식이 아닙니다.</span>
			  <div class="form-row">
			    <div class="col-auto">
			      <input type="text" class="form-control" name="hp1" id="hp1" maxlength="3" value="010" readonly />
			    </div>
			    <div class="col-auto d-flex align-items-center">-</div>
			    <div class="col-auto">
			      <input type="text" class="form-control" name="hp2" id="hp2" maxlength="4" value="${fn:substring(sessionScope.loginuser.mobile, 3, 7)}" />
			    </div>
			    <div class="col-auto d-flex align-items-center">-</div>
			    <div class="col-auto">
			      <input type="text" class="form-control" name="hp3" id="hp3" maxlength="4" value="${fn:substring(sessionScope.loginuser.mobile, 7, 11)}" />
			    </div>
			  </div>
			</div>
			<div class="form-group">
			  <label>우편번호<span class="text-danger">*</span></label><span class="error" style="color: red; margin-left: 3%">주소를 입력하세요.</span>
			  <div class="d-flex align-items-center">
			    <input type="text" class="form-control w-auto" name="postcode" id="postcode" maxlength="5" value="${loginuser.postcode}" style="max-width: 120px;" />
			    <img src="<%= ctxPath%>/images/b_zipcode.gif" id="zipcodeSearch" class="ml-2" style="cursor: pointer;" />
			  </div>
			</div>
          <div class="form-group">
            <label>주소</label><span class="error" style="color: red; margin-left: 3%">우편번호 형식에 맞지 않습니다.</span>
             <input type="text" class="form-control" name="address" id="address" value="${address}"/><br>
             <input type="text" class="form-control" name="detailaddress" id="detailAddress" value="${detailaddress}" />&nbsp;
             <c:if test="${empty extraAddress}">
             	<input type="text" class="form-control" name="extraaddress" id="extraAddress" placeholder="참고항목" />
             </c:if>
             <c:if test="${not empty extraAddress}">
             	<input type="text" class="form-control" name="extraaddress" id="extraAddress" value="${extraAddress}"/> 
             </c:if>
          </div>
          <button type="button" class="btn btn-warning text-white mt-3" onclick="goEdit()">수정하기</button>
        </form>
      </div>
    </div>
  </div>
</div>

<jsp:include page="../../footer.jsp" />
	