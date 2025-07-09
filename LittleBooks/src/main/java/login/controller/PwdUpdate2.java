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

public class PwdUpdate2 extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		String method = request.getMethod();

		if("POST".equalsIgnoreCase(method)) {
			// "암호변경하기" 버튼을 클릭했을 경우
			String userid = request.getParameter("userid");
			String new_pwd = request.getParameter("new_pwd");

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("new_pwd", new_pwd);
			paraMap.put("userid", userid);
			
			int n = 0;
			
			try {		
				n = mdao.pwdUpdate(paraMap);
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			String message = "";
			String loc = "";
			
			if (n == 1) {
				message = "비밀번호가 성공적으로 변경되었습니다.";
				loc = request.getContextPath()+"/index.go";
			} else {
				message = "비밀번호 변경이 실패되었습니다.";
				loc = request.getContextPath()+"/login/login.go";
				
				session.invalidate();
			}
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}// end of if("POST".equalsIgnoreCase(method))----------- 

	}

}
