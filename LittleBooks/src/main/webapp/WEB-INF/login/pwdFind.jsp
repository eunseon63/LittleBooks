<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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

<script type="text/javascript">
$(function() {
	// "비밀번호 찾기" 버튼 클릭 시 goFind() 실행
	$('button#btnPwdFind').click(function(){
		goFind();
	});
	
	// "암호 변경하기" 버튼 클릭 시 goUpdate() 실행
	$('button#btnUpdate').click(function(){
		goUpdate();
	});
	
	// 이메일 입력창에서 Enter 키 입력 시 goFind() 실행
	$('input:text[name="email"]').bind('keyup', function(e){
		if(e.keyCode == 13) {
		   goFind();
		}
	});
}); // end of $(function(){})------------------------------

// Function Declaration
// 입력된 아이디와 이메일을 통해 비밀번호 찾기 팝업을 여는 함수
function goFind() {
	const userid = $('input:text[name="userid"]').val().trim();
	const email = $('input:text[name="email"]').val();
	const regExp_email = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
	const frm = document.pwdFindFrm;
	
	if (userid == "") {
		alert("아이디를 입력하세요!!");
		return;
	}
	
	if( !regExp_email.test(email) ) {
		alert("e메일을 올바르게 입력하세요!!");
		return;
	}
	
    // 새 창 열기
    const popup = window.open("", "pwdFindPopup", "width=500,height=400,left=300,top=150,resizable=no,scrollbars=no");
	
 // 비밀번호 찾기 요청 전송
    frm.action = "<%= ctxPath %>/login/pwdFindSuccess.go";
    frm.method = "post";
    frm.target = "pwdFindPopup"; // 이 창에 폼 제출
    frm.submit();
}; // end of function goFind()-------------------

//새 비밀번호를 입력하고 서버에 전송하여 비밀번호를 변경하는 함수
function goUpdate() {
    const pwd = $('#pwd').val().trim();
    const pwd2 = $('#pwd2').val().trim();

    // 비밀번호 정규표현식: 8~15자, 숫자+영문자+특수문자 포함
    const regExp_pwd = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/;

    if (pwd === "") {
        alert("새 암호를 입력하세요!");
        $('#pwd').focus();
        return;
    }

    if (!regExp_pwd.test(pwd)) {
        alert("암호는 8~15자, 숫자/영문자/특수문자를 모두 포함해야 합니다.");
        $('#pwd').focus();
        return;
    }

    if (pwd2 === "") {
        alert("새 암호 확인을 입력하세요!");
        $('#pwd2').focus();
        return;
    }

    if (pwd !== pwd2) {
        alert("새 암호가 서로 일치하지 않습니다!");
        $('#pwd2').focus();
        return;
    }

    // 유효성 검사 통과 → 비밀번호 변경 요청 전송
    const frm = document.pwdUpdateEndFrm;
    frm.action = "<%= ctxPath %>/login/pwdUpdate.go";
    frm.method = "post";
    frm.submit();
} // function goUpdate() {}-------------------------
</script>

<!-- 비밀번호 찾기 폼 -->
<c:if test="${empty param.userid}">
	<div class="container" style="max-width: 400px; margin-top: 100px;">
	    <h4 class="text-center mb-4">비밀번호 찾기</h4>
	
	    <form name="pwdFindFrm">
	        <div class="form-group">
	            <label for="userid">아이디</label>
	            <input type="text" class="form-control" name="userid" id="userid" autocomplete="off" placeholder="아이디 입력하세요">
	        </div>
	
	        <div class="form-group">
	            <label for="email">이메일</label>
	            <input type="text" class="form-control" name="email" id="email" autocomplete="off" placeholder="이메일 입력하세요">
	        </div>
	
	        <div class="text-center mt-4">
	            <button type="button" class="btn btn-warning btn-block" id="btnPwdFind">찾기</button>
	        </div>
	    </form>
	</div>
</c:if>


<c:if test="${not empty param.userid}">
	<!-- 비밀번호 변경 폼 -->
	<div  class="container" style="max-width: 400px; margin-top: 30px;">
		<h4 class="text-center mb-4 my-4">비밀번호 변경</h4>
	
	   <form name="pwdUpdateEndFrm" class="container" style="max-width: 400px; margin-top: 40px;">
	       <div class="form-group">
	           <label for="pwd">새 암호</label>
	           <input type="password" name="pwd" id="pwd" class="form-control mt-2" placeholder="새 암호 입력">
	       </div>
	
	       <div class="form-group">
	           <label for="pwd2">새 암호 확인</label>
	           <input type="password" id="pwd2" class="form-control mt-2" placeholder="다시 입력">
	       </div>
	
	       <input type="hidden" name="userid" value="${param.userid}" />
	
	       <div class="text-center mt-4">
	           <button type="button" class="btn btn-warning btn-block" id="btnUpdate">암호 변경하기</button>
	       </div>
	   </form>
	</div>
</c:if>