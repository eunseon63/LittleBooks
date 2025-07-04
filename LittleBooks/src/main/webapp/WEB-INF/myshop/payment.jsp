<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/header1.jsp" />

<!-- jQuery & Iamport script -->
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script src="https://cdn.iamport.kr/js/iamport.payment-1.1.8.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script>
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var fullAddr = data.address;
            var extraAddr = '';

            if (data.addressType === 'R') {
                if (data.bname !== '') extraAddr += data.bname;
                if (data.buildingName !== '') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
            }

            $('#receiverPostcode').val(data.zonecode);
            $('#receiverAddress').val(fullAddr);
            $('#receiverExtraAddress').val(extraAddr);
            $('#receiverDetailAddress').focus();
        }
    }).open();
}

$(document).ready(function() {
    const IMP = window.IMP;
    IMP.init("imp53250123"); // 아임포트 가맹점 식별코드 입력

    $("#payBtn").click(function() {
        if (!$("#receiverName").val() || !$("#receiverPhone").val() || !$("#receiverPostcode").val() || !$("#receiverDetailAddress").val()) {
            alert("배송지 정보를 모두 입력해주세요.");
            return;
        }

        let merchant_uid = 'order_' + new Date().getTime(); // 유니크 주문번호

        // 총 결제 금액 텍스트 가져오기 (예: "₩12,345")
        let payPriceText = $("#payPrice").text();

        // 숫자만 추출 (₩와 , 제거)
        let amount = parseInt(payPriceText.replace(/[^\d]/g, ''));

        IMP.request_pay({
            pg: 'html5_inicis',
            pay_method: 'card',
            merchant_uid: merchant_uid,
            name: '${book.bname}',
            amount: 100, // 추후 수정 필요 
            buyer_email: '${sessionScope.loginUser.email}',
            buyer_name: $('#receiverName').val(),
            buyer_tel: $('#receiverPhone').val(),
            buyer_addr: $('#receiverAddress').val() + " " + $('#receiverDetailAddress').val(),
            buyer_postcode: $('#receiverPostcode').val()
        }, function(rsp) {
            if (rsp.success) {
                $.ajax({
                    url: '<c:url value="/myshop/paymentComplete.go" />',
                    method: 'POST',
                    data: {
                        imp_uid: rsp.imp_uid,
                        merchant_uid: rsp.merchant_uid,
                        amount: rsp.paid_amount,
                        bookseq: '${book.bookseq}',
                        qty: '${qty}',
                        receiverName: $('#receiverName').val(),
                        receiverPhone: $('#receiverPhone').val(),
                        receiverPostcode: $('#receiverPostcode').val(),
                        receiverAddress: $('#receiverAddress').val(),
                        receiverDetailAddress: $('#receiverDetailAddress').val(),
                        receiverExtraAddress: $('#receiverExtraAddress').val()
                    },
                    dataType:"json",
                    success: function(json) {
                        alert("결제가 완료되었습니다.");
                        location.href = "<c:url value='/myshop/orderSuccess.go' />";
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
});

$(function(){
	// 포인트 제외한 총 결제 금액 계산
	
    const bookPrice = ${book.price};
    let qty = parseInt(${qty});
    
    let totalPrice = bookPrice * qty;
    
    const availablePoint = parseInt($("#point").text());
    
 	// 포인트 입력값 유효성 검사 및 계산 함수
    function updatePayPrice() {
        let usePoint = parseInt($("#usepoint").val());
        if (isNaN(usePoint) || usePoint < 0) usePoint = 0;
        if (usePoint > availablePoint) usePoint = availablePoint;

        let finalPrice = totalPrice - usePoint;
        if (finalPrice < 0) finalPrice = 0;

        $("#payPrice").text(finalPrice.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW' }));
        $("#usepoint").val(usePoint);  // 잘못 입력된 경우 바로 수정해줌
    }

    // 초기 결제 금액 표시
    updatePayPrice();

    // usepoint 입력값 변경 시 이벤트
    $("#usepoint").on("input", function() {
        updatePayPrice();
    });
    
});


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
</style>

<div class="payment-wrapper">
    <h2>결제 확인</h2>
    <div class="payment-detail">
        <div><strong>도서명:</strong> ${book.bname}</div>
        <div><strong>저자:</strong> ${book.author}</div>
        <div><strong>출판사:</strong> ${book.pvo.pname}</div>
        <div><strong>단가:</strong> 
            <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₩" />
        </div>
        <div><strong>수량:</strong> ${qty}</div>

        <!-- 배송지 입력폼 -->
        <div><strong>받는 분:</strong> <input type="text" id="receiverName" value="${sessionScope.loginuser.name}" /></div>
        <div><strong>연락처:</strong> <input type="text" id="receiverPhone" value="${sessionScope.loginuser.mobile}" /></div>

        <div>
            <strong>우편번호:</strong>
            <input type="text" id="receiverPostcode" value="${sessionScope.loginuser.postcode}" readonly />
            <button type="button" onclick="execDaumPostcode()">주소 검색</button>
        </div>

        <div>
            <strong>주소:</strong><br />
            <input type="text" id="receiverAddress" size="50" placeholder="주소" value="${sessionScope.loginuser.address}" readonly /><br />
            <input type="text" id="receiverDetailAddress" size="50" placeholder="상세주소" value="${sessionScope.loginuser.detailaddress}" /><br />
            <input type="text" id="receiverExtraAddress" size="50" placeholder="참고항목" value="${sessionScope.loginuser.extraaddress}" />
        </div>

        
        <div class="point">
            사용 가능 포인트 : <span type="text" id="point">${sessionScope.loginuser.point}</span><br />
            <input type="text" id="usepoint" size="20" placeholder="포인트"/>
        </div>
        
        
        <!-- 총 결제 금액 -->
        <div class="total-price">
            총 결제 금액: <span id="payPrice"></span>
        </div>
        
    </div>

    <div class="pay-buttons">
        <button type="button" id="payBtn">결제 진행</button>
        <button type="button" onclick="history.back()">취소</button>
    </div>
</div>

<jsp:include page="/WEB-INF/footer.jsp" />
