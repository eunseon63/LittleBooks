package login.controller;

import java.sql.SQLException;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

// 회원 탈퇴 처리
public class DeleteMember extends AbstractController {

    private MemberDAO mdao = new MemberDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) { // GET 방식일 때 

        	// 삭제 여부 저장
            boolean isDeleted = false;

            try {
            	// 로그인한 경우에만 
                if (loginuser != null) {
                	// 회원 탈퇴 처리
                    isDeleted = mdao.deleteMember(loginuser.getUserid());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String message = "";
            String loc = "";

            if (isDeleted) { // 삭제 성공 시 
                message = "회원 탈퇴가 완료되었습니다.";
                session.invalidate(); // 세션 만료(로그아웃)
                loc = request.getContextPath() + "/index.go"; // 홈으로 이동
            } else {
            	// 삭제 실패 시 
                message = "회원 탈퇴에 실패했습니다.";
                loc = request.getContextPath() + "/login/myPage.go"; // 마이페이지로 이동
            }

            request.setAttribute("message", message);
            request.setAttribute("loc", loc);

            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
        }
    }
}
