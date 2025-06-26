$(function () {
	$('button#btnSubmit').click((e)=>{
		e.preventDefault();
		goLogin();  // 로그인 시도한다.	
	});
	
	$('input#loginPwd').bind("keyup", (e)=>{
		if(e.keyCode == 13) { // 암호입력란에 엔터를 했을 경우 
			goLogin();  
		}
	});
});

// Function Declaration
function goLogin() {
	if( $('input#loginUserid').val().trim() == "" ) {
		alert("아이디를 입력하세요!!");
		$('input#loginUserid').val("").focus();
		return;  // goLogin() 함수 종료
	}
	
	if( $('input#loginPwd').val().trim() == "" ) {
		alert("암호를 입력하세요!!");
		$('input#loginPwd').val("").focus();
		return;  // goLogin() 함수 종료
	}
	
	const frm = document.loginFrm;
	frm.submit();
}// end of function goLogin() {}--------------------------
