package common.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.domain.BookVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class IndexController extends AbstractController {

		@Override
		public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 청소년 권장 도서 bookseq 배열로 불러오기
        int[] youthSeqArr = {162, 183, 165, 169, 159, 164};
        

        BookDAO dao = new BookDAO_imple();
        List<BookVO> youthBooks = dao.selectBooksBySeqArray(youthSeqArr);
        
        // BEST 도서 (fk_snum = 2)
        List<BookVO> bestBooks = dao.selectBooksBySpec(2);

        // NEW 도서 (fk_snum = 3)
        List<BookVO> newBooks = dao.selectBooksBySpec(3);

        request.setAttribute("youthBooks", youthBooks);
        request.setAttribute("bestBooks", bestBooks);
        request.setAttribute("newBooks", newBooks);
        
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/index.jsp");
	}

}
