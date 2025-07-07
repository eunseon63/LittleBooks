package login.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

// 비밀번호 변경 처리
public class PwdUpdate extends AbstractController {
	
	private MemberDAO mdao = new MemberDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String userid = request.getParameter("userid");
		String method = request.getMethod();
		
		if("POST".equalsIgnoreCase(method)) { // POST 방식일 때
			
			// 새로운 비밀번호
			String new_pwd = request.getParameter("pwd");
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("new_pwd", new_pwd);
			paraMap.put("userid", userid);
			
			int n = 0;
			
			try {	
				// 비밀번호 변경 시도
				n = mdao.pwdUpdate(paraMap);
				// n = 1 : 비밀번호 변경 성공
				// n = 0 : 비밀번호 변경 실패
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			String message = "";
			String loc = "";
			
			if (n == 1) {
				message = "비밀번호가 성공적으로 변경되었습니다.";
				loc = request.getContextPath()+"/login/login.go";
			} else {
				message = "비밀번호 변경이 실패되었습니다.";
				loc = request.getContextPath()+"/login/login.go";
			}
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			request.setAttribute("popup_close", true);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}

	}

}
