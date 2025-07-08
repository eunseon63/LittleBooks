package login.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

// 인증번호 확인 처리
public class VerifyCertification extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();

		if ("POST".equalsIgnoreCase(method)) { // POST 방식일 때

			String userCertificationCode = request.getParameter("userCertificationCode"); // 사용자가 입력한 인증코드
			String userid = request.getParameter("userid");

			HttpSession session = request.getSession(); // 세션불러오기
			String certification_code = (String) session.getAttribute("certification_code"); // 이메일로 보내진 인증코드

			String message = "";
			String loc = "";

			// 사용자가 입력한 인증 코드가 일치할 경우
			if (certification_code.equals(userCertificationCode)) {
				message = "인증 성공 되었습니다.";
				loc = request.getContextPath() + "/login/pwdFind.go?userid=" + userid;
			} else {
				message = "발급된 인증코드가 아닙니다.\\n인증코드를 다시 발급받으세요.";
				loc = request.getContextPath() + "/login/pwdFind.go";
			}

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			request.setAttribute("popup_close", true);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");

			// 세션에 저장된 인증코드 삭제
			session.removeAttribute("certification_code");

		}

	}

}
