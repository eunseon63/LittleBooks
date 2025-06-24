package member.controller;

import java.sql.SQLException;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;



public class MemberRegister extends AbstractController {
	MemberDAO mdao = new MemberDAO_imple();
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String method = request.getMethod();
		if("GET".equals(method)) {
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/register/register.jsp");
		}
		else {
			String name = request.getParameter("name");//form 태그에서 받아오기

			String userid = request.getParameter("userid");//form 태그에서 받아오기

			String pwd= request.getParameter("pwd");//form 태그에서 받아오기

			String email = request.getParameter("email");//form 태그에서 받아오기

			String hp1 = request.getParameter("hp1");//form 태그에서 받아오기

			String hp2 = request.getParameter("hp2");//form 태그에서 받아오기

			String hp3 = request.getParameter("hp3");//form 태그에서 받아오기

			String postcode = request.getParameter("postcode");//form 태그에서 받아오기

			String address = request.getParameter("address");//form 태그에서 받아오기

			String detailaddress = request.getParameter("detailaddress");//form 태그에서 받아오기
			
			String extraaddress = request.getParameter("extraaddress");//form 태그에서 받아오기
			
			
			String birthday = request.getParameter("birthday");//form 태그에서 받아오기

			String mobile = hp1 + hp2 + hp3; 

			MemberVO member = new MemberVO();
			member.setName(name);
			member.setUserid(userid);
			member.setPwd(pwd);
			member.setEmail(email);
			member.setMobile(mobile);
			member.setPostcode(postcode);
			member.setAddress(address);
			member.setDetailaddress(detailaddress);
			member.setExtraaddress(extraaddress);
			
			member.setAddress(address);
			member.setBirthday(birthday);
			
			
			
			//===회원가입이 성공되어지면 "회원가입성공" 이라는 alert 를 띄우고 시작페이지를 이동한다ㅓ
			String message= "";
			String loc= "";
			
			try {
				int n= mdao.registerMember(member);
				
				if(n==1) {
					
					request.setAttribute("userid",userid);
					request.setAttribute("pwd",pwd);
					request.setAttribute("name",name);
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/index.jsp");
				}
				
				
				}catch(SQLException e) {
					e.printStackTrace();
					
					message = "회원가입 실패ㅜㅜ";
					loc= "javascript:history.back()"; //자바스크립트를 이용한 이전페이지로 이동하는 것.
				
					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
				
				}	
		}

	}

}
