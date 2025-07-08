package myshop.controller;

import java.util.List;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.domain.BookVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class NewBooksController extends AbstractController {

    private BookDAO dao = new BookDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<BookVO> newBooks = dao.selectBooksBySpec(3); // ★ fk_snum = 3 → NEW 도서

        request.setAttribute("newBooks", newBooks);

        super.setRedirect(false);
        super.setViewPage("/WEB-INF/myshop/newbooks.jsp");
    }
}
