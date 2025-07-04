<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/header1.jsp" />

<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script src="https://cdn.iamport.kr/js/iamport.payment-1.1.8.js"></script>

<script>
$(document).ready(function() {
    const IMP = window.IMP;
    IMP.init("imp53250123");
    
    const str_bookseq_join = "<c:out value='${str_bookseq_join}' default='' />";
    const str_oqty_join = "<c:out value='${str_oqty_join}' default='' />";
    const str_price_join = "<c:out value='${str_price_join}' default='' />";
    const str_cartseq_join = "<c:out value='${str_cartseq_join}' default='' />";
    
    let sum_totalPrice = parseInt("<c:out value='${sum_totalPrice}' default='0' />", 10);
    const originalTotalPrice = sum_totalPrice;

    $("#payBtn").click(function() {
    	if (!$("#receiver_name").val() || !$("#receiver_phone").val() || !$("#receiver_postcode").val() || !$("#receiver_detail_address").val()) {
    	    alert("배송지 정보를 모두 입력해주세요.");
    	    return;
    	}

        const merchant_uid = 'order_' + new Date().getTime();

        IMP.request_pay({
            pg: 'html5_inicis',
            pay_method: 'card',
            merchant_uid: merchant_uid,
            name: "장바구니 외 " + (<c:out value="${fn:length(bookList)-1}" default="0" />) + "건",
            amount: 100, //sum_totalPrice, 100원으로 임시 설정 
            buyer_email: '<c:out value="${sessionScope.loginuser.email}" default=""/>',
            buyer_name: $('#receiver_name').val(),
            buyer_tel: $('#receiver_phone').val(),
            buyer_addr: $('#receiver_address').val() + " " + $('#receiver_detail_address').val(),
            buyer_postcode: $('#receiver_postcode').val()
        }, function(rsp) {
            if (rsp.success) {
                $.ajax({
                    url: '<c:url value="/shop/orderAdd.go" />',
                    method: 'POST',
                    data: {
                        imp_uid: rsp.imp_uid,
                        merchant_uid: rsp.merchant_uid,
                        amount: rsp.paid_amount,

                        str_bookseq_join: str_bookseq_join,
                        str_oqty_join: str_oqty_join,
                        str_price_join: str_price_join,
                        sum_totalPrice: sum_totalPrice,
                        usepoint: $('#usepoint').val(),
                        str_cartseq_join: str_cartseq_join || "",

                        receiver_name: $('#receiver_name').val(),
                        receiver_phone: $('#receiver_phone').val(),
                        receiver_postcode: $('#receiver_postcode').val(),
                        receiver_address: $('#receiver_address').val(),
                        receiver_detail_address: $('#receiver_detail_address').val(),
                        receiver_extra_address: $('#receiver_extra_address').val()
                    },
                    success: function(res) {
                        alert("결제가 완료되었습니다.");
                        location.href = "<c:url value='/login/orderList.go' />";
                    },
                    error: function(xhr, status, error) {
                        alert("서버 오류: " + error);
                    }
                });
            } else {
                alert("결제 실패: " + rsp.error_msg);
            }
        });
    });

    window.applyPoint = function() {
        const usepoint = parseInt($("#usepoint").val(), 10) || 0;
        const availablePoint = parseInt($("#availablePoint").text().replace(/,/g, ""), 10);

        if (usepoint < 0) {
            alert("포인트는 0 이상만 입력 가능합니다.");
            $("#usepoint").val(0);
            return;
       }

        if (usepoint > availablePoint) {
            alert("보유 포인트보다 많이 사용할 수 없습니다.");
            $("#usepoint").val(0);
            return;
        }

        if (usepoint > originalTotalPrice) {
            alert("총 결제금액보다 많은 포인트는 사용할 수 없습니다.");
            $("#usepoint").val(0);
            return;
        }

        sum_totalPrice = originalTotalPrice - usepoint;
        $("#finalPrice").text(sum_totalPrice.toLocaleString() + " 원");
    };
});

// 주소 검색 API
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            $('#receiver_postcode').val(data.zonecode);
            $('#receiver_address').val(data.address);
            $('#receiver_detail_address').val(data.buildingName || '');
            $('#receiver_extra_address').focus();
        }
    }).open();
}
</script>

<style>
.payment-wrapper {
    max-width: 700px;
    margin: 80px auto;
    padding: 40px;
    background-color: #fff;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.05);
    font-family: 'Noto Sans KR', sans-serif;
}
.payment-wrapper h2 {
    font-size: 28px;
    font-weight: bold;
    margin-bottom: 30px;
    color: #222;
    border-bottom: 1px solid #eee;
    padding-bottom: 10px;
}
.payment-detail div {
    margin-bottom: 15px;
}
.payment-detail strong {
    display: inline-block;
    width: 100px;
    color: #555;
}
.total-price {
    font-size: 20px;
    font-weight: bold;
    margin-top: 20px;
    color: #d00000;
}
.pay-buttons {
    margin-top: 40px;
    display: flex;
    gap: 20px;
}
.pay-buttons button {
    padding: 14px 32px;
    font-size: 16px;
    font-weight: 600;
    border-radius: 8px;
    border: none;
    cursor: pointer;
    background-color: #f4c900;
    color: #222;
    transition: 0.2s ease;
    box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}
.pay-buttons button:hover {
    background-color: #ddb900;
}
.point {
    margin-top: 15px;
}
.cart-table {
    width: 100%;
    border-collapse: collapse;
}
.cart-table th, .cart-table td {
    padding: 18px;
    text-align: center;
    vertical-align: middle;
    border-bottom: 1px solid #eee;
}
.cart-table th {
    background-color: #fff9e5;
    color: #444;
    font-weight: 600;
}
.cart-table td {
    text-align: center;
}
.total-price-row {
    background-color: #fffdea;
    font-weight: bold;
    font-size: 16px;
    color: #444;
}
.payment-form-group {
    display: flex;
    align-items: center;
    margin-bottom: 12px;
    gap: 10px;
}
.payment-form-group label {
    width: 120px;
    font-weight: 500;
    color: #333;
    text-align: right;
}
.payment-form-group input[type="text"],
.payment-form-group input[type="number"] {
    padding: 6px 10px;
    font-size: 14px;
    border: 1px solid #ccc;
    border-radius: 6px;
    height: 32px;
}
.payment-form-group button {
    padding: 5px 10px;
    font-size: 13px;
    background-color: #ffd600;
    border: none;
    border-radius: 6px;
    cursor: pointer;
}
.payment-form-group button:hover {
    background-color: #ffc107;
}
.input-wide {
    width: 50px;
    padding: 6px 10px;
    font-size: 14px;
}
input#receiver_address {
    width: 450px;
    padding: 6px 10px;
    font-size: 14px;
}
</style>

<div class="payment-wrapper">
    <h2>결제 확인</h2>


    <div class="payment-form-group">
	    <label for="receiver_name">받는 분:</label>
	    <input type="text" id="receiver_name" name="receiver_name" value="<c:out value='${sessionScope.loginuser.name}' />" />
	</div>
	
	<div class="payment-form-group">
	    <label for="receiver_phone">연락처:</label>
	    <input type="text" id="receiver_phone" name="receiver_phone" value="<c:out value='${sessionScope.loginuser.mobile}' />" />
	</div>
	
	<div class="payment-form-group">
	    <label for="receiver_postcode">우편번호:</label>
	    <input type="text" id="receiver_postcode" name="receiver_postcode" value="<c:out value='${sessionScope.loginuser.postcode}' />" readonly />
	    <button type="button" onclick="execDaumPostcode()">주소 검색</button>
	</div>

    <div class="payment-form-group">
        <label for="receiver_address">주소:</label>
        <input type="text" id="receiver_address" name="receiver_address" value="<c:out value='${sessionScope.loginuser.address}' />" readonly />
    </div>

    <div class="payment-form-group">
        <label for="receiver_detail_address">상세주소:</label>
        <input type="text" id="receiver_detail_address" name="receiver_detail_address" value="<c:out value='${sessionScope.loginuser.detailaddress}' />" />
    </div>

    <div class="payment-form-group">
        <label for="receiver_extra_address">참고항목:</label>
        <input type="text" id="receiver_extra_address" name="receiver_extra_address" value="<c:out value='${sessionScope.loginuser.extraaddress}' />" />
    </div>
    
    <table class="cart-table" style="max-width: 700px; margin: 0 auto 30px;">
        <thead>
            <tr>
                <th>도서명</th>
                <th>구매 수량</th>
                <th>가격</th>
                <th>합계</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="book" items="${bookList}" varStatus="status">
                <tr>
                    <td>${book.bname}</td>
                    <td>${qtyList[status.index]}</td>
                    <td><fmt:formatNumber value="${book.price}" pattern="###,###" /> 원</td>
                    <td><fmt:formatNumber value="${qtyList[status.index] * book.price}" pattern="###,###" /> 원</td>
                </tr>
            </c:forEach>
        </tbody>
        <tfoot>
            <tr class="total-price-row">
                <td colspan="3" style="text-align:right;">총 결제 금액</td>
                <td><fmt:formatNumber value="${sum_totalPrice}" pattern="###,###" /> 원</td>
            </tr>
        </tfoot>
    </table>
    
    <div class="payment-form-group point">
	    <label>사용 가능 포인트:</label>
	    <span id="availablePoint"><c:out value='${sessionScope.loginuser.point}' default='0' /></span> P
	</div>
	
	<div class="payment-form-group">
	    <label for="usedPoint">사용 포인트:</label>
	    <input type="number" id="usepoint" value="0" placeholder="0" min="0" style="width:150px;" oninput="applyPoint()" />
	</div>
	
	<div class="payment-form-group">
	    <label>최종 결제금액:</label>
	    <span id="finalPrice" style="font-weight:bold; color:#d00000;">
	        <fmt:formatNumber value="${sum_totalPrice}" pattern="###,###" /> 원
	    </span>
	</div>

    <div class="pay-buttons">
        <button type="button" id="payBtn">결제 진행</button>
        <button type="button" onclick="history.back()">취소</button>
    </div>
</div>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<jsp:include page="/WEB-INF/footer.jsp" />
