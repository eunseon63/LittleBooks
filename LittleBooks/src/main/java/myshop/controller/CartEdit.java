package myshop.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class CartEdit extends AbstractController {

	private BookDAO bdao = null;
	
	public CartEdit() {
		bdao = new BookDAO_imple();
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) {
			// GET 방식이라면
			
			String message = "비정상적인 경로로 들어왔습니다";
			String loc = "javascript:history.back()";
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		
		else if("POST".equalsIgnoreCase(method) && super.checkLogin(request)) {
			// POST 방식이고, 로그인을 했다라면
			
			String cartseq = request.getParameter("cartseq");
			String cqty = request.getParameter("cqty");
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("cartseq", cartseq);
			paraMap.put("cqty", cqty);
			
			// 장바구니 테이블에서 특정제품의 주문량 변경시키기
			int n = bdao.updateCart(paraMap);
			
			JSONObject jsobj = new JSONObject(); // {}
			jsobj.put("n", n);  // {"n":1}
			
			String json = jsobj.toString();  // "{"n":1}"
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
		}
		
	}


}
