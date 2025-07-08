<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    body {
        font-family: 'Helvetica Neue', sans-serif;
    }

    .info-wrapper {
        max-width: 720px;
        margin: 60px auto;
        padding: 0 20px;
    }

    .info-wrapper h3 {
        text-align: center;
        margin-bottom: 40px;
        font-size: 1.75rem;
        font-weight: 600;
        color: #333;
    }

    .info-table {
        width: 100%;
        border-collapse: collapse;
    }

    .info-table th,
    .info-table td {
        padding: 12px 16px;
        font-size: 0.95rem;
        border-bottom: 1px solid #eaeaea;
    }

    .info-table th {
        text-align: left;
        color: #555;
        width: 150px;
        background-color: #fff9db;
    }

    .info-table td {
        color: #333;
        background-color: #fff;
    }

    @media (max-width: 576px) {
        .info-table th,
        .info-table td {
            display: block;
            width: 100%;
            padding: 8px 0;
        }

        .info-table th {
            background-color: transparent;
            font-weight: bold;
            border: none;
        }

        .info-table td {
            border-bottom: 1px solid #eee;
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
                    <td>${orderDetail.book.bname}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
