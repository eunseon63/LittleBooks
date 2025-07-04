<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String ctxPath = request.getContextPath();
%>

<%
   boolean isLogin = (session.getAttribute("loginuser") != null);
%>

<jsp:include page="/WEB-INF/header1.jsp" />

<!-- SweetAlert CSS/JS -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> 
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>

<!-- jQuery & jQuery UI -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" />
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>

<style>
body {
    font-family: 'Noto Sans KR', 'Segoe UI', sans-serif;
    background-color: #fefefe;
    color: #333;
}

.detail-wrapper {
    max-width: 1100px;
    margin: 100px auto 60px;
    display: flex;
    gap: 60px;
    background-color: #fff;
    padding: 40px;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.05);
}

.left-box {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
}
.left-box img {
    width: 100%;
    max-width: 360px;
    border: 1px solid #eee;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    background: #fff;
}

.right-box {
    flex: 1.4;
    display: flex;
    flex-direction: column;
    gap: 18px;
}

.right-box h2 {
    font-size: 30px;
    font-weight: 700;
    margin-bottom: 6px;
    color: #222;
}

.book-info {
    font-size: 15px;
    line-height: 1.8;
}
.book-info div {
    margin-bottom: 4px;
}
.book-info strong {
    color: #555;
    margin-right: 6px;
}

.select-box label {
    font-weight: 500;
    margin-right: 8px;
}

/* 스피너 입력칸 */
.select-box input {
    width: 70px;
    height: 25px;
    font-size: 14px;
    padding: 4px 8px;
    box-sizing: border-box;
    /* 오른쪽에 버튼 공간 확보 */
    padding-right: 28px;
}

/* 스피너 전체 박스 */
.ui-spinner {
    position: relative;
    display: inline-block;
    vertical-align: middle;
}

/* 스피너 버튼들 공통 */
.ui-spinner-button {
    position: absolute;
    right: 1px;
    width: 26px !important;
    height: 14px !important; /* 반반으로 나눔 */
    padding: 0 !important;
    margin: 0 !important;
    line-height: 14px !important;
    overflow: hidden !important;
    cursor: pointer;
    transition: background-color 0.2s ease;
}

/* 위쪽 버튼 */
.ui-spinner-up {
    top: 1px;
    border-bottom-left-radius: 0;
    border-bottom-right-radius: 0;
}

/* 아래쪽 버튼 */
.ui-spinner-down {
    bottom: 1px;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
}

.ui-spinner-button:hover {
    background-color: #ddb900 !important;
}

.price-section {
    font-size: 18px;
    font-weight: bold;
    color: #222;
    margin-top: 10px;
}

.button-group {
    display: flex;
    gap: 14px;
    margin-top: 20px;
}
.button-group button {
    padding: 12px 26px;
    font-size: 16px;
    font-weight: 600;
    border: none;
    border-radius: 8px;
    background-color: #f4c900;
    color: #222;
    cursor: pointer;
    box-shadow: 0 3px 8px rgba(0,0,0,0.1);
    transition: all 0.2s ease;
}
.button-group button:hover {
    background-color: #ddb900;
}

.section-box {
    max-width: 1000px;
    margin: 40px auto;
    background-color: #fffef3;
    border-radius: 12px;
    padding: 30px 40px;
    border: 1px solid #f4c900;
}

.section-box h3 {
    font-size: 22px;
    font-weight: bold;
    margin-bottom: 16px;
    border-bottom: 1px solid #eee;
    padding-bottom: 10px;
    color: #111;
}

.section-box p {
    font-size: 16px;
    line-height: 1.8;
}

.review-card {
    background-color: #fff;
    padding: 20px;
    border-radius: 10px;
    border: 1px solid #eee;
    margin-top: 12px;
}
.review-card .star {
    color: #f4c900;
    font-size: 18px;
}
.book-spec {
    color: #d00000;
    font-weight: bold;
    font-size: 14px;
    margin-bottom: 4px;
    letter-spacing: 0.5px;
}
.review-wrapper {
    max-width: 900px; /* ✅ 폭 제한 */
    margin: 40px auto 0 auto; /* ✅ 중앙 정렬 */
    border-top: 1px solid #ddd;
    padding-top: 20px;
    font-size: 14px;
}
.review-title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 15px;
}
.review-card {
    background: #f9f9f9;
    border: 1px solid #eee;
    border-radius: 8px;
    padding: 12px 16px;
    margin-bottom: 12px;
}
.review-card .star {
    color: #f2b01e;
    font-size: 14px;
    margin-left: 4px;
}
.review-card .date {
    font-size: 12px;
    color: #888;
    text-align: right;
}
.review-input textarea {
    font-size: 13px;
    height: 100px;
    resize: none;
}
.review-input .btn-submit {
    width: 100%;
    font-size: 14px;
    padding: 8px 0;
}
.commentDel,
.commentUpdate {
    display: inline-block;
    cursor: pointer;
    color: #555;
    padding: 2px 4px;
    border-radius: 3px;
    transition: background-color 0.2s ease;
}

.commentDel:hover,
.commentUpdate:hover {
    background-color: #f4c900;
    color: #000;
    font-weight: 600;
}
/* 리뷰 내용은 굵게 + 크고 진하게 */
.review-content {
    font-size: 15px;
    font-weight: 500;
    color: #222;
    margin-bottom: 6px;
}

/* 작성자 이름과 날짜는 옅게, 작게 */
.review-meta {
    font-size: 12px;
    color: #888;
    margin-bottom: 12px;
}
.rating-stars .star {
    font-size: 22px;
    color: #ddd;
    cursor: pointer;
}
.review-stars .star {
    color: #ccc; /* 미선택 별 - 밝은 회색 */
    font-size: 18px;
}

.review-stars .star.selected {
    color: #f4c900; /* 선택된 별 - 기존보다 약간 더 진한 노란색 */
    text-shadow: 0 0 2px #caa700; /* 약간 광택 효과 */
    font-size: 18px;
}

</style>

<script>
const isLoggedIn = ${not empty sessionScope.loginuser}; // true or false
const loginUserid = "${sessionScope.loginuser.userid != null ? sessionScope.loginuser.userid : ''}";
const bookseq = "<c:out value='${book.bookseq}' default='' />";

$(function(){
	goReviewListView();
	
    // jQuery UI 스피너 세팅 (1~100)
    $("#spinner").spinner({
        min: 1,
        max: 100,
        spin: function(event, ui) {
            if (ui.value > 100) {
                $(this).spinner("value", 100);
                return false;
            }
            if (ui.value < 1) {
                $(this).spinner("value", 1);
                return false;
            }
            updateTotalPrice(ui.value);
        },
        change: function(event, ui) {
            let val = parseInt($(this).val());
            if (isNaN(val) || val < 1) {
                $(this).spinner("value", 1);
                val = 1;
            } else if (val > 100) {
                $(this).spinner("value", 100);
                val = 100;
            }
            updateTotalPrice(val);
        }
    });

    // 초기 총 가격 계산
    updateTotalPrice(parseInt($("#spinner").val()));

    // 수량 직접 입력 시에도 총 가격 업데이트
    $("#spinner").on("input", function() {
        let val = parseInt($(this).val());
        if (isNaN(val) || val < 1) {
            val = 1;
        } else if (val > 100) {
            val = 100;
        }
        updateTotalPrice(val);
    });

let isOrderOK = false;
//로그인한 사용자가 해당 책을 구매한 상태인지 확인하는 용도
//true → 구매한 상태, false → 구매하지 않은 상태
 
 // === 후기 작성 버튼 클릭 이벤트 ===
 $(document).on('click', '#btnCommentOK', function() {
        console.log("후기 작성 버튼 클릭됨");

        if (!isLoggedIn) {
            alert("책 사용 후기를 작성하시려면 먼저 로그인 하셔야 합니다.");
            return;
        }

        if (!isOrderOK) {
            alert("이 책을 구매하셔야만 후기 작성을 하실 수 있습니다.");
            return;
        }

        const review_contents = $('textarea[name="contents"]').val().trim();

        if (review_contents === "") {
            alert("책 사용 후기 내용을 입력하세요!");
            $('textarea[name="contents"]').val("");
            return;
        }

        const queryString = $('form[name="commentFrm"]').serialize();
        console.log(queryString); 
        $.ajax({
            url: "<%= ctxPath %>/shop/reviewRegister.go",
            type: "post",
            data: queryString,
            dataType: "json",
            success: function(json) {
                console.log(JSON.stringify(json));

                if (json.n === 1) {
                    goReviewListView(); // 후기 목록 갱신
                } else if (json.n === -1) {
                    swal("이미 후기를 작성하셨습니다.\n작성하시려면 기존의 후기를 삭제하고 다시 작성해주세요.");
                } else {
                    alert("후기 등록에 실패했습니다.");
                }

                $('textarea[name="contents"]').val("").focus();
            },
            error: function(request, status, error) {
                alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
            }
        });
    });


//로그인한 사용자가 해당 책을 구매했는지 확인
$.ajax({
    url: "<%= ctxPath %>/shop/isOrder.go",
    type: "get",
    data: {
        "fk_bookseq": bookseq,
        "fk_userid": loginUserid
    },
    dataType: "json",
    async: false,
    success: function(json) {
        console.log("~~ 확인용 : " + JSON.stringify(json));
        isOrderOK = json.isOrder;
    },
    error: function(request, status, error) {
        alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
    }
});

// 별 클릭 시 색 채우고 값 설정
$(document).on("click", ".rating-stars .star", function() {
    const selectedRating = $(this).data("value");
    $("#rating").val(selectedRating);

    // 색 초기화 후 선택된 별까지 색 채우기
    $(".rating-stars .star").removeClass("selected");
    $(".rating-stars .star").each(function(index) {
        if (index < selectedRating) {
            $(this).addClass("selected");
        }
    });
    console.log($('#rating').val());
});


});

function goReviewListView() {
    $.ajax({
        url: "<%= ctxPath %>/shop/reviewList.go",
        type: "get",
        data: { "fk_bookseq": bookseq },
        dataType: "json",
        success: function(json) {
            let v_html = "";

            if (json.length > 0) {
                $.each(json, function(index, item) {
                    let writeuserid = item.fk_userid;
                    let rating = item.rating;  // 서버 JSON 필드명에 맞게 조정 필요
                    let reviewComment = item.reviewComment || item.contents;

                    // 디버깅용 로그 (나중에 삭제 가능)
                    console.log("리뷰 내용:", reviewComment, "별점:", rating);

                    v_html += "<div class='review-card' id='review" + index + "'>";

                    v_html += "<div class='review-content'><span class='markColor'>▶</span>&nbsp;" + reviewComment + "</div>";
                    let ratingNum = parseInt(rating);  // 안전하게 숫자 변환
                    v_html += "<div class='review-stars'>";
                    for(let i = 1; i <= 5; i++) {
                        if(i <= rating) {
                            v_html += "<span class='star selected'>★</span>";
                        } else {
                            v_html += "<span class='star'>★</span>";
                        }
                    }
                    v_html += "</div>";


                    v_html += "<div class='review-meta'>" + item.name + " | " + item.writedate + "</div>";

                    if (isLoggedIn && loginUserid === writeuserid) {
                        v_html += "<div class='review-meta'>";
                        v_html += "<span class='commentDel' onclick='delMyReview(" + item.reviewseq + ")'>후기삭제</span> ";
                        v_html += "<span class='commentUpdate' onclick='updateMyReview(" + index + "," + item.reviewseq + ")'>후기수정</span>";
                        v_html += "</div>";
                    }

                    v_html += "</div>";
                });
            } else {
                v_html += "<div>등록된 책 후기가 없습니다.</div>";
            }

            $('div#viewComments').html(v_html);
        },
        error: function(request, status, error) {
            alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
        }
    });
}


function updateTotalPrice(qty) {
    const price = ${book.price};
    const total = qty * price;
    $("#totalPrice").text(total.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW' }));
}

function goCart() {
    const frm = document.cartOrderFrm;
    const cqty = frm.cqty.value;
    const regExp = /^[1-9][0-9]*$/;
    var isLogin = <%= isLogin %>;

    if(!regExp.test(cqty) || cqty < 1 || cqty > 100){
        swal("수량 오류", "수량은 1에서 100 사이의 숫자만 가능합니다.", "warning");
        frm.cqty.focus();
        return false;
    }

    if (!isLogin) {
        swal({
            title: "로그인이 필요합니다!",
            text: "장바구니에 담으려면 먼저 로그인해주세요.",
            type: "warning"
        }, function() {
            location.href = "<%= ctxPath %>/login/login.go";
        });
    } else {
        // 로그인 된 경우에만 진행
        swal({
            title: "장바구니 담기 완료!",
            text: "선택하신 상품이 장바구니에 추가되었습니다.",
            type: "success"
        }, function() {
            frm.method = "post";
            frm.action = "<%= ctxPath %>/shop/cartAdd.go";
            frm.submit();
        });
    }
}


function goPayment() {

	const frm = document.cartOrderFrm;
    
    const qty = frm.qty.value;
    const bookseq = frm.fk_bookseq.value;

    // 유효성 검사
    if (!qty || isNaN(qty) || qty < 1 || qty > 100) {
        swal("수량 오류", "수량은 1~100 사이의 숫자만 가능합니다.", "warning");
        return;
    }

    if (!bookseq) {
        swal("도서 정보 오류", "도서 정보가 없습니다.", "error");
        return;
    }

    // URL 이동
    frm.method = "post";
    frm.action = "<%= ctxPath %>/myshop/payment.go";
    frm.submit();
}

// 특정 제품의 제품후기를 삭제하는 함수 
function delMyReview(reviewseq) {
	console.log("삭제할 reviewseq:", reviewseq); // 이거 꼭 찍어봐
   if(confirm("정말로 제품후기를 삭제하시겠습니까?")) {
      
      $.ajax({
          url:"<%= ctxPath%>/shop/reviewDel.go",
          type:"post",
          data:{"review_seq":reviewseq},
          dataType:"json",
           success:function(json){ 
            // console.log(JSON.stringify(json));
            // {"n":1} 또는 {"n":0}
            
               if(json.n == 1) {
                  alert("제품후기 삭제가 성공되었습니다.");
                  goReviewListView(); // 특정 제품의 제품후기글들을 보여주는 함수 호출하기
               }
               else {
                  alert("제품후기 삭제가 실패했습니다.");
               }
            
           },
           error: function(request, status, error){
              alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
           }
      });      
   }
   
}// end of function delMyReview(review_seq)---------------


//특정 제품의 제품후기를 수정하는 함수 
function updateMyReview(index, reviewseq) {
	console.log("삭제할 reviewseq:", reviewseq); // 이거 꼭 찍어봐
	  const origin_elmt = $('div#review'+index).html(); // 원래의 제품후기 엘리먼트 
 // alert(origin_elmt);
 // <span class="markColor">▶</span>&nbsp;단가라상의하복 추천해요~~
 
 // alert($('div#review'+index).text());
 // ▶ 단가라상의하복 추천해요~~
 
    const review_contents = $('div#review'+index).find('.review-content').text().substring(2).trim();

 // alert(review_contents);
 // 단가라상의하복 추천해요~~  
   
    $("div.commentUpdate").hide(); // "후기수정" 글자 감추기
    
 // "후기수정" 을 위한 엘리먼트 만들기 
	   let v_html = "<textarea id='edit_textarea' style='font-size: 12pt; width: 40%; height: 50px;'>"+review_contents+"</textarea>";
	   v_html += "<div style='display: inline-block; position: relative; top: -20px; left: 10px;'><button type='button' class='btn btn-sm btn-outline-secondary' id='btnReviewUpdate_OK'>수정완료</button></div>"; 
	   v_html += "<div style='display: inline-block; position: relative; top: -20px; left: 20px;'><button type='button' class='btn btn-sm btn-outline-secondary' id='btnReviewUpdate_NO'>수정취소</button></div>";
    
 // 원래의 제품후기 엘리먼트에 위에서 만든 "후기수정" 을 위한 엘리먼트로 교체하기    
	  $("div#review"+index).html(v_html);
 
 // 수정취소 버튼 클릭시 
    $(document).on("click", "button#btnReviewUpdate_NO", function(){
  	  $("div#review"+index).html(origin_elmt); // 원래의 제품후기 엘리먼트로 복원하기
  	  $("div.commentUpdate").show(); // "후기수정" 글자 보여주기
    });
	   
 // 수정완료 버튼 클릭시 
    $(document).on("click", "button#btnReviewUpdate_OK", function(){
  	  
    	// 함수 안에서 rating 값을 다시 가져와야 함
    	const updatedRating = $("#rating").val(); // 또는 별 클릭 시 설정된 요소에서 값을 가져옴
    	
  	  $.ajax({
			   url:"<%= ctxPath%>/shop/reviewUpdate.go",
			   type:"post",
			   data:{"review_seq":reviewseq
				    ,"contents":$('textarea#edit_textarea').val()
				    ,"rating": updatedRating  // 별점 값을 변수로 받아서 넘겨야 함
				    },
			   dataType:"json",
	 		   success:function(json){ 
	 			 // console.log(JSON.stringify(json));
	 			 // {"n":1} 또는 {"n":0}
	 			 
	 			    if(json.n == 1) {
	 			    	goReviewListView(); // 특정 제품의 제품후기글들을 보여주는 함수 호출하기
	 			    }
	 			    else {
	 			    	alert("제품후기 수정이 실패했습니다.");
	 			    	goReviewListView(); // 특정 제품의 제품후기글들을 보여주는 함수 호출하기
	 			    }
	 			 
	 		   },
	 		   error: function(request, status, error){
			       alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		       }
		  });
  	  
    });
	   
}// end of function updateMyReview(index, review_seq)-------

</script>


<div class="detail-wrapper">
    <!-- 이미지 -->
    <div class="detail-wrapper">
    <!-- 이미지 -->
    <div class="left-box">
        <c:choose>
            <c:when test="${not empty book.bimage}">
                <img src="${pageContext.request.contextPath}/images/${book.bimage}" alt="책 이미지" />
            </c:when>
            <c:otherwise>
                <div style="width: 360px; height: 360px; display: flex; align-items: center; justify-content: center; border: 1px solid #eee; border-radius: 12px; background: #f9f9f9;">
                    책 이미지 없음
                </div>
            </c:otherwise>
        </c:choose>
    </div>
   </div>


    <!-- 상세 정보 -->
    <div class="right-box">
        <!-- 스펙 표시 (BEST / NEW 등) -->
        <c:choose>
            <c:when test="${book.fk_snum == 2}">
                <div class="book-spec">BEST(인기)!!</div>
            </c:when>
            <c:when test="${book.fk_snum == 3}">
                <div class="book-spec">NEW(신상)!!</div>
            </c:when>
        </c:choose>

        <h2>${book.bname}</h2>
        <div class="book-info">
            <div><strong>출판사:</strong> ${book.pvo.pname}</div>
            <div><strong>저자:</strong> ${book.author}</div>
            <div><strong>가격:</strong> 
                <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₩" />
            </div>
        </div>

        <form name="cartOrderFrm">
            <div class="select-box">
                <label for="spinner">수량 선택</label>
                <input type="text" id="spinner" name="qty" value="1" autocomplete="off" />
            </div>

            <div class="price-section">
                총 가격: <span id="totalPrice"></span>
            </div>


            <div class="button-group">
                <button type="button" onclick="goPayment()">결제하기</button>
                <button type="button" onclick="goCart()">장바구니</button>
            </div>
            <input type="hidden" name="bookseq" id="fk_bookseq" value="${book.bookseq}" />
        </form>

    

    </div>
</div>

<!-- 책 설명 -->
<div class="section-box">
    <h3>책 설명</h3>
    <p>${book.bcontent}</p>
</div>

<!-- 리뷰 영역 -->
<div class="review-wrapper">
    <div class="review-title">${requestScope.bookVO.bname} 책 사용후기</div>

    <div id="viewComments">
        <%-- 여기에 Ajax로 리뷰 목록이 들어올 예정 --%>
    </div>

    <div class="row review-input mt-3">
    <div class="col-md-10">
        <form name="commentFrm">
            <textarea name="contents" class="form-control" placeholder="후기를 작성해주세요."></textarea>
            <input type="hidden" name="fk_userid" value="${sessionScope.loginuser.userid}" />
            <input type="hidden" name="fk_bookseq" value="${book.bookseq}" />
            <div class="rating-stars mb-2">
			    <span class="star" data-value="1">★</span>
			    <span class="star" data-value="2">★</span>
			    <span class="star" data-value="3">★</span>
			    <span class="star" data-value="4">★</span>
			    <span class="star" data-value="5">★</span>
			</div>
            <input type="hidden" name="rating" id="rating" value="5" /> <%-- 기본 별점 5점 --%>
        </form>
    </div>
    <div class="col-md-2 d-grid">
        <button type="button" class="btn btn-outline-primary btn-submit" id="btnCommentOK">후기 등록</button>
    </div>
	</div>

</div>

<jsp:include page="/WEB-INF/footer.jsp" />
