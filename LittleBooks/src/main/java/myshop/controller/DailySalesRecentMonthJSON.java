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

public class DailySalesRecentMonthJSON extends AbstractController {
	private BookDAO bdao;

    public DailySalesRecentMonthJSON() {
        bdao = new BookDAO_imple();
    }
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Map<String, String>> dailySalesList = bdao.getDailySalesLast30Days();

        JSONArray jsonArr = new JSONArray();

        if (dailySalesList != null) {
        	for (Map<String, String> map : dailySalesList) {
        	    JSONObject jsonObj = new JSONObject();
        	    jsonObj.put("date", map.get("date"));      
        	    jsonObj.put("date_total", map.get("date_total")); 
        	    jsonArr.put(jsonObj);
        	}
        }

        String json = jsonArr.toString(); 
        request.setAttribute("json", json);

        // JSON 응답으로 전송
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/jsonview.jsp");

	}

}
