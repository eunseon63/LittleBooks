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

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script> 

<%-- jQueryUI CSS 및 JS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
<script type="text/javascript" src="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script>

</head>
<body>

<nav class="navbar navbar-light fixed-top py-3" style="position: relative; background-color: #FFEB3B;">

  <!-- 가운데 BOOK STORE -->
  <div style="position: absolute; left: 50%; transform: translateX(-50%);">
    <a class="navbar-brand font-weight-bold" href="<%= ctxPath %>/index.jsp" style="font-size: 24px;">어린이 서점</a>
  </div>

  <!-- 오른쪽 로그인/회원가입 링크 -->
  <div class="ml-auto d-flex">
    <a class="nav-link" href="<%= ctxPath %>/login/login.jsp">로그인</a>
    <a class="nav-link" href="<%= ctxPath %>/register/register.jsp">회원가입</a>
    <!-- 검색(돋보기 아이콘) -->
    <a class="nav-link" href="<%= ctxPath %>/search/search.jsp" style="color: black;" title="검색">
      <i class="fas fa-search fa-lg"></i> <!-- Font Awesome 아이콘 -->
    </a>
  </div>

</nav>