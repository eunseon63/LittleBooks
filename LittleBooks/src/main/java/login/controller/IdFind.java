package login.controller;

import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

// 아이디 찾기 처리 
public class IdFind extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

	    String method = request.getMethod();

	    if ("POST".equalsIgnoreCase(method)) { // POST 방식일 때 
	    	
	    	// 입력한 이름, 이메일
	        String name = request.getParameter("name");
	        String email = request.getParameter("email");

	        Map<String, String> paraMap = new HashMap<>();
	        paraMap.put("name", name);
	        paraMap.put("email", email);

	        // 사용자 아이디 조회
	        // 없는 경우 null
	        String userid = mdao.findUserid(paraMap);

	        // 아이디 저장
	        // null인 경우에는 "존재하지 않습니다." 저장
	        request.setAttribute("userid", (userid != null) ? userid : "존재하지 않습니다.");

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
