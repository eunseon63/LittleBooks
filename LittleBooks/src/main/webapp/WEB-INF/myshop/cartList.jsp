<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctxPath = request.getContextPath();
%>

<jsp:include page="/WEB-INF/header1.jsp" />

<!-- SweetAlert -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>

<style>
body {
    font-family: 'Noto Sans KR', sans-serif;
    background-color: #fffdf8;
    margin: 0;
    padding: 0;
}

.cart-container {
    max-width: 1100px;
    margin: 80px auto;
    background-color: #fff;
    padding: 40px;
    border-radius: 16px;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.05);
}

.cart-title {
    font-size: 26px;
    font-weight: 700;
    text-align: center;
    margin-bottom: 36px;
    color: #333;
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

.cart-table td img {
    width: 80px;
    height: auto;
    border-radius: 8px;
    box-shadow: 0 1px 4px rgba(0,0,0,0.1);
}

input[type="number"] {
    width: 60px;
    text-align: center;
    padding: 4px;
    border: 1px solid #ddd;
    border-radius: 6px;
}

.btn-yellow {
    background-color: #FFD600;
    color: #222;
    border: none;
    border-radius: 8px;
    padding: 10px 24px;
    font-weight: 600;
    transition: background-color 0.2s ease;
    box-shadow: 0 3px 6px rgba(0,0,0,0.1);
}

.btn-yellow:hover {
    background-color: #FFC107;
}

.btn-outline-yellow {
    background-color: #fff;
    color: #FFC107;
    border: 2px solid #FFC107;
    padding: 9px 22px;
    font-weight: 600;
    border-radius: 8px;
    transition: all 0.2s ease;
}

.btn-outline-yellow:hover {
    background-color: #FFC107;
    color: #fff;
}

.delete-btn {
    background-color: transparent;
    color: #ff5252;
    font-size: 16px;
    border: none;
    cursor: pointer;
    transition: transform 0.2s;
}

.delete-btn:hover {
    transform: scale(1.2);
    color: #d00000;
}

.total-price-row {
    background-color: #fffdea;
    font-weight: bold;
    font-size: 16px;
    color: #444;
}

.actions {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 30px;
}
</style>


<script>
const isLoggedIn = ${not empty sessionScope.loginuser ? "true" : "false"};

function allCheckBox() {
    const checkboxes = document.querySelectorAll("input[name='bookseq']");
    const all = document.getElementById("allCheckOrNone").checked;
    checkboxes.forEach(chk => chk.checked = all);
}

function individualCheckBoxChanged() {
    const allCheckBox = document.getElementById("allCheckOrNone");
    const checkboxes = document.querySelectorAll("input[name='bookseq']");
    const allChecked = Array.from(checkboxes).every(chk => chk.checked);
    allCheckBox.checked = allChecked;
}

document.addEventListener('DOMContentLoaded', function() {
    const checkboxes = document.querySelectorAll("input[name='bookseq']");
    checkboxes.forEach(chk => {
        chk.addEventListener('change', individualCheckBoxChanged);
    });
});

function goDel(cartseq) {
    swal({
        title: "삭제 확인",
        text: "해당 상품을 장바구니에서 삭제하시겠습니까?",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#ff5252",
        confirmButtonText: "삭제",
        cancelButtonText: "취소"
    }, function(isConfirm) {
        if (isConfirm) {
            $.ajax({
                url: "<%= ctxPath %>/shop/cartDel.go",
                type: "POST",
                data: { cartseq: cartseq },
                dataType: "json",
                success: function(json) {
                    if (json.n == 1) {
                        swal("삭제 완료!", "상품이 장바구니에서 삭제되었습니다.", "success");

                        // 해당 항목 DOM에서 제거
                        $("#cart_" + cartseq).remove();

                        // 만약 장바구니가 비었다면 "비어 있음" 메시지도 갱신 필요 (옵션)
                    } else {
                        swal("삭제 실패", "삭제할 수 없습니다.", "error");
                    }
                },
                error: function(request, status, error) {
                    swal("오류 발생", "삭제 중 오류가 발생했습니다.", "error");
                }
            });
        }
    });
}


function goOqtyEdit(obj) {
    const index = $('button.updateBtn').index(obj);

    const cartseq = $('input.cartseq').eq(index).val(); // 장바구니 번호
    const cqty = $('input.cqty').eq(index).val();       // 수정 수량
    const bqty = $('input.bqty').eq(index).val();       // 재고 수량

    const regExp = /^[0-9]+$/g;
    if (!regExp.test(cqty) || Number(cqty) < 0) {
        alert("수량은 0 이상의 숫자여야 합니다.");
        location.reload();
        return;
    }

    if (Number(cqty) > Number(bqty)) {
        alert("주문수량이 재고 수량을 초과했습니다.");
        location.reload();
        return;
    }

    if (cqty === "0") {
        goDel(cartseq);
    } else {
        $.ajax({
            url: "<%= ctxPath %>/shop/cartEdit.go",
            type: "POST",
            data: { cartseq: cartseq, cqty: cqty },
            dataType: "json",
            success: function(json) {
                if (json.n == 1) {
                    alert("주문수량이 변경되었습니다.");
                    location.reload();
                } else {
                    alert("수량 변경 실패");
                }
            },
            error: function(request, status, error) {
                alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
            }
        });
    }
}

function goOrder() {
    if (!isLoggedIn) {
        swal("로그인 필요", "로그인 후 주문해 주세요.", "warning");
        location.href = "<%= ctxPath %>/login/login.go";
        return;
    }

    const checkedBoxes = document.querySelectorAll("input[name='bookseq']:checked");
    if (checkedBoxes.length === 0) {
        swal("선택 오류", "주문할 상품을 1개 이상 선택해주세요!", "warning");
        return;
    }

    let bookseqArr = [], oqtyArr = [], priceArr = [], cartseqArr = [], totalPrice = 0;

    for (const checkbox of checkedBoxes) {
        const tr = checkbox.closest("tr");
        const bookseq = checkbox.value;
        const qty = parseInt(tr.querySelector("input[name='cqty']").value);
        const price = parseInt(tr.querySelector("td:nth-child(5)").innerText.replace(/[^0-9]/g, ''));
        const cartseq = tr.querySelector("input[name='cartseq']").value;  // 장바구니 번호

        if (isNaN(qty) || qty < 1) {
            swal("수량 오류", "수량은 1 이상이어야 합니다.", "warning");
            return;
        }

        bookseqArr.push(bookseq);
        oqtyArr.push(qty);
        priceArr.push(price);
        cartseqArr.push(cartseq);  // 추가
        totalPrice += qty * price;
    }

    // 숨겨진 input에 값 넣기
    document.querySelector("input[name='str_bookseq_join']").value = bookseqArr.join(",");
    document.querySelector("input[name='str_oqty_join']").value = oqtyArr.join(",");
    document.querySelector("input[name='str_price_join']").value = priceArr.join(",");
    document.querySelector("input[name='str_cartseq_join']").value = cartseqArr.join(",");  // 추가
    document.querySelector("input[name='sum_totalPrice']").value = totalPrice;

    document.getElementById("orderForm").submit();
}


</script>

<div class="cart-container" style="margin-top:140px;">
    <div class="cart-title">
        ${sessionScope.loginuser.name} [${sessionScope.loginuser.userid}]님의 장바구니
    </div>

    <c:if test="${empty requestScope.cartList}">
        <div class="text-center text-danger font-weight-bold">장바구니에 담긴 상품이 없습니다.</div>
    </c:if>

    <c:if test="${not empty requestScope.cartList}">
        <table class="cart-table">
            <thead>
                <tr>
                    <th><input type="checkbox" id="allCheckOrNone" onclick="allCheckBox()" /></th>
                    <th>도서 이미지</th>
                    <th>도서명</th>
                    <th>수량</th>
                    <th>가격</th>
                    <th>합계</th>
                    <th>삭제</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cartvo" items="${requestScope.cartList}">
                    <tr id="cart_${cartvo.cartseq}">
                        <td><input type="checkbox" name="bookseq" value="${cartvo.fk_bookseq}" /></td>
                        <td><img src="<%= ctxPath %>/images/${cartvo.bvo.bimage}" alt="도서 이미지" /></td>
                        <td>${cartvo.bvo.bname}</td>
                        <td>
                            <input type="number" name="cqty" class="cqty" value="${cartvo.cqty}" min="1" max="99" />
                            <button type="button" class="btn btn-outline-secondary btn-sm updateBtn" onclick="goOqtyEdit(this)">수정</button>
                            <input type="hidden" name="bqty" class="bqty" value="${cartvo.bvo.bqty}" />
                            <input type="hidden" name="cartseq" class="cartseq" value="${cartvo.cartseq}" />
                        </td>
                        <td><fmt:formatNumber value="${cartvo.bvo.price}" pattern="###,###" /> 원</td>
                        <td><fmt:formatNumber value="${cartvo.bvo.price * cartvo.cqty}" pattern="###,###" /> 원</td>
                        <td><button type="button" class="delete-btn" onclick="goDel('${cartvo.cartseq}')">🗑</button></td>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot>
                <tr class="total-price-row">
                    <td></td>
                    <td colspan="4" class="text-right" style="font-weight:700; font-size:16px;">
                        총 합계: <fmt:formatNumber value="${requestScope.sumMap.SUMTOTALPRICE}" pattern="###,###" /> 원
                    </td>
                    <td style="font-weight:700; font-size:16px; color:#999999">
                        예상 적립 포인트: <fmt:formatNumber value="${requestScope.sumMap.SUMTOTALPRICE * 0.01}" pattern="###,###" /> Point
                    </td>
                    <td></td>
                </tr>
            </tfoot>
        </table>

        <div class="actions">
            <button type="button" class="btn-yellow" onclick="goOrder()">주문하기</button>
            <a href="<%= ctxPath %>/index.go" class="btn-outline-yellow">계속 쇼핑</a>
            <form id="orderForm" method="post" action="<%= ctxPath %>/shop/payment.go">
			    <input type="hidden" name="str_bookseq_join" />
			    <input type="hidden" name="str_oqty_join" />
			    <input type="hidden" name="str_price_join" />
			    <input type="hidden" name="sum_totalPrice" />
			    <input type="hidden" name="str_cartseq_join" />
			</form>

        </div>
    </c:if>
</div>

<jsp:include page="/WEB-INF/footer.jsp" />