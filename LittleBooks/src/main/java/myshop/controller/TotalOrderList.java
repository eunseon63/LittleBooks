package myshop.controller;

import java.util.List;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import myshop.domain.OrderDetailVO;
import myshop.model.OrderDAO;
import myshop.model.OrderDAO_imple;

public class TotalOrderList extends AbstractController {

    OrderDAO odao = new OrderDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
        
        // 전체 주문 상세 내역 가져오기
        List<OrderDetailVO> orderDetailList = odao.selectAllDetail();  // 관리자는 모든 주문 내역을 조회
        
        // 주문 상세 정보가 있으면
        request.setAttribute("orderDetailList", orderDetailList);
        
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/myshop/admin/totalOrderList.jsp");
    }
}

