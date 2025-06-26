<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String ctxPath = request.getContextPath();
%>

<jsp:include page="../header1.jsp" />
<script type="text/javascript" src="<%= ctxPath%>/js/member/mypage.js"></script>
<script type="text/javascript">


</script>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
    <style>
        body {
            background-color: #e5e5e5;
            font-family: Arial, sans-serif;
        }

        .container {
            width: 90%;
            max-width: 1200px;
            margin: 50px auto;
            background-color: #fff;
            padding: 40px;
            display: flex;
            border: 5px solid #f3db24;
        }

        .left-panel {
            flex: 1;
            margin-right: 40px;
            border: 2px solid gold;
            padding: 20px;
        }

        .left-panel h3 {
            font-size: 18px;
            margin-bottom: 10px;
        }

        .left-panel p {
            font-size: 16px;
            margin-bottom: 10px;
        }

        .left-panel button {
            display: block;
            width: 100%;
            margin: 10px 0;
            padding: 10px;
            border: 1px solid #f3db24;
            background-color: #f3db24;
            font-size: 15px;
            cursor: pointer;
            
        }

        .right-panel {
            flex: 2;
        }

        .right-panel form {
            display: flex;
            flex-direction: column;
        }

        .form-row {
            margin-bottom: 15px;
            display: flex;
            align-items: center;
        }

        .form-row label {
            width: 150px;
            font-weight: bold;
        }

        .form-row input {
            flex: 1;
            padding: 8px;
            border: 2px solid #f3db24;
        }

        .required {
            color: red;
        }

        .submit-btn {
            margin-top: 20px;
            padding: 10px 30px;
            background-color: #f3db24;
            font-weight: bold;
            border: none;
            cursor: pointer;
            align-self: flex-end;
            
        }

        .header {
            background-color: #f3db24;
            padding: 20px;
            text-align: right;
            font-weight: bold;
        }

        .header span {
            margin-left: 15px;
        }
    </style>
</head>
<body>

<div class="header">
    <span><a href="#">마이페이지</a></span>
    <span><a href="#">장바구니</a></span>
    <span><a href="#">로그아웃</a></span>
</div>

<div class="container">
    <div class="left-panel">
        <h3>이름 [${loginuser.name}]님</h3>
        <p>POINT : ${loginuser.point}</p>

        <button onclick="location.href='#'">내 정보 수정</button>
        <button onclick="location.href='#'">나의 주문 내역</button>
        <button onclick="location.href='#'">회원 탈퇴</button>
    </div>

    <div class="right-panel">
        <form action="mypageUpdate.go" method="post">
            <div class="form-row">
                <label>이름 <span class="required">*</span></label>
                <input type="text" name="name" value="${loginuser.name}" required>
            </div>
            <div class="form-row">
                <label>비밀번호 <span class="required">*</span></label>
                <input type="password" name="pwd" required>
            </div>
            <div class="form-row">
                <label>비밀번호 확인 <span class="required">*</span></label>
                <input type="password" name="pwdConfirm" required>
            </div>
            <div class="form-row">
                <label>이메일 <span class="required">*</span></label>
                <input type="email" name="email" value="${loginuser.email}" required>
            </div>
            <div class="form-row">
                <label>전화번호</label>
                <input type="text" name="mobile" value="${loginuser.mobile}">
            </div>
            <div class="form-row">
                <label>주소</label>
                <input type="text" name="address" value="${loginuser.address}">
            </div>

            <button type="submit" class="submit-btn">수정하기</button>
        </form>
    </div>
</div>

</body>
</html>

<%-- <a href="${pageContext.request.contextPath}/member/edit.go">회원정보 수정</a> --%>