<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
body {
    font-family: 'Segoe UI', 'Helvetica Neue', sans-serif;
    background: #f5f7fa;
    color: #333;
}

.info-wrapper {
    max-width: 800px;
    margin: 60px auto 80px;
    padding: 40px;
    background: #fff;
    border-radius: 14px;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.07);
}

.info-wrapper h3 {
    text-align: center;
    margin-bottom: 40px;
    font-size: 2rem;
    font-weight: 700;
    color: #222;
    border-bottom: 2px solid #ffe082;
    padding-bottom: 10px;
}

.info-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 40px;
}

.info-table thead th {
    background-color: #fff8e1;
    color: #444;
    padding: 16px;
    font-size: 1rem;
    border-bottom: 2px solid #ffe082;
    text-align: center;
}

.info-table tbody td {
    background-color: #ffffff;
    padding: 16px;
    text-align: center;
    border-bottom: 1px solid #f0f0f0;
    font-size: 0.95rem;
}

.info-table tbody tr:hover {
    background-color: #fdf6e3;
    transition: background-color 0.3s ease;
}

.btn-delivery {
    padding: 6px 14px;
    font-weight: 600;
    border-radius: 20px;
    border: none;
    font-size: 0.85rem;
    pointer-events: none;
}

.btn-waiting {
    background-color: #bdbdbd;
    color: #fff;
}

.btn-delivering {
    background-color: #ffd54f;
    color: #333;
}

.btn-delivered {
    background-color: #81c784;
    color: #fff;
}

.summary-box {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 20px;
    background: #fff8e1;
    border: 1px solid #ffe082;
    border-radius: 12px;
    padding: 25px 30px;
}

.summary-box div {
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.summary-box strong {
    font-weight: 700;
    color: #222;
    margin-bottom: 6px;
}

.summary-box p,
.summary-box span {
    font-size: 0.95rem;
    color: #444;
    line-height: 1.5;
}

@media (max-width: 576px) {
    .info-wrapper {
        padding: 20px;
    }
    .info-wrapper h3 {
        font-size: 1.6rem;
    }
    .info-table thead {
        display: none;
    }
    .info-table tbody td {
        display: flex;
        justify-content: space-between;
        padding: 10px 14px;
        border-bottom: 1px solid #ffe082;
    }
    .info-table tbody td::before {
        content: attr(data-label);
        font-weight: bold;
        color: #777;
    }
}
</style>

<div class="info-wrapper">
    <h3>주문 상세 내역 (주문번호: ${ordercode})</h3>

    <table class="info-table">
        <thead>
            <tr>
                <th>상품명</th>
                <th>수량</th>
                <th>가격</th>
                <th>배송상태</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="orderDetail" items="${orderOneDetailList}">
                <tr>
                    <td data-label="상품명">${orderDetail.book.bname}</td>
                    <td data-label="수량">${orderDetail.oqty}</td>
                    <td data-label="가격"><fmt:formatNumber value="${orderDetail.book.price}" type="currency" currencySymbol="₩" /></td>
                    <td data-label="배송상태">
                        <button type="button"
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
            </c:forEach>
        </tbody>
    </table>

    <div class="summary-box">
        <div>
            <strong>총 결제 금액</strong>
            <fmt:formatNumber value="${orderOneDetailList[0].order.totalPrice}" type="currency" currencySymbol="₩" />
        </div>
        <div>
            <strong>사용한 포인트</strong>
            <fmt:formatNumber value="${orderOneDetailList[0].order.usePoint}" type="number" /> p
        </div>
        <div>
            <strong>적립된 포인트</strong>
            <fmt:formatNumber value="${orderOneDetailList[0].order.totalPrice * 0.01}" type="number" maxFractionDigits="0" /> p
        </div>
        <div>
            <strong>배송지 정보</strong>
            <p>${orderOneDetailList[0].order.receiverName} / ${orderOneDetailList[0].order.receiverPhone}</p>
            <p>${orderOneDetailList[0].order.postcode} ${orderOneDetailList[0].order.address} ${orderOneDetailList[0].order.detailAddress} ${orderOneDetailList[0].order.extraAddress}</p>
        </div>
    </div>
</div>
