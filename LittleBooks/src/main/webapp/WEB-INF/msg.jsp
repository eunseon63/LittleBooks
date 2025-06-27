<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    alert("${message}");

    <c:choose>
        <c:when test="${popup_close eq true}">
            if (window.opener && !window.opener.closed) {
                window.opener.location.href = "${loc}"; // 부모창을 지정 URL로 이동시키기
            }
            window.close();
        </c:when>
        <c:otherwise>
            location.href = "${loc}";
        </c:otherwise>
    </c:choose>
</script>
