package member.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class MemberOneDetail extends AbstractController {

	private MemberDAO mdao;

	public MemberOneDetail() {
		mdao = new MemberDAO_imple();
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 관리자로 로그인 한 경우에만 조회 가능
		HttpSession session = request.getSession();
		
		String referer = request.getHeader("Referer");

		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

		if (loginuser != null && "admin".equals(loginuser.getUserid())) {
			
			
			String method = request.getMethod();
			
			if("POST".equalsIgnoreCase(method)) {
				
				String userid = request.getParameter("userid");

				// 사용자가 직접 주소창에 입력한 경우
				if (referer == null) {
					referer = "http://localhost:9090/MyMVC/index.up";
				}
				
				MemberVO mvo = mdao.selectOneMember(userid);
				
				request.setAttribute("mvo", mvo);
				request.setAttribute("referer", referer);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/member/admin/memberOneDetail.jsp");
				
			}
			
		} 
		else {
			
			// 로그인을 안하거나 또는 관리자(admin)가 아닌 사용자로 로그인 했을 경우
			String message = "관리자만 접근이 가능합니다";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		}

	}

}
