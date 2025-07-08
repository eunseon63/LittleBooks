package myshop.controller;

import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class UserTotalSpent extends AbstractController {
	
	private BookDAO bdao;

	    public UserTotalSpent() {
	        bdao = new BookDAO_imple();
	    }
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 List<Map<String, Object>> userSpentList = bdao.getUserTotalSpentList();
	        request.setAttribute("userSpentList", userSpentList);

	        super.setRedirect(false);
	        super.setViewPage("/WEB-INF/myshop/userTotalSpent.jsp");

	}

}
