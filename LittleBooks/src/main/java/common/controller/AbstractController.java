package common.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;

public abstract class AbstractController implements InterCommand {
	
	private boolean isRedirect = false;
	
	private String viewPage;
	
	public boolean isRedirect() {
		return isRedirect;
	}
	
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
	
	public String getViewPage() {
		return viewPage;
	}
	
	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}	
	
	// 로그인 여부 확인
	public boolean checkLogin(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser!=null) {
			return true;
		} else {
			return false;
		}
		
	}

}
