package myshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;
import myshop.domain.BookVO;

public class BookDetail extends AbstractController {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 1) bookseq 파라미터 받아오기
        String bookseq = request.getParameter("bookseq");

        // 2) 유효성 검사
        if (bookseq == null || bookseq.trim().isEmpty()) {
            request.setAttribute("message", "잘못된 접근입니다.");
            request.setAttribute("loc", request.getContextPath() + "/myshop/booklist.go");
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
            return;
        }

        // 3) DAO 호출하여 도서 정보 가져오기
        BookDAO dao = new BookDAO_imple();
        BookVO book = dao.selectOneBookByBookseq(bookseq);

        if (book == null) {
            request.setAttribute("message", "존재하지 않는 도서입니다.");
            request.setAttribute("loc", request.getContextPath() + "/myshop/booklist.go");
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
            return;
        }

        // 4) 도서 정보 request에 저장
        request.setAttribute("book", book);

        // 5) view 지정
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/myshop/bookdetail.jsp");
    }
}
