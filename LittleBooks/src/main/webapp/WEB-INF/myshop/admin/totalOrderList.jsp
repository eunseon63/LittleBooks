<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
    String ctxPath = request.getContextPath();
%>

<jsp:include page="../../header1.jsp" />

<style>
    body {
        font-family: 'Helvetica Neue', sans-serif;
    }

    .container {
        padding: 60px 50px;
    }

    h3 {
        color: #333;
        font-weight: bold;
        margin-bottom: 30px;
        text-align: center;
    }

    .table thead {
        background-color: #fff9db;
    }

    .table th,
    .table td {
        vertical-align: middle;
        font-size: 0.95rem;
    }

    #orderTbl tr.orderInfo:hover {
        background-color: #fffde7;
        cursor: pointer;
        transition: background-color 0.2s ease-in-out;
    }

    .product-info {
        text-align: left;
    }

    .product-info img {
        width: 70px;
        height: auto;
        border-radius: 4px;
        border: 1px solid #ddd;
    }

    .product-details {
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

    .text-muted.small {
        font-size: 0.85rem;
    }

    .badge {
        font-size: 0.9rem;
    }
</style>

<script type="text/javascript">
	$(function () {
        // 행 클릭 시 bookseq 기반 팝업 열기
		$('table#orderTbl tr.orderInfo').click(function () {
		    const ordercode = $(this).data("ordercode"); // data-bookseq 값 읽기

		    const popupUrl = "<%= ctxPath %>/myshop/userOrderDetail.go?ordercode=" + encodeURIComponent(ordercode);
		    const popupOptions = "width=800,height=600,scrollbars=yes,resizable=no";

		    window.open(popupUrl, "memberDetailPopup", popupOptions);
		});
	});
</script>


<div class="container mt-5">
    <h3>주문내역 전체 목록</h3>

    <div class="table-responsive shadow-sm rounded">
        <table class="table table-bordered text-center" id="orderTbl">
            <thead>
                <tr>
                    <th>주문코드</th>
                    <th>주문일자</th>
                    <th>제품정보</th>
                    <th>수량</th>
                    <th>총액</th>
                    <th>포인트</th>
                    <th>배송상태</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${not empty requestScope.orderDetailList}">
                    <c:forEach var="orderDetail" items="${requestScope.orderDetailList}">
                        <tr class="orderInfo" data-ordercode="${orderDetail.fk_ordercode}">
                            <td>${orderDetail.fk_ordercode}</td>
                            <td>${orderDetail.deliverdate}</td>
                            <td class="product-info">
                                <div class="d-flex align-items-start gap-3">
                                    <img src="<%= ctxPath %>/images/${orderDetail.book.bimage}"
                                         alt="${orderDetail.book.bname}" class="mr-3" />
                                    <div class="product-details">
                                        <div class="text-muted small">번호: ${orderDetail.book.bookseq}</div>
                                        <div class="text-muted small">제목: ${orderDetail.book.bname}</div>
                                        <div class="text-muted small">저자: ${orderDetail.book.author}</div>
                                        <div class="text-muted small">
                                            가격: <fmt:formatNumber value="${orderDetail.book.price}" type="currency" currencySymbol="₩" />
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <td>${orderDetail.oqty}</td>
                            <td>
                                <fmt:formatNumber value="${orderDetail.odrprice * orderDetail.oqty}" type="currency" currencySymbol="₩" />
                            </td>
                            <td>
                                <fmt:formatNumber value="${orderDetail.odrprice * orderDetail.oqty * 0.1}" type="number" maxFractionDigits="0" groupingUsed="false" />p
                            </td>
                            <td>
                                <span class="badge px-3 py-2 ${orderDetail.deliverstatus == '1' ? 'bg-warning text-dark' : 'bg-success'}">
                                    ${orderDetail.deliverstatus == '1' ? '배송중' : '배송완료'}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                <c:if test="${empty requestScope.orderDetailList}">
                    <tr>
                        <td colspan="7" class="text-center py-4 text-muted">주문내역이 존재하지 않습니다.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../../footer.jsp" />
