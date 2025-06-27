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
		$('button.btn-warning').click(function() {
			const input_confirmCode = $('input:text[name="input_confirmCode"]').val().trim();
			
			if (input_confirmCode == "") {
				alert("인증코드를 입력하세요!!");
				return; // 종료
			}
			
			const frm = document.verifyCertificationFrm;
			frm.userCertificationCode.value = input_confirmCode;
			frm.userid.value = "${requestScope.userid}";
			
			frm.action = "<%= ctxPath%>/login/verifyCertification.go";
			frm.method = "post";
			frm.submit();
		});
	});
	
</script>

<!-- 인증 결과 표시 영역 -->
<div class="container" style="max-width: 400px; margin-top: 30px;">

    <div id="div_findResult" class="text-center p-3">

        <!-- 사용자 정보 없음 -->
        <c:if test="${requestScope.isUserExist == false}">
    		<div class="text-center mb-3">
		        <img src="<%= ctxPath %>/images/error.png" alt="경고 이미지" style="width: 60px; height: 60px;">
		    </div>
            <p class="text-danger font-weight-bold mb-0">
                사용자 정보가 없습니다.
            </p>
        </c:if>

        <!-- 메일 발송 성공 -->
        <c:if test="${requestScope.isUserExist == true && requestScope.sendMailSuccess == true}">
            <!-- 메일 이미지 -->
		    <div class="text-center mb-3">
		        <img src="<%= ctxPath %>/images/mail.png" alt="메일 이미지" style="width: 60px; height: 60px;">
		    </div>
		    
		    <div class="mb-2" style="font-size: 0.90rem;">
		        <div>
		            인증코드가 <strong>${requestScope.email}</strong>로 발송되었습니다.
		        </div>
		        <div class="mt-1">
		            아래에 인증코드를 입력하세요.
		        </div>
		    </div>
            <input type="text" name="input_confirmCode" class="form-control mb-3" placeholder="인증코드 입력">
            <button type="button" class="btn btn-warning btn-block">인증하기</button>
        </c:if>

        <!-- 메일 발송 실패 -->
        <c:if test="${requestScope.isUserExist == true && requestScope.sendMailSuccess == false}">
            <p class="text-danger font-weight-bold mb-0">
                메일 발송이 실패했습니다.
            </p>
        </c:if>

    </div>
</div>

<%-- 인증하기 form --%>
<form name="verifyCertificationFrm">
    <input type="hidden" name="userCertificationCode" />
    <input type="hidden" name="userid" />
</form>