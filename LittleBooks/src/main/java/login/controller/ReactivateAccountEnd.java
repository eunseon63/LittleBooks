package login.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

// 휴면 계정 해제 처리
public class ReactivateAccountEnd extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method= request.getMethod();
		
		if("POST".equals(method)){ // POST 방식일 경우만 처리
			
			// 휴면 계정의 아이디, 비밀번호
			HttpSession session = request.getSession();
			String userid = (String) session.getAttribute("userid");
			String pwd = (String) session.getAttribute("pwd");
			
			// 계정의 idle 상태 해제
			int n = mdao.updateIdle(userid);

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("passwd", pwd);
			
			// 로그인 처리
			MemberVO loginuser = mdao.login(paraMap);
			session.setAttribute("loginuser", loginuser);
			
			// 로그인 날짜 업데이트
			mdao.updateLoginDate(userid);
			
			session.removeAttribute("userid");
			session.removeAttribute("pwd");
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("n", n);
			
			String json = jsonObj.toString();
			// System.out.println(">>> 확인용 json =>" + json);
		
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
		}
	}

}
