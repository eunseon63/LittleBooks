package myshop.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class CartAdd extends AbstractController {

    private BookDAO bdao = null;

    public CartAdd() {
        bdao = new BookDAO_imple();
    }
    
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인 유무 검사
		if(!super.checkLogin(request)) {
			
			request.setAttribute("message", "로그인 후 이용해주세요.");
			request.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {
			// 로그인을 한 상태이라면 
			// 장바구니 테이블(tbl_cart)에 해당 제품을 담아야 한다.
			// 장바구니 테이블에 해당 제품이 존재하지 않는 경우에는 tbl_cart 테이블에 insert 를 해야하고, 
			// 장바구니 테이블에 해당 제품이 존재하는 경우에는 또 그 제품을 추가해서 장바구니 담기를 한다라면 tbl_cart 테이블에 update 를 해야한다. 
			
			String method = request.getMethod();
			
			if("POST".equalsIgnoreCase(method)) {
				// POST 방식이라면 
				
				String fk_bookseq = request.getParameter("fk_bookseq"); // 제품번호
				String cqty = request.getParameter("cqty"); // 주문량
				
				HttpSession session = request.getSession();
				MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
				String fk_userid = loginuser.getUserid(); // 사용자ID
				
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("bookseq", fk_bookseq);
				paraMap.put("cqty", cqty);
				paraMap.put("fk_userid", fk_userid);
				
				try {
					int n = bdao.addCart(paraMap); 
					
					if(n==1) {
						super.setRedirect(true);
						super.setViewPage(request.getContextPath()+"/shop/cartList.go");
					}
				
				} catch(SQLException e) {
					request.setAttribute("message", "장바구니 담기 실패!!");
					request.setAttribute("loc", "javascript:history.back()");
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
				}
				
			}
			else {
				// GET 방식이라면
				String message = "비정상적인 경로로 들어왔습니다";
				String loc = "javascript:history.back()";
					
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				  
			    super.setRedirect(false);	
			    super.setViewPage("/WEB-INF/msg.jsp");
			}
		
		}	

	}

}
