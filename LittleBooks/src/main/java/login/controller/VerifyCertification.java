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

public class VerifyCertification extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		
		if("POST".equalsIgnoreCase(method)) {
			
			String userCertificationCode = request.getParameter("userCertificationCode");
			String userid = request.getParameter("userid");
			
			HttpSession session = request.getSession(); // 세션불러오기
			String certification_code = (String) session.getAttribute("certification_code");
						
			String message = "";
			String loc = "";
			
			if(certification_code.equals(userCertificationCode)) {
				message = "인증성공 되었습니다.";
				loc = request.getContextPath()+"/login/pwdFind.go?userid=" + userid;
			}
			else {
				message = "발급된 인증코드가 아닙니다.\\n인증코드를 다시 발급받으세요!!";
				loc = request.getContextPath()+"/login/pwdFind.go";
			}
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			request.setAttribute("popup_close", true);  // 팝업 닫기용 플래그
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			// !!!! 중요 !!!! //
			// !!!! 세션에 저장된 인증코드 삭제하기 !!!! //
			session.removeAttribute("certification_code");
			
		}// end of if("POST".equalsIgnoreCase(method)) {}-----------------
		
	}

}
