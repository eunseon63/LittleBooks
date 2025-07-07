package login.controller;

import java.util.List;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

// 주문 내역 요청
public class OrderList extends AbstractController {

	MemberDAO pdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
        HttpSession session = request.getSession();
        MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	
        if (loginuser == null) { // 로그인 안 된 경우
            request.setAttribute("message", "로그인 후 이용 가능합니다.");
            request.setAttribute("loc", "login.go");
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
            return;
        }

        String userid = loginuser.getUserid();
        // 주문내역을 가져와서 orderList에 저장
        List<MemberVO> orderList = pdao.getOrderListByUserid(userid);

        request.setAttribute("orderList", orderList);
		super.setViewPage("/WEB-INF/login/myPage/orderList.jsp");
	}

}
