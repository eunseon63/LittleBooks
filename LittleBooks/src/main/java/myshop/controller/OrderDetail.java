package myshop.controller;

import java.util.List;
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

   
   
   @Override
   public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
       HttpSession session = request.getSession();
       MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
       String userid = loginuser.getUserid();

       OrderDAO dao = new OrderDAO_imple();

       // 1. 전체 주문 목록
       List<OrderVO> orderList = dao.getOrderListByUserid(userid);
       request.setAttribute("orderList", orderList);

       // 2. 특정 주문 상세가 요청된 경우
       String ordercode = request.getParameter("ordercode");

//       if (ordercode != null && !ordercode.trim().isEmpty()) {
//           // 상세 주문 데이터 세팅
//           //List<OrderDetailVO> detailList = dao.getOrderDetailList(ordercode, userid);
//           OrderVO deliveryInfo = dao.getOrderInfo(ordercode, userid);
//
//           int totalAmount = 0;
//           int totalQty = 0;
//           for (OrderDetailVO vo : detailList) {
//               totalAmount += vo.getOdrprice();
//               totalQty += vo.getOqty();
//           }
//
//           request.setAttribute("selectedOrdercode", ordercode);
//           request.setAttribute("detailList", detailList);
//           request.setAttribute("deliveryInfo", deliveryInfo);
//           request.setAttribute("totalAmount", totalAmount);
//           request.setAttribute("totalQty", totalQty);
//       }

       super.setViewPage("/WEB-INF/myshop/orderDetail.jsp");
   }

}