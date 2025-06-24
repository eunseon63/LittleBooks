package login.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class PwdFind extends AbstractController {
	
	private MemberDAO mdao = new MemberDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		
		if ("POST".equals(method)) {
			
			String userid = request.getParameter("userid");
			String email = request.getParameter("email");
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("email", email);
			
			boolean isUserExist = mdao.isUserExist(paraMap);
			
			boolean sendMailSuccess = false; // 메일이 정상적으로 전송되었는지 유무를 알아오기 위한 용도
			
			if(isUserExist) {
				// 회원으로 존재하는 경우
				
				// 인증키를 랜덤하게 생성하도록 한다.
				Random rnd = new Random();
				
				String certification_code = "";
				// 인증키는 영문소문자 5글자 + 숫자 7글자로 만들겠습니다.
				
				char randchar = ' ';
				for(int i=0; i<5; i++) {
					/*
					   min 부터 max 사이의 값으로 랜덤한 정수를 얻으려면 
				       int rndnum = rnd.nextInt(max - min + 1) + min;
				           영문 소문자 'a' 부터 'z' 까지 랜덤하게 1개를 만든다.
				    */
					randchar = (char) (rnd.nextInt('z' - 'a' + 1) + 'a');
					certification_code += randchar;
				}// end of for-------------------------
				
				int randnum = 0;
				for(int i=0; i<7; i++) {
					/*
					   min 부터 max 사이의 값으로 랜덤한 정수를 얻으려면 
				       int rndnum = rnd.nextInt(max - min + 1) + min;
				           숫자 0 부터 9 까지 랜덤하게 1개를 만든다.
				    */
					randnum = rnd.nextInt(9 - 0 + 1) + 0;
					certification_code += randnum;
				}// end of for-------------------------
				
			//	System.out.println("~~~~~ 확인용 certification_code : " + certification_code); 
				// ~~~~~ 확인용 certification_code : gzhzs4631050
				
				// 랜덤하게 생성한 인증코드(certification_code)를 비밀번호 찾기를 하고자 하는 사용자의 email 로 전송시킨다. 
				GoogleMail mail = new GoogleMail();
				
				try {
					mail.send_certification_code(email, certification_code);
					sendMailSuccess = true; // 메일 전송 성공했음을 기록함.
					
					// 세션불러오기 
					HttpSession session = request.getSession();
					session.setAttribute("certification_code", certification_code);
					// 발급한 인증코드를 세션에 저장시킴.
					
				} catch(Exception e) {
					// 메일 전송이 실패한 경우
					e.printStackTrace();
					sendMailSuccess = false; // 메일 전송 실패했음을 기록함.
				}
								
			}// end of if(isUserExist) {}----------------------
			//////////////////////////////////////////////////////
			
			request.setAttribute("isUserExist", isUserExist);
			request.setAttribute("sendMailSuccess", sendMailSuccess);
			request.setAttribute("userid", userid);
			request.setAttribute("email", email);
			
		}// end of if("POST".equalsIgnoreCase(method)) {}-------------------
		
		request.setAttribute("method", method);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/pwdFind.jsp");		
	}

	
}
