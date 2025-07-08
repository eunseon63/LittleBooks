package member.controller;

import member.model.MemberDAO;
import member.model.MemberDAO_imple;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 이메일 중복 확인 처리
public class EmailDuplicateCheck extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String method = request.getMethod();

		if ("POST".equals(method)) { // POST 방식일 때

			String email = request.getParameter("email");

			// 이메일이 존재하는지 확인
			boolean isExists = mdao.emailDuplicateCheck(email);

			JSONObject jsonObj = new JSONObject(); 
			
			jsonObj.put("isExists", isExists); 

			String json = jsonObj.toString(); 
			// System.out.println(">>> 확인용 json =>" + json);

			request.setAttribute("json", json);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
		}
	}
}
