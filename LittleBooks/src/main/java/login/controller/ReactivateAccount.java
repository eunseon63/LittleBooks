package login.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

// 휴면 계정 확인 처리
public class ReactivateAccount extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();

		if (!"POST".equalsIgnoreCase(method)) { // GET 방식일 때

			HttpSession session = request.getSession();
			String userid = (String) session.getAttribute("userid");

			// 사용자 이름, 마지막 로그인 날짜 가져오기
			String name = mdao.selectName(userid);
			// System.out.println(name);
			String loginDate = mdao.selectLoginDate(userid);

			// 날짜 포맷 처리
			loginDate = loginDate.substring(0, 11);
			System.out.println(loginDate);

			request.setAttribute("loginDate", loginDate);
			request.setAttribute("name", name);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/login/reactivateAccount.jsp");

		} else {

			String message = "로그인 실패";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
	}

}
