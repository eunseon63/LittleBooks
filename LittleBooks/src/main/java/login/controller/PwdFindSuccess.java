package login.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

// 비밀번호 찾기 처리
public class PwdFindSuccess extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();

		if ("POST".equals(method)) { // POST 방식일 때

			// 아이디, 이메일
			String userid = request.getParameter("userid");
			String email = request.getParameter("email");

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("email", email);

			// 존재하는 사용자인지 확인
			boolean isUserExist = mdao.isUserExist(paraMap);

			// 메일이 정상적으로 보내졌는지 확인
			boolean sendMailSuccess = false;

			if (isUserExist) {
				// 회원으로 존재하는 경우

				// 인증키 랜덤 생성
				Random rnd = new Random();
				String certification_code = "";

				// 인증코드 생성 : 영소문자 5자 + 숫자 7자 
				char randchar = ' ';
				for (int i = 0; i < 5; i++) {
					randchar = (char) (rnd.nextInt('z' - 'a' + 1) + 'a');
					certification_code += randchar;
				} // end of for-------------------------

				int randnum = 0;
				for (int i = 0; i < 7; i++) {
					randnum = rnd.nextInt(9 - 0 + 1) + 0;
					certification_code += randnum;
				} 

				// 인증코드를 비밀번호 찾기를 하고자 하는 사용자의 email 로 전송
				GoogleMail mail = new GoogleMail();

				// 이메일 전송
				try {
					mail.send_certification_code(email, certification_code);
					sendMailSuccess = true;

					// 인증코드 세션에 저장해 인증 번호 확인 시 사
					HttpSession session = request.getSession();
					session.setAttribute("certification_code", certification_code);

				} catch (Exception e) {
					// 메일 전송이 실패한 경우
					e.printStackTrace();
					sendMailSuccess = false;
				}
			}

			// 결과 출력에 사용할 데이터
			request.setAttribute("isUserExist", isUserExist);
			request.setAttribute("sendMailSuccess", sendMailSuccess);
			request.setAttribute("userid", userid);
			request.setAttribute("email", email);
		}

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/pwdFindSuccess.jsp");
	}

}
