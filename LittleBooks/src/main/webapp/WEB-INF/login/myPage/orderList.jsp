<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String ctxPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <title>나의 주문 내역</title>

    <!-- CSS -->
    <link rel="stylesheet" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" />

    <!-- jQuery -->
    <script src="<%=ctxPath%>/js/jquery-3.7.1.min.js"></script>

    <style>
        body {
            font-family: 'Segoe UI', 'Helvetica Neue', sans-serif;
            background-color: #f9f9fb;
            color: #333;
        }
        .container {
            max-width: 1000px;
            margin: 40px auto 80px;
            padding: 0 15px;
        }
        h3, .section-title {
            font-weight: 700;
            font-size: 1.7rem;
            margin-bottom: 30px;
            color: #222;
        }
        .order-box {
            background: #fff;
            border: 1px solid #e0e0e0;
            border-radius: 12px;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.06);
            padding: 25px;
            margin-bottom: 30px;
        }
        .order-header {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
            font-size: 1rem;
            font-weight: 600;
            color: #444;
        }
        table.orderTbl {
            width: 100%;
            border-collapse: collapse;
            table-layout: fixed;
        }
        table.orderTbl thead {
            background-color: #fdf2c4;
        }
        table.orderTbl th, table.orderTbl td {
            border: 1px solid #e2e2e2;
            padding: 14px 10px;
            text-align: center;
            vertical-align: middle;
        }
        table.orderTbl th:nth-child(1) { width: 70%; }
        table.orderTbl th:nth-child(2) { width: 30%; }

        td.product-info {
            display: flex;
            align-items: center;
            gap: 15px;
            padding: 10px 12px;
            overflow: hidden;
            text-align: left !important;
        }
        td.product-info img {
            width: 60px;
            height: 80px;
            object-fit: cover;
            border-radius: 6px;
            border: 1px solid #ccc;
        }
        td.product-info div {
            display: flex;
            flex-direction: column;
            justify-content: center;
            line-height: 1.4;
            overflow: hidden;
        }
        td.product-info strong {
            font-size: 1rem;
            color: #222;
        }
        td.product-info small {
            color: #777;
            font-size: 0.85rem;
        }

        .btn-delivery {
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: 600;
            padding: 6px 18px;
            border: none;
            cursor: pointer;
        }
        .btn-waiting { background-color: #9e9e9e; color: #fff; }
        .btn-delivering { background-color: #ffb74d; color: #222; }
        .btn-delivered { background-color: #4caf50; color: #fff; }
    </style>

    <script type="text/javascript">
        $(function () {
            $('.orderTbl').on('click', 'tr.orderInfo', function (e) {
                if ($(e.target).closest('.toggleDeliveryBtn').length > 0) return;

                const ordercode = $(this).data("ordercode");
                if (!ordercode) {
                    alert("주문코드가 없습니다.");
                    return;
                }

                const popupUrl = "<%= ctxPath %>/login/orderDetail.go?ordercode=" + encodeURIComponent(ordercode);
                window.open(popupUrl, "memberDetailPopup", "width=800,height=600,scrollbars=yes,resizable=no");
            });
        });
    </script>
</head>

<body>
<jsp:include page="../../header1.jsp" />

<div class="container mb-5" style="margin-top: 7%;">
<br>
<br>
<br>

<div class="container mt-5 mb-5">
    <div class="row">
        <!-- 좌측 네비게이션 -->
        <div class="col-md-3" style="margin-top: 6.5%;">
            <div class="card shadow-sm p-3 text-center">
                <div class="rounded-circle bg-warning text-white d-inline-block"
                     style="width: 70px; height: 70px; line-height: 70px; font-size: 20px; font-weight: bold;">
                    <c:out value="${loginuser.name}" />
                </div>
                <div class="mt-2 font-weight-bold">
                    <c:out value="${loginuser.name}" />님
                </div>
                <div style="font-size: 0.9rem; color: gray;">
                    POINT: <c:out value="${loginuser.point}" />
                </div>
                <ul class="nav flex-column mt-4">
                    <li class="nav-item mb-2">
                        <button type="button" class="btn btn-outline-secondary btn-block"
                                onclick="location.href='myPage.go'">내 정보 수정</button>
                    </li>
                    <li class="nav-item mb-2">
                        <button type="button" class="btn btn-outline-secondary btn-block"
                                onclick="location.href='orderList.go'">나의 주문 내역</button>
                    </li>
                    <li class="nav-item">
                        <button type="button" class="btn btn-outline-danger btn-block"
                                onclick="location.href='deleteMember.go'">회원 탈퇴</button>
                    </li>
                </ul>
            </div>
        </div>

        <!-- 우측 주문 상세 내역 -->
        <div class="col-md-9">
            <div class="section-title" style="text-align:left;">나의 주문 내역</div>

			<form method="get" action="orderList.go" class="mb-4">
			    <div class="form-inline justify-content-end">
			        <label for="period" class="mr-2 font-weight-bold">조회 기간:</label>
			        <select name="period" id="period" class="form-control mr-3">
			            <option value="1" ${param.period == '1' ? 'selected' : ''}>1개월</option>
			            <option value="3" ${param.period == '3' ? 'selected' : ''}>3개월</option>
			            <option value="6" ${param.period == '6' ? 'selected' : ''}>6개월</option>
			            <option value="12" ${param.period == '12' ? 'selected' : ''}>12개월</option>
			        </select>
			        <button type="submit" class="btn btn-primary" style="background-color: #ffb74d; color:black; border:none;">조회</button>
			    </div>
			</form>
            <c:choose>
                <c:when test="${not empty orderDetailList}">
                    <c:set var="prevOrderCode" value="" />
                    <c:forEach var="orderDetail" items="${orderDetailList}" varStatus="loop">
                        <c:set var="currentOrderCode" value="${orderDetail.fk_ordercode}" />
                        <c:if test="${prevOrderCode ne currentOrderCode}">
                            <div class="order-box">
                                <div class="order-header">
                                    <div><strong>주문번호:</strong> ${orderDetail.fk_ordercode}</div>
                                    <div><strong>주문일자:</strong> <c:out value="${fn:substring(orderDetail.order.orderdate, 0, 10)}" /></div>
                                </div>
                                <table class="orderTbl">
                                    <thead>
                                        <tr>
                                            <th>상품정보</th>
                                            <th>배송상태</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                        </c:if>

                        <tr class="orderInfo" data-ordercode="${orderDetail.fk_ordercode}">
                            <td class="product-info">
                                <img src="<%= ctxPath %>/images/${orderDetail.book.bimage}" alt="${orderDetail.book.bname}" />
                                <div>
                                    <strong>${orderDetail.book.bname}</strong>
                                    <small>저자: ${orderDetail.book.author}</small>
                                </div>
                            </td>
                            <td>
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

                        <c:if test="${loop.last || (fn:length(orderDetailList) > loop.index + 1 && orderDetailList[loop.index + 1].fk_ordercode ne currentOrderCode)}">
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                        <c:set var="prevOrderCode" value="${currentOrderCode}" />
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="text-center text-muted py-5">
                        <h5>주문 내역이 없습니다.</h5>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="../../footer.jsp" />

<!-- Bootstrap JS -->
<script src="<%=ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>
<script src="<%=ctxPath%>/js/member/deleteMember.js"></script>
</body>
</html>