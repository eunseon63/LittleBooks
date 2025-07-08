<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctxPath = request.getContextPath();
%>

<!-- Custom CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/cart/cartList.css" />

<jsp:include page="/WEB-INF/header1.jsp" />

<!-- SweetAlert -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>

<script>
//로그인 여부 확인 (true/false)
const isLoggedIn = ${not empty sessionScope.loginuser ? "true" : "false"};

//전체 선택/해제 체크박스 기능
function allCheckBox() {
    const checkboxes = document.querySelectorAll("input[name='bookseq']");
    const all = document.getElementById("allCheckOrNone").checked;
    checkboxes.forEach(chk => chk.checked = all);
}

//개별 체크박스 변경 시 전체 체크 여부 갱신
function individualCheckBoxChanged() {
    const allCheckBox = document.getElementById("allCheckOrNone");
    const checkboxes = document.querySelectorAll("input[name='bookseq']");
    const allChecked = Array.from(checkboxes).every(chk => chk.checked);
    allCheckBox.checked = allChecked;
}

//DOM이 로드되면 개별 체크박스에 이벤트 바인딩
document.addEventListener('DOMContentLoaded', function() {
    const checkboxes = document.querySelectorAll("input[name='bookseq']");
    checkboxes.forEach(chk => {
        chk.addEventListener('change', individualCheckBoxChanged);
    });
});

//장바구니 항목 삭제 함수
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

                        location.reload();
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

function updateTotalSummary() {
    let totalPrice = 0;

    document.querySelectorAll("tbody tr").forEach(tr => {
        const isChecked = tr.querySelector("input[name='bookseq']") !== null;
        if (isChecked) {
            const qty = parseInt(tr.querySelector("input[name='cqty']").value);
            const priceText = tr.querySelector("td:nth-child(5)").innerText.replace(/[^0-9]/g, '');
            const price = parseInt(priceText);

            totalPrice += qty * price;
        }
    });

    const totalPriceCell = document.querySelector(".total-price-row td:nth-child(2)");
    const pointCell = document.querySelector(".total-price-row td:nth-child(3)");

    // 가격 포맷 (3자리마다 콤마 추가)
    const formattedPrice = totalPrice.toLocaleString();
    const formattedPoint = Math.floor(totalPrice * 0.01).toLocaleString();

    if (totalPriceCell && pointCell) {
        totalPriceCell.innerHTML = `총 합계: ${formattedPrice} 원`;
        pointCell.innerHTML = `예상 적립 포인트: ${formattedPoint} Point`;
    }
}

//장바구니 수량 수정 함수
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

//선택한 상품들 주문 요청 함수
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