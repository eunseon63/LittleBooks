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

public class LoginSuccess extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); // "GET" 또는 "POST"
		
		if(!"POST".equalsIgnoreCase(method)) {
			// POST 방식으로 넘어온 것이 아니라면
			
			String message = "비정상적인 경로로 들어왔습니다.";
			String loc = "javascript:history.back()";
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		} else {
			String userid = request.getParameter("userid");
			String pwd = request.getParameter("pwd");
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("passwd", pwd);
			
			MemberVO loginuser = mdao.login(paraMap);
			
			if(loginuser != null) {
				System.out.println("~~~ 확인용 로그인 성공 ^___^");
			
				if(loginuser.getIdle() == 1) {
					// 마지막으로 로그인 한 것이 1년 이상 지난 경우 
					
					String message = "로그인을 한지 1년이 지나서 휴면상태로 되었습니다.\\n휴면을 풀어주는 페이지로 이동합니다.";
					String loc = request.getContextPath()+"/index.go";
					// 원래는 위와같이 index.go 이 아니라 휴면인 계정을 풀어주는 페이지로 URL을 잡아주어야 한다.!!
					
					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
					
					return; // 메소드 종료 
				}
								
				HttpSession session = request.getSession();
				// WAS 메모리에 생성되어져 있는 sesssion 을 불러오는 것이다.
				
				session.setAttribute("loginuser", loginuser);
				// session(세션)에 로그인 되어진 사용자 정보인 loginuser 를 키이름을 "loginuser" 으로 저장시켜두는 것이다. 
				
				if(loginuser.isRequirePwdChange()) {
					// 휴면이 아니면서 비밀번호를 변경한지 3개월 이상된 경우 
					
					String message = "비밀번호를 변경하신지 3개월이 지났습니다.\\n암호를 변경해주는 페이지로 이동합니다.";
					String loc = request.getContextPath()+"/login/dormantPage.go";
					// 원래는 위와같이 index.up 이 아니라 암호를 변경하는 페이지로 URL을 잡아주어야 한다.!!
					
					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
					
					return; // 메소드 종료 
				} else {
					// 휴면이 아니면서 비밀번호를 변경한지 3개월 미만인 경우
					
					// 페이지 이동을 시킨다.
					super.setRedirect(true);
					super.setViewPage(request.getContextPath()+"/index.go");
				}
				
			} else {
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

}
