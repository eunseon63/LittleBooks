<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String ctxPath = request.getContextPath();
    //     /MyMVC
%>

<jsp:include page="../header1.jsp" />

<script type="text/javascript" src="<%= ctxPath%>/js/login/login.js"></script>

<script type="text/javascript">
function openIdFind() {
    const popup = window.open(
        "<%= ctxPath %>/login/idFind.go",  // URL
        "idFindWindow",                    // 창 이름
        "width=600,height=500,left=100,top=100,resizable=no,scrollbars=no" // 창 옵션
    );
    popup.focus();
}

function openPwdFind() {
    const popup = window.open(
        "<%= ctxPath %>/login/pwdFind.go",  // URL
        "pwdFindWindow",                    // 창 이름
        "width=600,height=500,left=100,top=100,resizable=no,scrollbars=no" // 창 옵션
    );
    popup.focus();
}
</script>

<!-- 로그인 폼 -->
<div class="container" style="max-width: 400px; margin-top: 10%;">
    <h3 class="text-center mb-4">로그인</h3>

    <form name="loginFrm" action="<%= ctxPath %>/login/loginSuccess.go" method="post">
        <div class="form-group">
            <label for="loginUserid">아이디</label>
            <input type="text" class="form-control" name="userid" id="loginUserid" placeholder="아이디을 입력하세요" autocomplete="off">
        </div>

        <div class="form-group">
            <label for="loginPwd">비밀번호</label>
            <input type="password" class="form-control" name="pwd" id="loginPwd" placeholder="비밀번호을 입력하세요" autocomplete="off">
        </div>

        <button type="submit" id="btnSubmit" class="btn btn-warning btn-block my-5">로그인</button>

        <div class="text-center mt-3">
            <a href="#" onclick="openIdFind(); return false;">아이디찾기</a> /
            <a href="#" onclick="openPwdFind(); return false;">비밀번호찾기</a>
        </div>
    </form>
</div>

