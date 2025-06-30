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
	    
	    // 1) category, sort 파라미터 받기
	    String category = request.getParameter("category");
	    if (category == null || category.isEmpty()) {
	        category = "all";
	    }

	    String sort = request.getParameter("sort");  // "new" or null

	    // 2) DAO 호출
	    BookDAO dao = new BookDAO_imple();
	    List<BookVO> bookList;

	    if ("all".equals(category)) {
	        bookList = dao.selectAllBooksSorted(sort);  // 전체 정렬
	    } else {
	        bookList = dao.selectBooksByCategorySorted(category, sort); // 카테고리 정렬
	    }

	    // 3) 로그 찍기
	    System.out.println(">>> [카테고리: " + category + ", 정렬: " + sort + "] 도서 목록 조회 결과 <<<");
	    for (BookVO book : bookList) {
	        System.out.println("책제목: " + book.getBname());
	        System.out.println("저자: " + book.getAuthor());
	        System.out.println("가격: " + book.getPrice());
	        System.out.println("카테고리: " + (book.getCvo() != null ? book.getCvo().getCategoryname() : "없음"));
	        System.out.println("입고일자: " + book.getBinputdate());
	        System.out.println("-----------------------------");
	    }

	    // 4) JSP에 전달
	    request.setAttribute("bookList", bookList);
	    request.setAttribute("category", category);
	    request.setAttribute("sort", sort);  // 정렬 유지용

	    super.setRedirect(false);
	    super.setViewPage("/WEB-INF/myshop/booklist.jsp");
	}


}
