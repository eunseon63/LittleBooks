package login.controller;

import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class IdFind extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

	    String method = request.getMethod();

	    if ("POST".equalsIgnoreCase(method)) {
	        String name = request.getParameter("name");
	        String email = request.getParameter("email");

	        Map<String, String> paraMap = new HashMap<>();
	        paraMap.put("name", name);
	        paraMap.put("email", email);

	        String userid = mdao.findUserid(paraMap);

	        request.setAttribute("userid", (userid != null) ? userid : "존재하지 않습니다.");

	        // 팝업 결과 전용 JSP로 이동
	        super.setRedirect(false);
	        super.setViewPage("/WEB-INF/login/idFindSuccess.jsp");
	        return;
	    }

	    //  GET 요청이면 아이디 찾기 폼 페이지
	    request.setAttribute("method", method);
	    super.setRedirect(false);
	    super.setViewPage("/WEB-INF/login/idFind.jsp");
	}


}
