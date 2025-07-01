<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    String ctxPath = request.getContextPath();
%>

<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" > 

<!-- Font Awesome 6 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">

<!-- 직접 만든 CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/template/template.css" />
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script> 

<%-- jQueryUI CSS 및 JS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
<script type="text/javascript" src="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script>


<script type="text/javascript" src="<%= ctxPath%>/js/login/dormantPage.js"></script>


<nav class="navbar navbar-light fixed-top py-4" style="background-color: #FFFFFF;">

  <!-- 가운데: 어린이 서점 -->
  <div style="position: absolute; left: 50%; transform: translateX(-50%); margin-top: 2%">
    <a class="navbar-brand font-weight-bold" href="<%= ctxPath %>/index.go" style="font-size: 24px;"><img src="${pageContext.request.contextPath}/images/logo.png" alt="메인로고" style="height: 100px; margin-top:10%;"/></a>
  </div>
</nav>

<!-- 비밀번호 변경 폼 -->
<div  class="container" style="max-width: 400px; margin-top: 10%;">
	<h4 class="text-center mb-4 my-4">비밀번호 변경</h4>
	
	<div class="my-5">
		<span style="color: red;">${loginuser.name}</span> 회원님은 <strong>3개월 이상 동일한 비밀번호</strong>를 사용하고 계십니다 <br>
		소중한 개인 정보보호를 위하여 비밀번호를 주기적으로 변경해 주세요.
	</div>

   <form name="pwdUpdateEndFrm" class="container" style="max-width: 400px; margin-top: 40px;">
   
       <div class="form-group">
           <label for="new_pwd">새 암호</label><span class="error mx-2" style="color: red;">암호는 영문자,숫자,특수기호가 혼합된 8~15 글자로 입력하세요</span>
           <span id="duplicate_pwd" style="color: red; margin-left: 2%;"></span>
           <input type="password" id="new_pwd" name="new_pwd" class="form-control mt-2 requiredInfo" placeholder="새 암호 입력">
       </div>

       <div class="form-group">
           <label for="pwd2">새 암호 확인</label><span class="error mx-2" style="color: red;">암호가 일치하지 않습니다</span>
           <input type="password" id="pwd2" class="form-control mt-2 requiredInfo" placeholder="다시 입력">
       </div>

       <input type="hidden" name="userid" value="${loginuser.userid}" />

       <div class="text-center mt-4">
           <button type="button" class="btn btn-warning btn-block" id="btnUpdate" onclick="goEdit()">암호 변경하기</button>
       </div>
   </form>
</div>