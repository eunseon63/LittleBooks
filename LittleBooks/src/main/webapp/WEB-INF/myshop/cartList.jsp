<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctxPath = request.getContextPath();
%>
<jsp:include page="../header1.jsp" />

<div class="container-fluid mt-5">
    <h4 class="text-center my-4">${sessionScope.loginuser.name} [${sessionScope.loginuser.userid}]님 장바구니</h4>

    <c:if test="${empty requestScope.cartList}">
        <div class="text-center text-danger font-weight-bold">
            장바구니에 담긴 상품이 없습니다.
        </div>
    </c:if>

    <c:if test="${not empty requestScope.cartList}">
        <form method="post" action="<%= ctxPath %>/shop/orderAdd.up">
            <table class="table table-bordered table-hover text-center" style="width:90%; margin: 0 auto;">
                <thead class="thead-light">
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
                    <c:forEach var="cartvo" items="${requestScope.cartList}" varStatus="status">
                        <tr>
                            <td>
                                <input type="checkbox" name="bookseq" id="bookseq${status.index}" value="${cartvo.fk_bookseq}" />
                            </td>
                            <td>
                                <img src="<%= ctxPath %>/images/${cartvo.book.bimage}" width="80" height="100" class="img-thumbnail" />
                            </td>
                            <td>${cartvo.book.bname}</td>
                            <td>
                                <input type="number" name="oqty" value="${cartvo.oqty}" class="form-control" style="width: 70px; display: inline-block;" /> 개
                                <input type="hidden" name="bqty" value="${cartvo.book.bqty}" />
                                <input type="hidden" name="cartno" value="${cartvo.cartno}" />
                            </td>
                            <td>
                                <fmt:formatNumber value="${cartvo.book.price}" pattern="###,###" /> 원
                            </td>
                            <td>
                                <fmt:formatNumber value="${cartvo.book.price * cartvo.oqty}" pattern="###,###" /> 원
                            </td>
                            <td>
                                <button type="button" class="btn btn-sm btn-danger" onclick="goDel('${cartvo.cartno}')">삭제</button>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="5" class="text-right font-weight-bold">총 합계:</td>
                        <td colspan="2" class="text-left text-danger font-weight-bold">
                            <fmt:formatNumber value="${requestScope.sumMap.SUMTOTALPRICE}" pattern="###,###" /> 원
                        </td>
                    </tr>
                </tbody>
            </table>

            <div class="text-center mt-4">
                <button type="button" class="btn btn-dark mr-2" onclick="goOrder()">주문하기</button>
                <a href="<%= ctxPath %>/shop/mallHomeMore.up" class="btn btn-secondary">계속 쇼핑</a>
            </div>
        </form>
    </c:if>

    <div id="order_error_msg" class="text-center text-danger font-weight-bold mt-3"></div>

    <!-- 로딩 스피너 -->
    <div class="loader" style="display:none; position:fixed; top:40%; left:50%; transform:translate(-50%,-50%);"></div>
</div>
<jsp:include page="../footer.jsp" />
