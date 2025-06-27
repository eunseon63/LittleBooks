<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="../../header1.jsp" />

<style>
  table.table {
    text-align: center;
  }
  table.table td {
    vertical-align: middle;
    text-align: center;
    font-size: 1rem;
    padding: 12px;
  }
</style>

<div class="container" style="padding-top: 80px;">

	<c:if test="${empty requestScope.mvo}">
		<div class="text-center h4 my-5">존재하지 않는 회원입니다.</div>
	</c:if>

	<c:if test="${not empty requestScope.mvo}">
		<p class="text-center h3 my-5">${requestScope.mvo.name} 님의 회원 상세정보</p>

		<table class="table table-bordered" style="width: 60%; margin: 0 auto;">
			<tr>
				<td>아이디</td>
				<td>${requestScope.mvo.userid}</td>
			</tr>
			<tr>
				<td>회원명</td>
				<td>${requestScope.mvo.name}</td>
			</tr>
			<tr>
				<td>이메일</td>
				<td>${requestScope.mvo.email}</td>
			</tr>
			<tr>
				<td>휴대폰</td>
				<c:set var="mobile" value="${requestScope.mvo.mobile}" />
				<td>${fn:substring(mobile, 0, 3)}-${fn:substring(mobile, 3, 7)}-${fn:substring(mobile, 7, 11)}</td>
			</tr>
			<tr>
				<td>우편번호</td>
				<td>${requestScope.mvo.postcode}</td>
			</tr>
			<tr>
				<td>주소</td>
				<td>
					${requestScope.mvo.address}&nbsp;
					${requestScope.mvo.detailaddress}&nbsp;
					${requestScope.mvo.extraaddress}
				</td>
			</tr>
			<tr>
				<td>생년월일</td>
				<td>${fn:substring(requestScope.mvo.birthday, 0, 10)}</td>
			</tr>
			<tr>
				<td>만나이</td>
				<td>${requestScope.mvo.age}</td>
			</tr>
			<tr>
				<td>포인트</td>
				<td><fmt:formatNumber value="${requestScope.mvo.point}" pattern="###,###" /> POINT</td>
			</tr>
			<tr>
				<td>가입일자</td>
				<td>${requestScope.mvo.registerday}</td>
			</tr>
		</table>

	</c:if>
	
	<div class="text-center mb-5">
		<button type="button" class="btn mt-5 mx-5" style="background-color: #ffc107;" onclick="javascript:location.href='${requestScope.referer}'">회원목록</button>
	</div>

</div>

<jsp:include page="../../footer.jsp" />
