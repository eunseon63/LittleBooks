package myshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;

import common.controller.AbstractController;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;
import myshop.domain.BookVO;

public class Payment extends AbstractController {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String str_bookseq_join = request.getParameter("str_bookseq_join");
        String str_oqty_join = request.getParameter("str_oqty_join");
        String str_price_join = request.getParameter("str_price_join");
        String str_cartseq_join = request.getParameter("str_cartseq_join");

        if (str_bookseq_join == null || str_oqty_join == null || str_bookseq_join.trim().isEmpty() || str_oqty_join.trim().isEmpty()) {
            request.setAttribute("message", "잘못된 접근입니다.");
            request.setAttribute("loc", request.getContextPath() + "/myshop/booklist.go");
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
            return;
        }

        String[] bookseqArr = str_bookseq_join.split(",");
        String[] qtyArr = str_oqty_join.split(",");

        List<BookVO> bookList = new ArrayList<>();
        List<Integer> qtyList = new ArrayList<>();

        BookDAO dao = new BookDAO_imple();

        int totalPrice = 0;

        for (int i = 0; i < bookseqArr.length; i++) {
            String bookseqStr = bookseqArr[i].trim();
            int qty = 1;
            try {
                qty = Integer.parseInt(qtyArr[i].trim());
                if (qty < 1) qty = 1;
            } catch (Exception e) {}

            BookVO book = dao.selectOneBookByBookseq(bookseqStr);
            if (book == null) {
                request.setAttribute("message", "존재하지 않는 도서가 포함되어 있습니다.");
                request.setAttribute("loc", request.getContextPath() + "/myshop/booklist.go");
                super.setRedirect(false);
                super.setViewPage("/WEB-INF/msg.jsp");
                return;
            }

            // 총 가격 계산
            int bookTotalPrice = book.getPrice() * qty;
            book.setTotalPrice(bookTotalPrice);

            totalPrice += bookTotalPrice;

            bookList.add(book);
            qtyList.add(qty);
        }

        // request attribute 세팅
        request.setAttribute("bookList", bookList);
        request.setAttribute("qtyList", qtyList);
        request.setAttribute("sum_totalPrice", totalPrice); // 총 결제 금액
        request.setAttribute("str_bookseq_join", str_bookseq_join); // join 책 seq
        request.setAttribute("str_oqty_join", str_oqty_join); // join 주문 수량 
        request.setAttribute("str_price_join", str_price_join); // join 각 결제 금액
        request.setAttribute("str_cartseq_join", str_cartseq_join);

        // 결제 페이지로 forward
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/myshop/payment.jsp");
    }
}
