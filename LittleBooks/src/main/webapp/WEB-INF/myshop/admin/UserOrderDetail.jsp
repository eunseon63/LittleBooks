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
    <h3>주문자 정보 상세보기</h3>

    <table class="info-table">
        <tr>
            <th>주문번호</th>
            <td>${ordercode}</td>
        </tr>
        <tr>
            <th>아이디</th>
            <td>${member.userid}</td>
        </tr>
        <tr>
            <th>이름</th>
            <td>${member.name}</td>
        </tr>
        <tr>
            <th>이메일</th>
            <td>${member.email}</td>
        </tr>
        <tr>
            <th>전화번호</th>
            <td>${member.mobile}</td>
        </tr>
        <tr>
            <th>우편번호</th>
            <td>${member.postcode}</td>
        </tr>
        <tr>
            <th>주소</th>
            <td>${member.address} ${member.detailaddress} ${member.extraaddress}</td>
        </tr>
    </table>
</div>

