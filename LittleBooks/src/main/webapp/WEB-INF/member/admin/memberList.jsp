<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="../../header1.jsp" />

<!-- Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>

<style>
    body {
        font-family: 'Helvetica Neue', sans-serif;
    }

    .container {
        padding: 60px 50px;
    }

    h3 {
        color: #333;
        font-weight: bold;
        margin-bottom: 30px;
        text-align: center;
    }

    .form-inline {
        flex-wrap: wrap;
        justify-content: center;
        gap: 1rem;
    }

    .form-inline .form-control {
        border-radius: 4px;
        margin-bottom: 10px;
    }

    .btn-primary {
        background-color: #fbc02d;
        border-color: #fbc02d;
        color: #212529;
        font-weight: 600;
    }

    .btn-primary:hover {
        background-color: #f9a825;
        border-color: #f9a825;
    }

    .table thead {
        background-color: #fff9db;
    }

    .table th, .table td {
        vertical-align: middle;
    }
    
    #memberTbl tr.memberInfo:hover {
	  background-color: #fffde7; /* 연노랑 */
	  cursor: pointer;
	  transition: background-color 0.2s ease-in-out;
	}

    .pagination .page-link {
        color: #fbc02d;
        border: 1px solid #fbc02d;
    }

    .pagination .page-item.active .page-link {
        background-color: #fbc02d;
        color: white;
        border-color: #fbc02d;
    }

    .pagination .page-link:hover {
        background-color: #fff3cd;
        border-color: #fbc02d;
    }
</style>

<script type="text/javascript">
    $(function () {
        // 검색조건 및 페이지당 인원수 유지
        $('select[name="sizePerPage"]').val("${requestScope.sizePerPage}");

        if ("${requestScope.searchType}" != "" && "${requestScope.searchWord}" != "") {
            $('select[name="searchType"]').val("${requestScope.searchType}");
            $('input[name="searchWord"]').val("${requestScope.searchWord}");
        }

        // 페이지당 보기 변경 시 자동 제출
        $('select[name="sizePerPage"]').on('change', function () {
            const frm = document.member_search_frm;
            frm.submit();
        });

        // 엔터 검색
        $('input:text[name="searchWord"]').keydown(function (e) {
            if (e.keyCode == 13) {
                goSearch();
            }
        });
        
        // 회원 상세 정보 페이지 열기
		$('table#memberTbl tr.memberInfo').click(e => {
			// alert($(e.target).parent().html());
			
			const userid = $(e.target).parent().find("td.userid").text();
			// alert(userid);
			
			const frm = document.memberOneDetail_frm;
			frm.userid.value = userid;
			
			frm.action = "${pageContext.request.contextPath}/member/memberOneDetail.go";
			frm.method = "post";
			frm.submit();
			
		});
    });

    function goSearch() {
        const searchType = $('select[name="searchType"]').val();
        if (searchType == "") {
            alert("검색 대상을 선택하세요!");
            return;
        }
        document.member_search_frm.submit();
    }
</script>

</head>
<body>

<div class="container mt-5">
    <h3>회원 목록</h3>

    <!-- 검색 및 페이지당 보기 -->
	<form name="member_search_frm" class="mb-4">
  <div class="form-row justify-content-center align-items-center">
    <div class="col-auto">
      <select class="form-control" name="searchType">
        <option value="">검색대상</option>
        <option value="userid">아이디</option>
        <option value="name">이름</option>
        <option value="email">이메일</option>
      </select>
    </div>
    <div class="col-auto">
      <input type="text" class="form-control" name="searchWord" placeholder="검색어 입력">
    </div>
    <div class="col-auto">
      <button type="button" class="btn btn-primary" onclick="goSearch()">검색</button>
    </div>
    <div class="col-auto">
   
      <select class="form-control" name="sizePerPage">
        <option value="10" selected>10명</option>
        <option value="5">5명</option>
      </select>
    </div>
  </div>
</form>

    <!-- 회원 목록 테이블 -->
    <table class="table table-bordered text-center" id="memberTbl">
        <thead>
        <tr>
            <th>번호</th>
            <th>아이디</th>
            <th>이름</th>
            <th>이메일</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${not empty requestScope.memberList}">
            <c:forEach var="membervo" items="${requestScope.memberList}" varStatus="status">
                <fmt:parseNumber var="currentShowPageNo" value="${requestScope.currentShowPageNo}" />
                <fmt:parseNumber var="sizePerPage" value="${requestScope.sizePerPage}" />
                <tr class="memberInfo">
                    <td>${(requestScope.totalMemberCount) - (currentShowPageNo - 1) * sizePerPage - (status.index)}</td>
                    <td class="userid">${membervo.userid}</td>
                    <td>${membervo.name}</td>
                    <td>${membervo.email}</td>
                </tr>
            </c:forEach>
        </c:if>
        <c:if test="${empty requestScope.memberList}">
            <tr>
                <td colspan="4">데이터가 존재하지 않습니다.</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <!-- 페이지바 -->
    <div id="pageBar" class="text-center mt-4">
        <nav>
            <ul class="pagination justify-content-center">
                ${requestScope.pageBar}
            </ul>
        </nav>
    </div>
</div>

<form name="memberOneDetail_frm">
	<input type="hidden" name="userid" />
</form>

<jsp:include page="../../footer.jsp" />

</body>
</html>
