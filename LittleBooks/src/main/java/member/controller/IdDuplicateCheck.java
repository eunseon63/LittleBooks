package member.controller;

import member.model.MemberDAO;
import member.model.MemberDAO_imple;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class IdDuplicateCheck extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String method= request.getMethod();
		
		if("POST".equals(method)){
			
			String userid= request.getParameter("userid");
		
			boolean isExists = mdao.idDuplicateCheck(userid);
			
			JSONObject jsonObj = new JSONObject(); //https://mvnrepository.com/artifact/org.json/json/20240303 에서 bundle 다운 후 webinf lib 폴더에 넣어둬야 import 사용 가능함
			jsonObj.put("isExists", isExists); // {"isExists":true}또는 {"isExists":false} 으로 만들어 준다.
			
			String json = jsonObj.toString(); // 문자열 형태인  "{"isExists":true}"또는 "{"isExists":false}" 으로 만들어 준다.
			System.out.println(">>> 확인용 json =>" + json);
		
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
		}

	}

}
