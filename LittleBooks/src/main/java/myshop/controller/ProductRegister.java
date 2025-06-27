package myshop.controller;

import java.util.List;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import myshop.domain.CategoryVO;
import myshop.domain.SpecVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class ProductRegister extends AbstractController {

    private BookDAO pdao = null;
	
	public ProductRegister() {
		pdao = new BookDAO_imple();
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// == 관리자(admin)로 로그인 했을 때만 제품등록이 가능하도록 한다. == //
		HttpSession session = request.getSession();
		
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		if( loginuser != null && "admin".equals(loginuser.getUserid()) ) {
			// 관리자(admin)로 로그인 했을 경우
			
			String method = request.getMethod();
			
			if(!"POST".equalsIgnoreCase(method)) { // "GET" 이라면
				
				// 카테고리 목록을 조회해오기
				List<CategoryVO> categoryList = pdao.getCategoryList();
				request.setAttribute("categoryList", categoryList);
				
				// SPEC 목록을 조회해오기
				List<SpecVO> specList = pdao.getSpecList();
				request.setAttribute("specList", specList);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/myshop/admin/productRegister.jsp");  
			}
		}

	}

}
