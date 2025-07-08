package myshop.controller;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.model.OrderDAO;
import myshop.model.OrderDAO_imple;
import net.nurigo.java_sdk.api.Message;

public class SmsSend extends AbstractController {
	
	OrderDAO odao = new OrderDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String[] ordercodes = request.getParameterValues("ordercodes");
		// System.out.println(ordercodes);
		
	    String api_key = "NCSPXB3OVMWS4HLL";
	  	String api_secret = "NM6MIMJP0IJB60TPIL0E0HO0VZDOELUG";
	  	
	  	JSONArray resultArr = new JSONArray();

		for (int i=0; i<ordercodes.length; i++) {
			String userid = odao.selectUserid(ordercodes[i]);
			
			// 전화번호 찾는 메서드
			String mobile = odao.selectMobile(userid);
			System.out.println(mobile);
			
			String smsContent = "LittleBooks 에서 주문하신 책 주문번호[" + ordercodes[i] + "] 를 우체국택배로 배송하였습니다.";

			Message coolsms = new Message(api_key, api_secret);

			HashMap<String, String> paraMap = new HashMap<>();
			paraMap.put("to", mobile); // 수신번호
			paraMap.put("from", "01087134833"); // 발신번호
			paraMap.put("type", "SMS"); // Message type ( SMS(단문), LMS(장문), MMS, ATA )
			paraMap.put("text", smsContent); // 문자내용
			paraMap.put("app_version", "JAVA SDK v2.2"); // application name and version 

			JSONObject jsobj = (JSONObject) coolsms.send(paraMap);
			
			Object successCnt = jsobj.get("success_count");
			
			// 문자메시지를 보낸경우
			if (successCnt != null && Integer.parseInt(successCnt.toString()) > 0) {
				
				String ordercode = odao.selectOrdercode(userid);
				odao.updateDeliverstatus(ordercode);
			}
		}
		
		request.setAttribute("json", resultArr.toString());

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");	
	}

}
