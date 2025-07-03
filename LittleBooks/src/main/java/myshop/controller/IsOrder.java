package myshop.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class IsOrder extends AbstractController {
	
	private BookDAO bdao = null;
	
	public IsOrder() {
		bdao = new BookDAO_imple();
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String fk_bookseq = request.getParameter("fk_bookseq");
		String fk_userid = request.getParameter("fk_userid");
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("fk_bookseq", fk_bookseq);
		paraMap.put("fk_userid", fk_userid);
		
		boolean bool = bdao.isOrder(paraMap);

		JSONObject jsonObj = new JSONObject(); // {}
		jsonObj.put("isOrder", bool); // {"isOrder":true} 또는 {"isOrder":false} 
		
		String json = jsonObj.toString();
		request.setAttribute("json", json);

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");

	}

}
