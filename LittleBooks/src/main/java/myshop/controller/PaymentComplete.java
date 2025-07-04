package myshop.controller;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PaymentComplete extends AbstractController {

	
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		
		// 결제 정보 
        String imp_uid = request.getParameter("imp_uid");
        String merchant_uid = request.getParameter("merchant_uid");
        int amount = Integer.parseInt(request.getParameter("amount"));
        int bookseq = Integer.parseInt(request.getParameter("bookseq"));
        int qty = Integer.parseInt(request.getParameter("qty"));
        
        
        //  배송지 정보 
        String receiverName = request.getParameter("receiverName");
        String receiverPhone = request.getParameter("receiverPhone");
        String receiverPostcode = request.getParameter("receiverPostcode");
        String receiverAddress = request.getParameter("receiverAddress");
        String receiverDetailAddress = request.getParameter("receiverDetailAddress");
        String receiverExtraAddress = request.getParameter("receiverExtraAddress");
	  
        
        
        
        // 결제 완료 응답
        
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("status", "success");
        jsonObj.put("message", "결제가 완료되었습니다.");

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(jsonObj.toString());
	
	
	
	}

}
