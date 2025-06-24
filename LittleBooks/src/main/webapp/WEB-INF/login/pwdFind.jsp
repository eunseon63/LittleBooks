<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

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
	
	const method = "${requestScope.method}";
	
	if(method == "POST") {
		$('input:text[name="userid"]').val("${requestScope.userid}");
		$('input:text[name="email"]').val("${requestScope.email}");
	}
	
	$('button.btn-warning').click(function(){
		goFind();
	});
	
	$('input:text[name="email"]').bind('keyup', function(e){
		if(e.keyCode == 13) {
		   goFind();
		}
	});
}); // end of $(function(){})------------------------------

// Function Declaration
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
		// 이메일이 정규표현식에 위배된 경우
		alert("e메일을 올바르게 입력하세요!!");
		return; // goFind()함수의 종료
	}
	
	frm.action = "<%= ctxPath%>/login/pwdFind.go";
	frm.method = "post";
	frm.submit();
}; // end of function goFind()-------------------
</script>

<!-- 비밀번호 찾기 폼 -->
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
            <button type="button" class="btn btn-warning btn-block">찾기</button>
        </div>
    </form>
</div>
