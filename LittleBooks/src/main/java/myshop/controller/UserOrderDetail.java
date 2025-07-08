package myshop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.domain.MemberVO;
import myshop.domain.OrderDetailVO;
import myshop.model.OrderDAO;
import myshop.model.OrderDAO_imple;

public class UserOrderDetail extends AbstractController {
	
	OrderDAO odao = new OrderDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String ordercode = request.getParameter("ordercode");
		// System.out.println(ordercode);
		
		// 주문자 아이디 찾기
		String userid = odao.selectUserid(ordercode);
		// System.out.println(userid);
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("userid", userid);
		paraMap.put("ordercode", ordercode);
		
		// 주문자 정보 찾기
		MemberVO member = odao.selectOrderMember(paraMap);
		// System.out.println(member);
		
		List<OrderDetailVO> orderDetailList = odao.selectAllDetail(userid);  // 모든 주문 내역을 조회
        
        request.setAttribute("orderDetailList", orderDetailList);
		request.setAttribute("member", member);
		request.setAttribute("ordercode", ordercode);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/admin/UserOrderDetail.jsp");
	}

}
