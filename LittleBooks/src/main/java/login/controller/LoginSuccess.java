package login.controller;

import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

// 로그인 요청 처리 
public class LoginSuccess extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();

		if (!"POST".equalsIgnoreCase(method)) { // GET 방식일 때

			String message = "비정상적인 경로로 들어왔습니다.";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");

		} else { // POST 방식일 때

			// 아이디, 비밀번호
			String userid = request.getParameter("userid");
			String pwd = request.getParameter("pwd");

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("passwd", pwd);

			// 로그인 시도
			MemberVO loginuser = mdao.login(paraMap);

			if (loginuser != null) {
				System.out.println("로그인 성공!");

				if (loginuser.getIdle() == 1) {
					// 마지막으로 로그인 한 것이 1년 이상 지난 경우
					HttpSession session = request.getSession();
					session.setAttribute("userid", userid);
					session.setAttribute("pwd", pwd);

					// 휴면계정 페이지로 이동
					String message = "로그인을 한지 1년이 지나서 휴면상태로 되었습니다.\\n휴면을 풀어주는 페이지로 이동합니다.";
					String loc = request.getContextPath() + "/login/reactivateAccount.go";

					request.setAttribute("message", message);
					request.setAttribute("loc", loc);

					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");

					return; // 종료
					
				}

				HttpSession session = request.getSession();

				// 로그인된 사용자 정보를 loginuser로 저장
				session.setAttribute("loginuser", loginuser);
				
				if (loginuser.isRequirePwdChange()) {
					// 휴면이 아니면서 비밀번호를 변경한지 3개월 이상된 경우

					// 암호 변경 페이지로 이동
					String message = "비밀번호를 변경하신지 3개월이 지났습니다.\\n암호를 변경해주는 페이지로 이동합니다.";
					String loc = request.getContextPath() + "/login/dormantPage.go";

					request.setAttribute("message", message);
					request.setAttribute("loc", loc);

					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");

					return; // 종료
					
				} else {
					// 휴면이 아니면서 비밀번호를 변경한지 3개월 미만인 경우

					// 페이지 이동을 시킨다.
					super.setRedirect(true);
					super.setViewPage(request.getContextPath() + "/index.go");
				}

			} else {
				// System.out.println("로그인 실패!");

				String message = "로그인 실패";
				String loc = "javascript:history.back()";

				request.setAttribute("message", message);
				request.setAttribute("loc", loc);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}

		}

	}

}
