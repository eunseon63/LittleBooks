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
    	if (sort == null || sort.isEmpty()) {
    	    sort = "new"; // 기본 정렬
    	}

    	List<BookVO> bookList;
    	BookDAO dao = new BookDAO_imple();
    	OrderDAO orderDao = new OrderDAO_imple();

    	if ("all".equals(category)) {
    	    if ("sales".equals(sort)) {
    	        bookList = orderDao.selectBooksOrderBySales(0); // 0 → 전체
    	    } else {
    	        bookList = dao.selectAllBooksSorted(sort);
    	    }
    	} else {
    	    int categorySeq = dao.getCategorySeqByName(category);
    	    if ("sales".equals(sort)) {
    	        bookList = orderDao.selectBooksOrderBySales(categorySeq);
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
