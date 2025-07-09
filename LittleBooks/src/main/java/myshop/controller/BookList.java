package myshop.controller;

import java.util.List;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.domain.BookVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;
import myshop.model.OrderDAO;
import myshop.model.OrderDAO_imple;

public class BookList extends AbstractController {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    	String category = request.getParameter("category");
    	if (category == null || category.isEmpty()) {
    	    category = "all";
    	}

    	String sort = request.getParameter("sort");

    	List<BookVO> bookList;

    	if ("sales".equals(sort)) {
    	    // sales 정렬은 숫자 category 받음
    	    int categorySeq = 0;
    	    try {
    	        categorySeq = Integer.parseInt(category);
    	    } catch(Exception e) {
    	        categorySeq = 0; // all로 처리
    	    }

    	    OrderDAO orderDao = new OrderDAO_imple();
    	    bookList = orderDao.selectBooksOrderBySales(categorySeq);

    	} else {
    	    // new 정렬은 한글 category 이름 받음 (기존 코드 유지)
    	    BookDAO dao = new BookDAO_imple();

    	    if ("all".equals(category)) {
    	        bookList = dao.selectAllBooksSorted(sort);
    	    } else {
    	        bookList = dao.selectBooksByCategorySorted(category, sort);
    	    }
    	}


        request.setAttribute("bookList", bookList);
        request.setAttribute("category", category);
        request.setAttribute("sort", sort);

        super.setRedirect(false);
        super.setViewPage("/WEB-INF/myshop/booklist.jsp");
    }
}
