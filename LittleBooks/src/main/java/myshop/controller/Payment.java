package myshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import common.controller.AbstractController;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;
import myshop.domain.BookVO;

public class Payment extends AbstractController {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 1) bookseq, qty 파라미터 받아오기
        String bookseq = request.getParameter("bookseq");
        String qtyStr = request.getParameter("qty");

        // 2) 유효성 검사
        if (bookseq == null || bookseq.trim().isEmpty() || qtyStr == null || qtyStr.trim().isEmpty()) {
            request.setAttribute("message", "잘못된 접근입니다.");
            request.setAttribute("loc", request.getContextPath() + "/myshop/booklist.go");
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
            return;
        }

        int qty = 1;
        try {
            qty = Integer.parseInt(qtyStr);
            if (qty < 1) qty = 1;  // 최소 수량 1
        } catch (NumberFormatException e) {
            qty = 1;
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

        // 4) 도서 정보 및 수량 request에 저장
        request.setAttribute("book", book);
        request.setAttribute("qty", qty);

        // 5) view 지정 (결제 페이지 JSP)
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/myshop/payment.jsp");
    }
}
