package login.controller;

import java.util.List;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import myshop.domain.OrderDetailVO;
import myshop.model.OrderDAO;
import myshop.model.OrderDAO_imple;

// 주문 내역 요청
public class OrderList extends AbstractController {

	OrderDAO odao = new OrderDAO_imple();
	
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
        
        // 전체 주문 상세 내역 가져오기
        List<OrderDetailVO> orderDetailList = odao.selectAllDetail(userid);
        // System.out.println(orderDetailList);
        
        
        int totalQty = 0;      // 총 수량
        int totalPrice = 0;    // 총 금액

        for (OrderDetailVO od : orderDetailList) {
            totalQty += od.getOqty();                            // 수량
            totalPrice += od.getOqty() * od.getOdrprice();      // 수량 * 단가
        }

        request.setAttribute("totalQty", totalQty);
        request.setAttribute("totalPrice", totalPrice);
        // 주문 상세 정보가 있으면
        request.setAttribute("orderDetailList", orderDetailList);
        
        super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/myPage/orderList.jsp");
	}

}
