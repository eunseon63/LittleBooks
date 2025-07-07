package login.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;

// 마이페이지 요청 
public class MyPage extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		String address = loginuser.getAddress();
		String detailaddress = loginuser.getDetailaddress();
		String extraaddress = loginuser.getExtraaddress();
		
		request.setAttribute("address", address);
		request.setAttribute("detailaddress", detailaddress);
		request.setAttribute("extraaddress", extraaddress);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/myPage/myPage.jsp");
	}

}
