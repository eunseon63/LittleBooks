package login.controller;

import java.sql.SQLException;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;


public class DeleteMember extends AbstractController {

    private MemberDAO mdao = new MemberDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) {

            boolean isDeleted = false;

            try {
                if (loginuser != null) {
                    isDeleted = mdao.deleteMember(loginuser.getUserid());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String message = "";
            String loc = "";

            if (isDeleted) {
                message = "회원 탈퇴가 완료되었습니다.";
                session.invalidate(); // 세션 만료
                loc = request.getContextPath() + "/index.go";
            } else {
                message = "회원 탈퇴에 실패했습니다.";
                loc = request.getContextPath() + "/login/myPage.go";
            }

            request.setAttribute("message", message);
            request.setAttribute("loc", loc);

            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
        }
    }
}
