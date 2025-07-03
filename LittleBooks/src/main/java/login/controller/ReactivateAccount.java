package login.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class ReactivateAccount extends AbstractController {
	
	private MemberDAO mdao = new MemberDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); // "GET" 또는 "POST"
		
		
		if(!"POST".equalsIgnoreCase(method)) {
			
			HttpSession session = request.getSession();
			String userid = (String) session.getAttribute("userid");
			
			String name = mdao.selectName(userid);
			System.out.println(name);
			String loginDate = mdao.selectLoginDate(userid);
			
			loginDate = loginDate.substring(0,11);
			System.out.println(loginDate);
			
			request.setAttribute("loginDate", loginDate);
			request.setAttribute("name", name);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/login/reactivateAccount.jsp");
			
		}  else {
			 // System.out.println("~~~ 확인용 로그인 실패 ㅜㅜ");
			
			String message = "로그인 실패";
			String loc = "javascript:history.back()";
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
	}

}
