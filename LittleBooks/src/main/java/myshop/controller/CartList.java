package myshop.controller;

import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import myshop.domain.CartVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class CartList extends AbstractController {

    private BookDAO bdao = null;

    public CartList() {
        bdao = new BookDAO_imple();
    }

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String referer = request.getHeader("referer");
		
		// 주소창에 직접 입력 후 들어온 경우 
		if(referer == null) {
		   super.setRedirect(true);
		   super.setViewPage(request.getContextPath()+"/index.up");
		   return;
		}
		
		// 로그인 후 장바구니 이용 가능
		if(!super.checkLogin(request)) {
			request.setAttribute("message", "로그인 후 이용해주세요.");
			request.setAttribute("loc", "javascript:history.back()"); 
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		// 로그인 한 경우
		else {
			HttpSession session = request.getSession();
			MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
			
			List<CartVO> cartList = bdao.selectProductCart(loginuser.getUserid());
			Map<String, Integer> sumMap = bdao.selectCartSumPrice(loginuser.getUserid());
			
			request.setAttribute("cartList", cartList);
			request.setAttribute("sumMap", sumMap);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/cartList.jsp");
		}

	}

}
