package myshop.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.domain.MemberVO;


public class AdminChart extends AbstractController {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // === 로그인 유무 검사 === //
        if (!super.checkLogin(request)) {
            // 로그인하지 않은 경우
            request.setAttribute("message", "관리자 통계를 조회하려면 먼저 로그인하세요!!");
            request.setAttribute("loc", "javascript:history.back()");

            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
            return;
        }

        else {
            // 로그인한 경우 => 관리자 권한 확인
        	MemberVO loginuser = (MemberVO) request.getSession().getAttribute("loginuser");

        	if (loginuser == null || !"admin".equalsIgnoreCase(loginuser.getUserid())) {
        	    request.setAttribute("message", "관리자만 접근 가능한 페이지입니다.");
        	    request.setAttribute("loc", "javascript:history.back()");

        	    super.setRedirect(false);
        	    super.setViewPage("/WEB-INF/msg.jsp");
        	    return;
        	}

            // === 정상 접근 === //
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/chart/adminChart.jsp");
        }
    }
}
