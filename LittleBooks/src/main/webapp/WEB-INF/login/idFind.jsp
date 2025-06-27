<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    String ctxPath = request.getContextPath();
    String foundId = (String) request.getAttribute("foundId");
    if(foundId == null) foundId = "";
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

$(function () {
    const method = "${requestScope.method}";
    
    if(method == "POST") {
        $('input:text[name="name"]').val("${requestScope.name}");
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
}); // end of $(function(){})

// Function Declaration
function goFind() {
    const name = $('input:text[name="name"]').val().trim();
    const email = $('input:text[name="email"]').val();
    const regExp_email = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    const frm = document.idFindFrm;
    
    if (name == "") {
        alert("성명을 입력하세요!!");
        return;
    }
    
    if( !regExp_email.test(email) ) {
        alert("e메일을 올바르게 입력하세요!!");
        return;
    }
    // 새 창 열기
    const popup = window.open("", "idFindPopup", "width=500,height=400,left=300,top=150,resizable=no,scrollbars=no");
    
    frm.action = "<%= ctxPath%>/login/idFind.go";
    frm.method = "post";
    frm.target = "idFindPopup"; // 이 창에 폼 제출
    frm.submit();
}

</script>

<div class="container" style="max-width: 400px; margin-top: 80px;">
    <h4 class="text-center mb-4">아이디 찾기</h4>
    
    <form name="idFindFrm">
        <div class="form-group">
            <label for="name">성명</label>
            <input type="text" name="name" id="name" class="form-control" placeholder="이름을 입력하세요" autocomplete="off">
        </div>

        <div class="form-group">
            <label for="email">이메일</label>
            <input type="text" name="email" id="email" class="form-control" placeholder="이메일을 입력하세요" autocomplete="off">
        </div>

        <button type="button" class="btn btn-warning btn-block mt-3">찾기</button>
    </form>
</div>
