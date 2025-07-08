<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="../header1.jsp" />

<div class="container" style="padding: 100px 50px;">
    <h3 class="text-center mb-4">회원별 누적 구매 금액</h3>

    <table class="table table-bordered text-center">
        <thead class="table-light">
            <tr>
                <th>순위</th>
                <th>회원 ID</th>
                <th>누적 구매 금액</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${userSpentList}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${user.userid}</td>
                    <td><fmt:formatNumber value="${user.totalSpent}" type="currency" currencySymbol="₩" /></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="../footer.jsp" />
