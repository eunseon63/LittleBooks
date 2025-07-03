<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String ctxPath = request.getContextPath();
%>

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="<%= ctxPath %>/bootstrap-4.6.2-dist/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">

<!-- jQuery & Bootstrap JS -->
<script src="<%= ctxPath %>/js/jquery-3.7.1.min.js"></script>
<script src="<%= ctxPath %>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>

<!-- jQuery UI -->
<link rel="stylesheet" href="<%= ctxPath %>/jquery-ui-1.13.1.custom/jquery-ui.min.css">
<script src="<%= ctxPath %>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script>

<script type="text/javascript">
$(function () {
  $("#btnReactivate").click(function() {
	    if(confirm("휴면 해제를 진행하시겠습니까?")) {
	      $.ajax({
	        url: '<%= ctxPath %>/login/reactivateAccountEnd.go',
	        type: 'POST',
	        dataType: 'json',
	        success: function(json) {
	          if(json.n == 1) {
	            alert("휴면 해제가 완료되었습니다.");
	            // 메인 화면으로 이동 (로그인 상태 유지 가정)
	            window.location.href = '<%= ctxPath %>/index.go';
	          } else {
	            alert("휴면 해제에 실패했습니다. 다시 시도해주세요.");
	          }
	        },
			error: function(request, status, error){
	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
	      });
	    }
  });
});
</script>

<!-- 로고 영역 -->
<nav class="navbar navbar-light fixed-top py-4" style="background-color: #FFFFFF;">

  <!-- 가운데: 어린이 서점 -->
  <div style="position: absolute; left: 50%; transform: translateX(-50%); margin-top: 2%">
    <a class="navbar-brand font-weight-bold" href="<%= ctxPath %>/index.go" style="font-size: 24px;"><img src="${pageContext.request.contextPath}/images/logo.png" alt="메인로고" style="height: 100px; margin-top:10%;"/></a>
  </div>
</nav>

<!-- 본문 -->
<div class="container" style="max-width: 40%; margin-top: 10%">
  <h4 class="font-weight-bold mb-4 text-center">휴면 계정 안내</h4>

  <p>안녕하세요!</p>
  <p>
    <span style="color: red;">${requestScope.name}</span> 회원님은 <strong>LITTLE BOOKS 계정에 1년 이상 로그인하지 않아</strong><br>
    관련 법령에 따라 휴면 상태로 전환되었습니다.
  </p>

<%
    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String today = java.time.LocalDate.now().format(formatter);
%>

  <div class="border rounded px-3 py-2 my-4 bg-light">
    <div><strong>마지막 접속일:</strong> ${loginDate}</div>
    <div><strong>휴면 전환일:</strong> <%= today %></div>
  </div>

  <p>LITTLE BOOKS 서비스를 계속 이용하시려면 아래 <strong>[휴면 해제하기]</strong> 버튼을 클릭해주세요.</p>

  <div class="text-center mt-4">
    <button type="button" class="btn btn-warning font-weight-bold px-4" id="btnReactivate">휴면 해제하기</button>
  </div>
</div>