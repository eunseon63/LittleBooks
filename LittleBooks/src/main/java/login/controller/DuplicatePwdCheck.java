package login.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

// 비밀번호 중복 검사 처리
public class DuplicatePwdCheck extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); 
		
		if("POST".equals(method)) { // POST 방식일 때 
			
			// 새로운 비밀번호와 아이디
			String new_pwd = request.getParameter("new_pwd");
			String userid = request.getParameter("userid");
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("new_pwd", new_pwd);
			paraMap.put("userid", userid);
			
			// DB에 동일한 비밀번호가 존재하는지 확인
			// 현재 암호와 동일하면 true, 동일하지 않으면 false
			boolean isExists = mdao.duplicatePwdCheck(paraMap);
			
			JSONObject jsonObj = new JSONObject(); 
			jsonObj.put("isExists", isExists); 
			
			String json = jsonObj.toString();
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
			
		}
		
	}

}
