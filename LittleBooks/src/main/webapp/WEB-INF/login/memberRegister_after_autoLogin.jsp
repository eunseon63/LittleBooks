<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script type="text/javascript">

   window.onload = function(){
	   
	   alert("<%= request.getAttribute("name") %>님 회원가입 성공!! ");
	   
	   const frm = document.loginFrm;
	   frm.submit();
   }

</script>

</head>
<body>

   <form name="loginFrm" action="<%= request.getContextPath()%>/index.go" method="post">
       <input type="hidden" name="userid" value="${requestScope.userid}">
       <input type="hidden" name="pwd" value="${requestScope.pwd}">
   </form>

</body>
</html>


