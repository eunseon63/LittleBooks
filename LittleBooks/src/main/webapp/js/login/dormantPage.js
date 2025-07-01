$(function () {

	$("span.error").hide();
	$("input#new_pwd").focus();

    // 새 암호 유효성 검사
    $("input#new_pwd").blur(function (e) {
		
		const regExp_pwd = new RegExp(/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g);
		const bool = regExp_pwd.test($(e.target).val());
		
        if (!bool) {
			$("div#form-group :input").prop("disabled", true);  
			$(e.target).prop("disabled", false); 
			
			$(e.target).parent().find("span.error").show();
			$(e.target).val("").focus();
        } else {
			$("div#form-group :input").prop("disabled", false);
            $(e.target).parent().find("span.error").hide();
        }
    });

    // 새 암호 확인 검사
    $("input#pwd2").blur(function (e) {
		
		if( $("input#new_pwd").val() != $(e.target).val() ) {
			// 암호와 암호확인값이 틀린 경우 
			
			$("div#form-group :input").prop("disabled", true);  
			$("input#new_pwd").prop("disabled", false);
			$(e.target).prop("disabled", false);
			
		//	$(e.target).next().show();
		//  또는
		    $(e.target).parent().find("span.error").show();
			$("input#new_pwd").focus();
		}
		else {
			// 암호와 암호확인값이 같은 경우
			$("div#form-group :input").prop("disabled", false);
			
			//	$(e.target).next().hide();
		    //  또는
		    $(e.target).parent().find("span.error").hide();

		}
    });
}); // end of $(function () {}-----------------------------

function goEdit() {
	
	let b_requiredInfo = false;
	
	const requiredInfo_list = document.querySelectorAll("input.requiredInfo"); 
	for(let i=0; i<requiredInfo_list.length; i++){
		const val = requiredInfo_list[i].value.trim();
		if(val == "") {
			alert("입력사항을 모두 입력하셔야 합니다.");
		    b_requiredInfo = true;
		    break; 
		}
	}// end of for-----------------------------
	
	if(b_requiredInfo) {
		return; // goRegister() 함수를 종료한다.
	}
	
	let isNewPwd = true;

	$.ajax({
		url:"duplicatePwdCheck.go",
		data:{"new_pwd":$('input:password[name="new_pwd"]').val()
			 ,"userid":$('input:hidden[name="userid"]').val()
		     },
		type:"post",
		async:false,  // !!!! 반드시 동기방식 이어야 한다. !!!!!
		dataType:"json",
		success:function(json){
			// console.log(json);
			// {"isExists" : true}        또는    {"isExists" : false} 
			// 새암호가 기존암호와 동일한 경우          새암호가 기존암호와 다른 경우
			
			if(json.isExists) {
				// 새암호가 기존암호와 동일한 경우
				$('span#duplicate_pwd').html("현재 사용중인 비밀번호와 동일하므로 변경이 불가합니다."); 
				isNewPwd = false;
			} else {
				
			}
		},
		error: function(request, status, error){
			   alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	 	}
	}); // end of $.ajax({})--------------------	
	
	if ($("input#pwd2").val() != $("input#new_pwd").val()) {
		isNewPwd = false;
	}
	
	if (isNewPwd) {
	    const frm = document.pwdUpdateEndFrm;
	    frm.action = "/LittleBooks/login/pwdUpdate2.go";
	    frm.method = "post";
	    frm.submit();
	}
} // end of goEdit()-------------------------------
