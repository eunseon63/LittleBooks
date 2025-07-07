<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String ctxPath = request.getContextPath();
%>

<%
   boolean isLogin = (session.getAttribute("loginuser") != null);
%>
<!-- Custom CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/book/bookDetail.css" />

<jsp:include page="/WEB-INF/header1.jsp" />

<!-- SweetAlert CSS/JS -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> 
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>

<!-- jQuery & jQuery UI -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" />
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>

<script>
const isLoggedIn = ${not empty sessionScope.loginuser ? 'true' : 'false'};
const loginUserid = "<c:out value='${sessionScope.loginuser.userid}' default='' />";
const bookseq = "<c:out value='${book.bookseq}' default='' />";

$(function(){
	goReviewListView();

    // jQuery UI spinner 세팅 (1~100)
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
        change: function(event) {  // ui 인자 제거, 없으니까
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

    // 후기 작성 버튼 클릭 이벤트
    $(document).on('click', '#btnCommentOK', function() {
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

        $.ajax({
            url: "<%= ctxPath %>/shop/reviewRegister.go",
            type: "post",
            data: queryString,
            dataType: "json",
            success: function(json) {
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

    // 로그인한 사용자가 해당 책을 구매했는지 확인
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

        $(".rating-stars .star").removeClass("selected");
        $(".rating-stars .star").each(function(index) {
            if (index < selectedRating) {
                $(this).addClass("selected");
            }
        });
    });
});  // <-- jQuery ready 함수 닫힘

// 특정 책의 리뷰글들을 보여주는 함수 
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
                    v_html += "<div class='rating-stars' style='margin-top: 10px;'>";
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
    
    $("input[name='cqty']").val(qty); // 수량 반영
}

function goCart() {
    const frm = document.cartOrderFrm;
    const cqty = frm.cqty.value;
    const regExp = /^[1-9][0-9]*$/;

    if (!regExp.test(cqty) || cqty < 1 || cqty > 100) {
        swal("수량 오류", "수량은 1에서 100 사이의 숫자만 가능합니다.", "warning");
        frm.cqty.focus();
        return false;
    }

    if (!isLoggedIn) {
        swal({
            title: "로그인이 필요합니다!",
            text: "장바구니에 담으려면 먼저 로그인해주세요.",
            type: "warning"
        }, function() {
            location.href = "<%= ctxPath %>/login/login.go";
        });
    } else {
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

function goOrder() {
    const frm = document.cartOrderFrm;
    const qty = parseInt(frm.cqty.value);
    const bookseqVal = frm.fk_bookseq.value;

    if (!qty || isNaN(qty) || qty < 1 || qty > 100) {
        swal("수량 오류", "수량은 1~100 사이의 숫자만 가능합니다.", "warning");
        return;
    }

    if (!bookseqVal) {
        swal("도서 정보 오류", "도서 정보가 없습니다.", "error");
        return;
    }

    if (!isLoggedIn) {
        swal({
            title: "로그인이 필요합니다!",
            text: "장바구니에 담으려면 먼저 로그인해주세요.",
            type: "warning"
        }, function() {
            location.href = "<%= ctxPath %>/login/login.go";
        });
    } else {
        frm.method = "post";
        frm.action = "<%= ctxPath %>/shop/payment.go";
        frm.submit();
    }
}

// 특정 제품의 제품후기를 삭제하는 함수 
function delMyReview(reviewseq) {
    if(confirm("정말로 제품후기를 삭제하시겠습니까?")) {
        $.ajax({
            url:"<%= ctxPath%>/shop/reviewDel.go",
            type:"post",
            data:{"review_seq":reviewseq},
            dataType:"json",
            success:function(json){ 
                if(json.n == 1) {
                    alert("제품후기 삭제가 성공되었습니다.");
                    goReviewListView();
                } else {
                    alert("제품후기 삭제가 실패했습니다.");
                }
            },
            error: function(request, status, error){
                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
        });      
    }
}

//특정 제품의 제품후기를 수정하는 함수
function updateMyReview(index, reviewseq) {
    const origin_elmt = $('div#review' + index).html();
    const review_contents = $('div#review' + index).find('.review-content').text().substring(2).trim();
    let currentRating = 5; // 실제로는 데이터에서 받아와야 함

    $("div.commentUpdate").hide();

    let v_html = "<textarea id='edit_textarea' style='font-size:12pt; width: 100%; height: 80px; margin-bottom: 12px;'>" + review_contents + "</textarea>";

    v_html += "<div class='rating-stars' style='margin-top: 10px; margin-bottom: 15px;'>";
    for (let i = 1; i <= 5; i++) {
        v_html += "<span class='star " + (i <= currentRating ? "selected" : "") + "' data-value='" + i + "' style='margin-right: 6px; font-size: 22px;'>★</span>";
    }
    v_html += "</div>";

    v_html += "<div style='display:flex; gap: 12px;'>";
    v_html += "<button type='button' id='btnReviewUpdate_OK' style='padding: 6px 14px; font-size: 14px;'>수정완료</button>";
    v_html += "<button type='button' id='btnReviewUpdate_NO' style='padding: 6px 14px; font-size: 14px;'>수정취소</button>";
    v_html += "</div>";

    v_html += "<input type='hidden' id='rating' name='rating' value='" + currentRating + "' />";

    $("div#review" + index).html(v_html);

    // 별 클릭 이벤트
    $("div#review" + index + " .rating-stars .star").on("click", function() {
        const selectedRating = $(this).data("value");
        $("#rating").val(selectedRating);

        $(this).siblings().removeClass("selected");
        $(this).addClass("selected");
        $(this).prevAll().addClass("selected");
    });

    // 기존 이벤트 제거 후 새로 등록 (중복 방지)
    $(document).off("click", "#btnReviewUpdate_NO").on("click", "#btnReviewUpdate_NO", function() {
        $("div#review" + index).html(origin_elmt);
        $("div.commentUpdate").show();
    });

    $(document).off("click", "#btnReviewUpdate_OK").on("click", "#btnReviewUpdate_OK", function() {
        const updatedRating = $("#rating").val();

        $.ajax({
            url: "<%= ctxPath %>/shop/reviewUpdate.go",
            type: "post",
            data: {
                "review_seq": reviewseq,
                "contents": $('#edit_textarea').val(),
                "rating": updatedRating
            },
            dataType: "json",
            success: function(json) {
                if (json.n == 1) {
                    goReviewListView();
                } else {
                    alert("제품후기 수정이 실패했습니다.");
                    goReviewListView();
                }
            },
            error: function(request, status, error) {
                alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
            }
        });
    });
} // end of function updateMyReview(index, review_seq)-------

</script>


<div class="detail-wrapper">
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

    <div class="right-box">
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
		        <input type="text" id="spinner" value="1" autocomplete="off" />
		    </div>
            <div class="price-section">
                총 가격: <span id="totalPrice"></span>
            </div>
		    <div class="button-group">
		        <button type="button" onclick="goOrder()">결제하기</button>
		        <button type="button" onclick="goCart()">장바구니</button>
		    </div>
		    <input type="hidden" name="fk_bookseq" id="fk_bookseq" value="${book.bookseq}" />
			<input type="hidden" name="cqty" id="cqty" value="1" />
		    <input type="hidden" name="str_bookseq_join" value="${book.bookseq}" />
		    <input type="hidden" name="str_oqty_join" id="oqtyHidden" value="1" />
		    <input type="hidden" name="str_price_join" value="${book.price}" />
		    <input type="hidden" name="sum_totalPrice" id="sumTotalHidden" value="${book.price}" />
		    <input type="hidden" name="str_cartseq_join" value="0" />
		</form>

        
        
    </div>
</div>

<!-- 📚 책 설명 영역 -->
<div class="section-box">
    <h3>책 설명</h3>
    <p>${book.bcontent}</p>
</div>

<!-- 리뷰 영역 -->
<div class="review-wrapper">
    <div class="review-title">${requestScope.book.bname} 책 사용후기</div>

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