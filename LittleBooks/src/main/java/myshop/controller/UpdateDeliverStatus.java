package myshop.controller;

import java.io.PrintWriter;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.model.OrderDAO;
import myshop.model.OrderDAO_imple;

// 주문 상태 변경 처리
public class UpdateDeliverStatus extends AbstractController {

	OrderDAO odao = new OrderDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String ordercode = request.getParameter("ordercode");
		String status = request.getParameter("status"); 
 	
		int result = odao.updateDeliverStatus(ordercode, Integer.parseInt(status));
 
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		if (result > 0) {
		    out.print("success");
		} else {
		    out.print("fail");
		}
		
		out.close();
		    
	}

}
