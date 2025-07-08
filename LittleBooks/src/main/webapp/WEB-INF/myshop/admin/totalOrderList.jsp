<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String ctxPath = request.getContextPath();
%>

<jsp:include page="../../header1.jsp" />

<style>
    body {
        font-family: 'Helvetica Neue', sans-serif;
        background: #f5f5f5;
        color: #333;
    }
    .container {
        max-width: 900px;
        margin: 40px auto 80px;
        padding: 0 15px;
    }
    h3 {
        text-align: center;
        margin-bottom: 40px;
        font-weight: 700;
        font-size: 1.8rem;
        color: #222;
    }
    .order-box {
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 3px 8px rgba(0,0,0,0.1);
        margin-bottom: 40px;
        padding: 20px 25px;
    }
    .order-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 2px solid #eee;
        padding-bottom: 12px;
        margin-bottom: 20px;
        font-weight: 600;
        font-size: 1.1rem;
    }
    .order-header div {
        color: #555;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        font-size: 1rem;
    }
    thead {
        background-color: #fef7d1;
    }
    th, td {
        border: 1px solid #ddd;
        padding: 12px 15px;
        text-align: center;
        vertical-align: middle;
    }
    td.product-info {
        text-align: left;
        display: flex;
        align-items: center;
        gap: 15px;
    }
    td.product-info img {
        width: 60px;
        height: auto;
        border-radius: 5px;
        border: 1px solid #ccc;
    }
    td.product-info div {
        line-height: 1.3;
        color: #444;
    }
    td.product-info div strong {
        color: #222;
    }

    .btn-delivery {
        padding: 6px 18px;
        font-weight: 700;
        border-radius: 20px;
        border: none;
        cursor: default;
        user-select: none;
        font-size: 0.9rem;
    }
    
    .btn-delivering {
        background-color: #f0ad4e;
        color: #000;
    }
    
    .btn-delivered {
        background-color: #5cb85c;
        color: #fff;
    }
    
    .btn-waiting {
	    background-color: #999; /* 예: 회색 */
	    color: #fff;
	}

    .summary-info {
        margin-top: 20px;
        background-color: #fafafa;
        border: 1px solid #ddd;
        border-radius: 6px;
        padding: 15px 25px;
        font-size: 1rem;
        display: flex;
        justify-content: space-around;
        flex-wrap: wrap;
        gap: 15px;
        color: #333;
        font-weight: 600;
    }
    .summary-info div {
        min-width: 150px;
        text-align: center;
    }

    /* 행 호버 효과 */
    .orderTbl tr.orderInfo:hover {
        background-color: #fffde7;
        cursor: pointer;
        transition: background-color 0.2s ease-in-out;
    }
</style>

<!-- jQuery 꼭 로드되어 있어야 합니다! -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script type="text/javascript">
	$(function () {
	    // 이벤트 위임: #orderTbl 내 tr.orderInfo 클릭 시 팝업 띄우기
	    $('.orderTbl').on('click', 'tr.orderInfo', function (e) {

	    	 // 버튼을 클릭한 경우는 무시 (상위 tr의 클릭 이벤트 차단)
	        if ($(e.target).closest('.toggleDeliveryBtn').length > 0) {
	            return;
	        }
	    	 
	        const ordercode = $(this).data("ordercode");
	        if(!ordercode) {
	            alert("주문코드가 없습니다.");
	            return;
	        }
	        const popupUrl = "<%= ctxPath %>/myshop/userOrderDetail.go?ordercode=" + encodeURIComponent(ordercode);
		    const popupOptions = "width=800,height=600,scrollbars=yes,resizable=no";
	        window.open(popupUrl, "memberDetailPopup", popupOptions);
	    });
	});
	
	$(document).on("click", ".toggleDeliveryBtn", function () {
	    const $btn = $(this);
	    const ordercode = $btn.data("ordercode");
	    const currentStatus = $btn.data("status"); // 1 : 배송 중 / 2 : 배송 완료 
	    
	    let nextStatus;
	    if (currentStatus == "0") nextStatus = "1";
	    else if (currentStatus == "1") nextStatus = "2";
	    else {
	        // 배송 완료(2) 이상 상태에서는 버튼 비활성화
	        $btn.prop("disabled", true);
	        return; // 함수 종료
	    }

	    $.ajax({
	        url: "<%= ctxPath %>/myshop/updateDeliverStatus.go",
	        method: "POST",
	        data: {
	            ordercode: ordercode,
	            status: nextStatus
	        },
	        success: function (response) {
	            const trimmed = response.trim();  // 공백 제거
	            console.log("응답:", trimmed); // 디버깅용
	            if (trimmed === "success") {
	                $btn.data("status", nextStatus);

	                // 기존 상태 클래스 모두 제거
	                $btn.removeClass("btn-waiting btn-delivering btn-delivered");

	                // 다음 상태에 맞는 클래스와 텍스트 지정
	                if (nextStatus === "0") {
	                    $btn.addClass("btn-waiting");
	                    $btn.text("배송대기");
	                } else if (nextStatus === "1") {
	                    $btn.addClass("btn-delivering");
	                    alert('배송이 시작되었습니다.');
	                    $btn.text("배송중");
	                    location.reload(); // ⭐ 이게 즉시 새로고침시킴
	                } else if (nextStatus === "2") {
	                    $btn.addClass("btn-delivered");
	                    alert('배송이 시작되었습니다.');
	                    $btn.text("배송완료");
	                    location.reload(); // ⭐ 이게 즉시 새로고침시킴
	                }
	            } else {
	                alert("상태 변경에 실패했습니다. 서버 응답: " + trimmed);
	            }
	        },
	        error: function () {
	            alert("서버 오류가 발생했습니다.");
	        }
	    });
	});
</script>

<div class="container">
    <h3>주문내역 전체 목록</h3>

    <c:set var="prevOrderCode" value="" />
    <c:forEach var="orderDetail" items="${requestScope.orderDetailList}" varStatus="loop">
        <c:set var="currentOrderCode" value="${orderDetail.fk_ordercode}" />

        <c:if test="${prevOrderCode ne currentOrderCode}">
            <div class="order-box">

                <div>
                    <div>주문코드: ${orderDetail.fk_ordercode}</div>
                    <div>주문일자: <c:out value="${fn:substring(orderDetail.order.orderdate, 0, 10)}" /></div>
                </div>

                <table class="orderTbl">
                    <thead>
                        <tr>
                            <th>제품정보</th>
                            <th>수량</th>
                            <th>가격(단가)</th>
                            <th>총 가격</th>
                            <th>배송 상태</th>
                        </tr>
                    </thead>
                    <tbody>
        </c:if>

        <tr class="orderInfo" data-ordercode="${orderDetail.fk_ordercode}">
            <td class="product-info">
                <img src="<%= ctxPath %>/images/${orderDetail.book.bimage}" alt="${orderDetail.book.bname}" />
                <div>
                    <div><strong>${orderDetail.book.bname}</strong></div>
                    <div>저자: ${orderDetail.book.author}</div>
                </div>
            </td>
            <td>${orderDetail.oqty}</td>
            <td><fmt:formatNumber value="${orderDetail.odrprice}" type="currency" currencySymbol="₩" /></td>
            <td><fmt:formatNumber value="${orderDetail.odrprice * orderDetail.oqty}" type="currency" currencySymbol="₩" /></td>
            <td>
                <button
				    class="btn-delivery toggleDeliveryBtn
				        ${orderDetail.deliverstatus == '0' ? 'btn-waiting' : ''}
				        ${orderDetail.deliverstatus == '1' ? 'btn-delivering' : ''}
				        ${orderDetail.deliverstatus == '2' ? 'btn-delivered' : ''}"
				    data-ordercode="${orderDetail.fk_ordercode}"
				    data-status="${orderDetail.deliverstatus}">
				    ${orderDetail.deliverstatus == '0' ? '배송대기' : ''}
				    ${orderDetail.deliverstatus == '1' ? '배송중' : ''}
				    ${orderDetail.deliverstatus == '2' ? '배송완료' : ''}
				</button>
            </td>
        </tr>

        <c:if test="${(loop.last) || (fn:length(requestScope.orderDetailList) > loop.index + 1 && requestScope.orderDetailList[loop.index + 1].fk_ordercode ne currentOrderCode)}">
                    </tbody>
                </table>

                <div class="summary-info">
                    <div>총 결제 금액: <fmt:formatNumber value="${orderDetail.order.totalPrice}" type="currency" currencySymbol="₩" /></div>
                    <div>사용 포인트: <fmt:formatNumber value="${orderDetail.order.usePoint}" type="number" /> p</div>
                    <div>적립 포인트: <fmt:formatNumber value="${orderDetail.order.totalPrice * 0.01}" type="number" maxFractionDigits="0" /> p</div>
                </div>

            </div>
        </c:if>

        <c:set var="prevOrderCode" value="${currentOrderCode}" />
    </c:forEach>
</div>

<jsp:include page="../../footer.jsp" />