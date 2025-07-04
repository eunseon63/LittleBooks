<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
    String ctxPath = request.getContextPath();
%>

<jsp:include page="../../header1.jsp" />

<div class="container" style="margin-top: 7%;">
    
    <!-- 상단 제목 영역 -->
    <div style="margin: 5% auto;">
        <h3 class="text-center">주문내역 전체 목록</h3>
    </div>

    <!-- 주문 목록 테이블 -->
    <div class="table-responsive shadow-sm rounded">
        <table class="table table-hover table-striped align-middle text-center">
            <thead class="table-warning">
                <tr>
                    <th>주문코드</th>
                    <th>주문일자</th>
                    <th>제품정보</th>
                    <th>수량</th>
                    <th>총액</th>
                    <th>포인트</th>
                    <th>배송상태</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${not empty requestScope.orderDetailList}">
                    <c:forEach var="orderDetail" items="${requestScope.orderDetailList}">
                        <tr>
                            <td>${orderDetail.fk_ordercode}</td>
                            <td>${orderDetail.deliverdate}</td>
                            <td class="text-start">
                                <div class="d-flex align-items-center gap-3">
                                    <img src="<%= ctxPath%>/images/${orderDetail.book.bimage}"
                                         alt="${orderDetail.book.bname}"
                                         class="rounded border"
                                         style="width: 70px; height: auto;" />
                                    <div>
                                        <div class="fw-semibold">${orderDetail.book.bname}</div>
                                        <div class="text-muted small">저자: ${orderDetail.book.author}</div>
                                        <div class="text-muted small">
                                            가격: <fmt:formatNumber value="${orderDetail.book.price}" type="currency" currencySymbol="₩" />
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <td>${orderDetail.oqty}</td>
                            <td>
                                <fmt:formatNumber value="${orderDetail.odrprice * orderDetail.oqty}" type="currency" currencySymbol="₩" />
                            </td>
                            <td>
                                <fmt:formatNumber value="${orderDetail.odrprice * orderDetail.oqty * 0.1}" type="number" />p
                            </td>
                            <td>
                                <span class="badge px-3 py-2 ${orderDetail.deliverstatus == '1' ? 'bg-warning text-dark' : 'bg-success'}">
                                    ${orderDetail.deliverstatus == '1' ? '배송중' : '배송완료'}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                <c:if test="${empty requestScope.orderDetailList}">
                    <tr>
                        <td colspan="7" class="text-center py-4 text-muted">주문내역이 존재하지 않습니다.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../../footer.jsp" />
