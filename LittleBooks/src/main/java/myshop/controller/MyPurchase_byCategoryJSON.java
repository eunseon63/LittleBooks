package myshop.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;


public class MyPurchase_byCategoryJSON extends AbstractController {

	private BookDAO bdao = null;
	
	public MyPurchase_byCategoryJSON() {
		bdao = new BookDAO_imple();
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 나의 카테고리별주문 통계 보기는 반드시 해당사용자가 로그인을 해야만 볼 수 있다.
		if(!super.checkLogin(request)) {
			request.setAttribute("message", "나의 카테고리별주문 통계를 보려면 먼저 로그인 부터 하세요!!");
			request.setAttribute("loc", "javascript:history.back()"); 
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		
		else {
			// 로그인을 했을 경우 
			String userid = request.getParameter("userid");
			
			List<Map<String, String>> myPurchase_map_List = bdao.myPurchase_byCategory(userid);
			
			JSONArray json_arr = new JSONArray();
			
			if(myPurchase_map_List.size() > 0) {
				
				for(Map<String, String> map : myPurchase_map_List) {
					JSONObject json_obj = new JSONObject();
					json_obj.put("categoryname", map.get("categoryname"));
					json_obj.put("cnt", map.get("cnt"));
					json_obj.put("sumpay", map.get("sumpay"));
					json_obj.put("sumpay_pct", map.get("sumpay_pct"));
					
					json_arr.put(json_obj);
				}// end of for------------------------------
				
			}
			
			request.setAttribute("json", json_arr.toString());
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
		}	
		
	}

}
