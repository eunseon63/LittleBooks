<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String ctxPath = request.getContextPath();
%>

<%
   boolean isLogin = (session.getAttribute("loginuser") != null);
%>

<jsp:include page="/WEB-INF/header1.jsp" />

<!-- SweetAlert CSS/JS -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> 
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>

<!-- jQuery & jQuery UI -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" />
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>

<style>
body {
    font-family: 'Noto Sans KR', 'Segoe UI', sans-serif;
    background-color: #fefefe;
    color: #333;
}

.detail-wrapper {
    max-width: 1100px;
    margin: 100px auto 60px;
    display: flex;
    gap: 60px;
    background-color: #fff;
    padding: 40px;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.05);
}

.left-box {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
}
.left-box img {
    width: 100%;
    max-width: 360px;
    border: 1px solid #eee;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    background: #fff;
}

.right-box {
    flex: 1.4;
    display: flex;
    flex-direction: column;
    gap: 18px;
}

.right-box h2 {
    font-size: 30px;
    font-weight: 700;
    margin-bottom: 6px;
    color: #222;
}

.book-info {
    font-size: 15px;
    line-height: 1.8;
}
.book-info div {
    margin-bottom: 4px;
}
.book-info strong {
    color: #555;
    margin-right: 6px;
}

.select-box label {
    font-weight: 500;
    margin-right: 8px;
}

/* 스피너 입력칸 */
.select-box input {
    width: 70px;
    height: 25px;
    font-size: 14px;
    padding: 4px 8px;
    box-sizing: border-box;
    /* 오른쪽에 버튼 공간 확보 */
    padding-right: 28px;
}

/* 스피너 전체 박스 */
.ui-spinner {
    position: relative;
    display: inline-block;
    vertical-align: middle;
}

/* 스피너 버튼들 공통 */
.ui-spinner-button {
    position: absolute;
    right: 1px;
    width: 26px !important;
    height: 14px !important; /* 반반으로 나눔 */
    padding: 0 !important;
    margin: 0 !important;
    line-height: 14px !important;
    overflow: hidden !important;
    cursor: pointer;
    transition: background-color 0.2s ease;
}

/* 위쪽 버튼 */
.ui-spinner-up {
    top: 1px;
    border-bottom-left-radius: 0;
    border-bottom-right-radius: 0;
}

/* 아래쪽 버튼 */
.ui-spinner-down {
    bottom: 1px;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
}

.ui-spinner-button:hover {
    background-color: #ddb900 !important;
}

.price-section {
    font-size: 18px;
    font-weight: bold;
    color: #222;
    margin-top: 10px;
}

.button-group {
    display: flex;
    gap: 14px;
    margin-top: 20px;
}
.button-group button {
    padding: 12px 26px;
    font-size: 16px;
    font-weight: 600;
    border: none;
    border-radius: 8px;
    background-color: #f4c900;
    color: #222;
    cursor: pointer;
    box-shadow: 0 3px 8px rgba(0,0,0,0.1);
    transition: all 0.2s ease;
}
.button-group button:hover {
    background-color: #ddb900;
}

.section-box {
    max-width: 1000px;
    margin: 40px auto;
    background-color: #fffef3;
    border-radius: 12px;
    padding: 30px 40px;
    border: 1px solid #f4c900;
}

.section-box h3 {
    font-size: 22px;
    font-weight: bold;
    margin-bottom: 16px;
    border-bottom: 1px solid #eee;
    padding-bottom: 10px;
    color: #111;
}

.section-box p {
    font-size: 16px;
    line-height: 1.8;
}

.review-card {
    background-color: #fff;
    padding: 20px;
    border-radius: 10px;
    border: 1px solid #eee;
    margin-top: 12px;
}
.review-card .star {
    color: #f4c900;
    font-size: 18px;
}
.book-spec {
    color: #d00000;
    font-weight: bold;
    font-size: 14px;
    margin-bottom: 4px;
    letter-spacing: 0.5px;
}
</style>

<script>
$(function(){
    // jQuery UI 스피너 세팅 (1~100)
    $("#spinner").spinner({
        min: 1,
        max: 100,
        spin: function(event, ui) {
            if (ui.value > 100) {
                $(this).spinner("value", 100);
                return false;
            }
            if (ui.value < 1) {
                $(this).spinner("value", 1);
                return false;
            }
            updateTotalPrice(ui.value);
        },
        change: function(event, ui) {
            let val = parseInt($(this).val());
            if (isNaN(val) || val < 1) {
                $(this).spinner("value", 1);
                val = 1;
            } else if (val > 100) {
                $(this).spinner("value", 100);
                val = 100;
            }
            updateTotalPrice(val);
        }
    });

    // 초기 총 가격 계산
    updateTotalPrice(parseInt($("#spinner").val()));

    // 수량 직접 입력 시에도 총 가격 업데이트
    $("#spinner").on("input", function() {
        let val = parseInt($(this).val());
        if (isNaN(val) || val < 1) {
            val = 1;
        } else if (val > 100) {
            val = 100;
        }
        updateTotalPrice(val);
    });
});

function updateTotalPrice(qty) {
    const price = ${book.price};
    const total = qty * price;
    $("#totalPrice").text(total.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW' }));
}

function goCart() {
    const frm = document.cartOrderFrm;
    const cqty = frm.cqty.value;
    const regExp = /^[1-9][0-9]*$/;
    var isLogin = <%= isLogin %>;

    if(!regExp.test(cqty) || cqty < 1 || cqty > 100){
        swal("수량 오류", "수량은 1에서 100 사이의 숫자만 가능합니다.", "warning");
        frm.cqty.focus();
        return false;
    }

    if (!isLogin) {
        swal({
            title: "로그인이 필요합니다!",
            text: "장바구니에 담으려면 먼저 로그인해주세요.",
            type: "warning"
        }, function() {
            location.href = "<%= ctxPath %>/login/login.go";
        });
    } else {
        // 로그인 된 경우에만 진행
        swal({
            title: "장바구니 담기 완료!",
            text: "선택하신 상품이 장바구니에 추가되었습니다.",
            type: "success"
        }, function() {
            frm.method = "post";
            frm.action = "<%= ctxPath %>/shop/cartAdd.go";
            frm.submit();
        });
    }
}
</script>

<div class="detail-wrapper">
    <!-- 이미지 -->
    <div class="left-box">
        <c:choose>
            <c:when test="${not empty book.bimage}">
                <img src="${pageContext.request.contextPath}/images/${book.bimage}" alt="책 이미지" />
            </c:when>
            <c:otherwise>
                <div style="width: 360px; height: 360px; display: flex; align-items: center; justify-content: center; border: 1px solid #eee; border-radius: 12px; background: #f9f9f9;">
                    책 이미지 없음
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- 상세 정보 -->
    <div class="right-box">
        <!-- 스펙 표시 (BEST / NEW 등) -->
        <c:choose>
            <c:when test="${book.fk_snum == 2}">
                <div class="book-spec">BEST(인기)!!</div>
            </c:when>
            <c:when test="${book.fk_snum == 3}">
                <div class="book-spec">NEW(신상)!!</div>
            </c:when>
        </c:choose>

        <h2>${book.bname}</h2>
        <div class="book-info">
            <div><strong>출판사:</strong> ${book.pvo.pname}</div>
            <div><strong>저자:</strong> ${book.author}</div>
            <div><strong>가격:</strong> 
                <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₩" />
            </div>
        </div>

        <form name="cartOrderFrm">
            <div class="select-box">
                <label for="spinner">수량 선택</label>
                <input type="text" id="spinner" name="cqty" value="1" autocomplete="off" />
            </div>

            <div class="price-section">
                총 가격: <span id="totalPrice"></span>
            </div>


            <div class="button-group">
                <button type="button">결제하기</button>
                <button type="button" onclick="goCart()">장바구니</button>
            </div>
            <input type="hidden" name="fk_bookseq" value="${book.bookseq}" />
        </form>

    

    </div>
</div>

<!-- 책 설명 -->
<div class="section-box">
    <h3>책 설명</h3>
    <p>${book.bcontent}</p>
</div>

<!-- 리뷰 -->
<div class="section-box review">
    <h3>리뷰</h3>
    <div class="review-card">
        <p><strong>ID</strong> <span class="star">★★★★★</span><br>아이가 너무 좋아합니다!</p>
    </div>
</div>

<jsp:include page="/WEB-INF/footer.jsp" />
