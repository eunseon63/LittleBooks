<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="../header1.jsp" />

<style>
    body {
        font-family: 'Noto Sans KR', sans-serif;
    }
    .sales-container {
        padding: 100px 50px 50px 50px;
    }
    h3 {
        font-weight: bold;
        color: #333;
        text-align: center;
        margin-bottom: 40px;
    }
    .summary-box {
        background-color: #fff8e1;
        border: 1px solid #fbc02d;
        border-radius: 10px;
        padding: 30px;
        margin-bottom: 40px;
        text-align: center;
    }
    .summary-box h4 {
        font-weight: 600;
        color: #f57f17;
    }
    .summary-box .amount {
        font-size: 2rem;
        font-weight: bold;
        color: #d84315;
    }
    .table thead {
        background-color: #fffde7;
    }
    .table th, .table td {
        vertical-align: middle;
    }
    .table td {
        font-size: 0.95rem;
    }
</style>

<div class="sales-container container">

    <h3>전체 매출 확인</h3>

    <!-- 요약 매출 박스 -->
    <div class="summary-box">
        <h4>총 매출 금액</h4>
        <div class="amount">
            <fmt:formatNumber value="${requestScope.totalSales}" type="currency" currencySymbol="₩" />
        </div>
    </div>

    <!-- 매출 목록 테이블 -->
    <div class="table-responsive">
        <table class="table table-bordered text-center">
            <thead>
                <tr>
                    <th>주문번호</th>
                    <th>구매자 아이디</th>
                    <th>도서 제목</th>
                    <th>수량</th>
                    <th>총금액</th>
                    <th>주문일자</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${not empty requestScope.salesList}">
                    <c:forEach var="sale" items="${requestScope.salesList}">
                        <tr>
                            <td>${sale.ordercode}</td>
                            <td>${sale.userid}</td>
                            <td>${sale.bname}</td>
                            <td>${sale.oqty}</td>
                            <td><fmt:formatNumber value="${sale.totalprice}" type="currency" currencySymbol="₩" /></td>
                            <td><fmt:formatDate value="${sale.orderdate}" pattern="yyyy-MM-dd HH:mm" /></td>
                        </tr>
                    </c:forEach>
                </c:if>
                <c:if test="${empty requestScope.salesList}">
                    <tr>
                        <td colspan="6">매출 데이터가 존재하지 않습니다.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

</div>

<jsp:include page="../footer.jsp" />
