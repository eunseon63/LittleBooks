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

public class ReactivateAccountEnd extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method= request.getMethod();
		
		if("POST".equals(method)){
			
			HttpSession session = request.getSession();
			String userid = (String) session.getAttribute("userid");
			String pwd = (String) session.getAttribute("pwd");
			
			int n = mdao.updateIdle(userid);

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("passwd", pwd);
			
			MemberVO loginuser = mdao.login(paraMap);
			session.setAttribute("loginuser", loginuser);
			
			mdao.updateLoginDate(userid);
			
			session.removeAttribute("userid");
			session.removeAttribute("pwd");
			
			JSONObject jsonObj = new JSONObject(); //https://mvnrepository.com/artifact/org.json/json/20240303 에서 bundle 다운 후 webinf lib 폴더에 넣어둬야 import 사용 가능함
			jsonObj.put("n", n); // {"isExists":true}또는 {"isExists":false} 으로 만들어 준다.
			
			String json = jsonObj.toString(); // 문자열 형태인  "{"isExists":true}"또는 "{"isExists":false}" 으로 만들어 준다.
			System.out.println(">>> 확인용 json =>" + json);
		
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
		}
	}

}
