package login.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DormantPage extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/dormantPage.jsp");
	}

}
