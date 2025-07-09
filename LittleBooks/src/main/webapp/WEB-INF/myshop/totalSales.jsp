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
    .order-group {
        margin-bottom: 40px;
    }
    .order-header {
        background-color: #e3f2fd;
        padding: 10px 15px;
        border-radius: 6px;
        margin-bottom: 10px;
    }
</style>

<div class="sales-container container">

    <h3>전체 매출 확인</h3>

    <!-- 총 매출 박스 -->
    <div class="summary-box">
        <h4>총 매출 금액</h4>
        <div class="amount">
            <fmt:formatNumber value="${totalSales}" type="currency" currencySymbol="₩" />
        </div>
    </div>

    <!-- 주문 단위로 묶은 매출 목록 -->
    <c:if test="${not empty salesList}">
        <c:set var="prevOrderCode" value="" />
        <c:forEach var="sale" items="${salesList}">
            <c:if test="${sale.ordercode ne prevOrderCode}">
                <c:if test="${not empty prevOrderCode}">
                    </tbody>
                    </table>
                    </div> <!-- order-group 끝 -->
                </c:if>

                <!-- 새로운 주문 시작 -->
                <div class="order-group">
                    <div class="order-header">
                        <strong>주문번호:</strong> ${sale.ordercode} &nbsp; | 
                        <strong>구매자:</strong> ${sale.userid} &nbsp; | 
                        <strong>주문일자:</strong> 
                        <fmt:formatDate value="${sale.orderdate}" pattern="yyyy-MM-dd HH:mm" /> &nbsp; | 
                        <strong>총금액:</strong> 
                        <fmt:formatNumber value="${sale.totalprice}" type="currency" currencySymbol="₩" />
                    </div>

                    <div class="table-responsive">
                        <table class="table table-bordered text-center">
                            <thead>
                                <tr>
                                    <th>도서 제목</th>
                                    <th>수량</th>
                                </tr>
                            </thead>
                            <tbody>
            </c:if>

            <!-- 도서 목록 -->
            <tr>
                <td>${sale.bname}</td>
                <td>${sale.oqty}</td>
            </tr>

            <!-- 현재 주문코드 기억 -->
            <c:set var="prevOrderCode" value="${sale.ordercode}" />
        </c:forEach>

        <!-- 마지막 주문 닫기 -->
        </tbody>
        </table>
        </div> <!-- order-group 끝 -->
    </c:if>

    <c:if test="${empty salesList}">
        <div class="text-center">매출 데이터가 존재하지 않습니다.</div>
    </c:if>

</div>

<jsp:include page="../footer.jsp" />
