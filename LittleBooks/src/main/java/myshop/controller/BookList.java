package myshop.controller;

import java.util.List;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;
import myshop.domain.BookVO;

public class BookList extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		 // 1) category 파라미터 받기
	    String category = request.getParameter("category");
	    if (category == null || category.isEmpty()) {
	        category = "all";  // 기본값 설정
	    }
	    
	    // 2) DAO 생성 및 메서드 호출
	    BookDAO dao = new BookDAO_imple();
	    List<BookVO> bookList = dao.selectBooksByCategory(category);
	    
	 // ===== 콘솔에 출력해보기 =====
	    System.out.println(">>> [카테고리: " + category + "] 도서 목록 조회 결과 <<<");
	    for (BookVO book : bookList) {
	        System.out.println("책제목: " + book.getBname());
	        System.out.println("저자: " + book.getAuthor());
	        System.out.println("가격: " + book.getPrice());
	        System.out.println("카테고리: " + (book.getCvo() != null ? book.getCvo().getCategoryname() : "없음"));
	        System.out.println("입고일자: " + book.getBinputdate());
	        System.out.println("-----------------------------");
	    }
	    // ==============================
	    
	    // 3) 결과를 request 객체에 저장해서 JSP에 전달
	    request.setAttribute("bookList", bookList);
	    request.setAttribute("category", category);

	    // 4) 페이지 포워딩
	    super.setRedirect(false);
	    super.setViewPage("/WEB-INF/myshop/booklist.jsp");

	}

}
