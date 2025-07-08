package myshop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import myshop.domain.OrderDetailVO;
import myshop.domain.OrderVO;
import myshop.model.OrderDAO;
import myshop.model.OrderDAO_imple;

public class OrderDetail extends AbstractController {

	OrderDAO odao = new OrderDAO_imple();
   
   @Override
   public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	   
		String ordercode = request.getParameter("ordercode");

		List<OrderDetailVO> orderOneDetailList = odao.selectOneDetail(ordercode);  // 한 주문 내역을 조회
        
        request.setAttribute("orderOneDetailList", orderOneDetailList);

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/myPage/orderDetail.jsp");
   }

}