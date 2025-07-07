package login.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 로그아웃 처리
public class Logout extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 로그아웃 처리하기
		HttpSession session = request.getSession(); // 세션불러오기 
		session.invalidate(); // 세션 종료 (로그아웃)
		
		// 홈페이지로 이동
		super.setRedirect(true);
		super.setViewPage(request.getContextPath()+"/index.go");
	}

}
