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

        String sort = request.getParameter("sort");  // "new", "sales", or null

        List<BookVO> bookList;

        if ("sales".equals(sort)) {
            // 판매순 정렬은 OrderDAO에서 처리 (판매량 합산)
            OrderDAO orderDao = new OrderDAO_imple();

            int categorySeq = 0;
            try {
                categorySeq = Integer.parseInt(category);
            } catch (Exception e) {
                // all인 경우 등 숫자가 아닐 때 0으로 처리 (전체)
                categorySeq = 0;
            }

            bookList = orderDao.selectBooksOrderBySales(categorySeq);

        } else {
            // 기존 new, 기본 정렬 처리
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
