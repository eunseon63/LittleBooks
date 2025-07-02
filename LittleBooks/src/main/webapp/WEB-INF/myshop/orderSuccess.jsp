<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/header1.jsp" %>

<div style="max-width:600px; margin:100px auto; text-align:center;">
    <h2>결제가 완료되었습니다 🎉</h2>
    <p>주문이 정상적으로 처리되었습니다.</p>
    <a href="<c:url value='/myshop/booklist.go' />" 
       style="display:inline-block; margin-top:20px; padding:10px 20px; background:#f4c900; color:#222; border-radius:6px;">
       쇼핑 계속하기
    </a>
</div>

<%@ include file="/WEB-INF/footer.jsp" %>