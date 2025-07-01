<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>
<!DOCTYPE html>
<html>
<head>

<title>:::HOMEPAGE:::</title> 

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

<script>
  $(document).ready(function(){
    $(".dropdown").hover(function(){
      $(this).find(".dropdown-menu").stop(true, true).delay(100).fadeIn(200);
    }, function(){
      $(this).find(".dropdown-menu").stop(true, true).delay(100).fadeOut(200);
    });
  });
</script>

</head>
<body>

<nav class="navbar navbar-light fixed-top py-4" style="background-color: #FFFFFF;">

  <!-- 가운데: 어린이 서점 -->
  <div style="position: absolute; left: 50%; transform: translateX(-50%);">
    <a class="navbar-brand font-weight-bold" href="<%= ctxPath %>/index.go" style="font-size: 24px;"><img src="${pageContext.request.contextPath}/images/logo.png" alt="메인로고" style="height: 100px; margin-top:10%;"/></a>
  </div>

  <!-- 오른쪽 요소들 -->
  <div class="ml-auto d-flex align-items-center">

    <!-- 책 카테고리 드롭다운 -->
    <div class="dropdown mr-3">
      <span class="nav-link font-weight-bold" style="cursor: pointer;" id="bookMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        책
      </span>
      <div class="dropdown-menu" aria-labelledby="bookMenu">
        <a class="dropdown-item" href="<%= ctxPath %>/myshop/booklist.go?category=all">전체</a>
        <a class="dropdown-item" href="<%= ctxPath %>/myshop/booklist.go?category=위인전">위인전</a>
        <a class="dropdown-item" href="<%= ctxPath %>/myshop/booklist.go?category=전래동화">전래동화</a>
        <a class="dropdown-item" href="<%= ctxPath %>/myshop/booklist.go?category=세계동화">세계동화</a>
        <a class="dropdown-item" href="<%= ctxPath %>/myshop/booklist.go?category=만화책">만화책 시리즈</a>
      </div>
    </div>

	<c:if test="${empty sessionScope.loginuser}"> <%-- 로그인 안했을 경우 --%> 
	    <a class="nav-link" href="<%= ctxPath %>/login/login.go">로그인</a>
	    <a class="nav-link" href="<%= ctxPath %>/register/register.go">회원가입</a>
    </c:if>
    <c:if test="${not empty sessionScope.loginuser and sessionScope.loginuser.userid != 'admin'}"> <%-- 로그인 했을 경우 --%> 
	    <a class="nav-link" href="<%= ctxPath %>/login/myPage.go">마이페이지</a>
	    <a class="nav-link" href="<%= ctxPath %>/login/logout.go">로그아웃</a>
	    <a class="nav-link" href="<%= ctxPath %>/register/register.jsp">장바구니</a>
    </c:if>
   	<c:if test="${not empty sessionScope.loginuser and sessionScope.loginuser.userid == 'admin'}"> <%-- 로그인 했을 경우 --%> 
	    <a class="nav-link" href="<%= ctxPath %>/myshop/bookRegister.go">제품등록</a>
	    <a class="nav-link" href="<%= ctxPath %>/member/memberList.go">회원목록</a>
	    <a class="nav-link" href="<%= ctxPath %>/">전체매출확인</a>
	    <a class="nav-link" href="<%= ctxPath %>/login/logout.go">로그아웃</a>
    </c:if>
    <a class="nav-link" href="<%= ctxPath %>/search/searchPage.go" style="color: black;" title="검색">
      <i class="fas fa-search fa-lg"></i>
    </a>
  </div>

</nav>
