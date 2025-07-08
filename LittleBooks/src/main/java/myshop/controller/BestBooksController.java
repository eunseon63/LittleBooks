package myshop.controller;

import java.util.List;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.domain.BookVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class BestBooksController extends AbstractController {

    private BookDAO dao = new BookDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<BookVO> bestBooks = dao.selectBooksBySpec(2); // ★ fk_snum = 2 → BEST 도서

        request.setAttribute("bestBooks", bestBooks);

        super.setRedirect(false);
        super.setViewPage("/WEB-INF/myshop/bestbooks.jsp");
    }
}
