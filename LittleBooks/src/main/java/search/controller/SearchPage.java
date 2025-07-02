package search.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.domain.BookVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class SearchPage extends AbstractController {

    BookDAO bdao = new BookDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 주소창 직접 접근 차단
        String referer = request.getHeader("Referer");
        if (referer == null) {
            super.setRedirect(true);
            super.setViewPage(request.getContextPath() + "/index.go");
            return;
        }

        // 파라미터 수집
        String sizePerPage = request.getParameter("sizePerPage");
        String currentShowPageNo = request.getParameter("currentShowPageNo");
        String searchType = request.getParameter("searchType");
        String searchWord = request.getParameter("searchWord");

        // 유효성 검사 및 기본값 설정
        if (searchType == null ||
            (!"bname".equals(searchType) && !"author".equals(searchType) && !"publisher".equals(searchType))) {
            searchType = "bname";
        }

        if (searchWord == null) {
            searchWord = "";
        }

        if (sizePerPage == null || (!"10".equals(sizePerPage) && !"5".equals(sizePerPage) && !"3".equals(sizePerPage))) {
            sizePerPage = "41";  // 모든 책 보기
        }

        if (currentShowPageNo == null) {
            currentShowPageNo = "1";
        }

        // searchType을 실제 DB 컬럼명으로 매핑
        String dbColumn = "BNAME";
        switch (searchType) {
            case "bname":
                dbColumn = "BNAME";
                break;
            case "author":
                dbColumn = "AUTHOR";
                break;
            case "publisher":
                dbColumn = "FK_PUBLISHSEQ"; // 출판사 번호 (DAO에서 join 필요)
                break;
        }

        // 파라미터 맵 구성
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("sizePerPage", sizePerPage);
        paraMap.put("currentShowPageNo", currentShowPageNo);
        paraMap.put("searchType", searchType);
        paraMap.put("searchWord", searchWord.trim());
        paraMap.put("dbColumn", dbColumn);

        // 총 페이지 수 계산
        int totalPage = bdao.getTotalPage(paraMap);

        try {
            int pageNoInt = Integer.parseInt(currentShowPageNo);
            if (pageNoInt > totalPage || pageNoInt <= 0) {
                currentShowPageNo = "1";
                paraMap.put("currentShowPageNo", currentShowPageNo);
            }
        } catch (NumberFormatException e) {
            currentShowPageNo = "1";
            paraMap.put("currentShowPageNo", currentShowPageNo);
        }

        // 도서 리스트 조회
        List<BookVO> bookList = bdao.selectBookPaging(paraMap);

        // request attribute 설정
        request.setAttribute("bookList", bookList);
        request.setAttribute("sizePerPage", sizePerPage);
        request.setAttribute("searchType", searchType);
        request.setAttribute("searchWord", searchWord);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentShowPageNo", currentShowPageNo);

        // 뷰 지정
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/search/searchPage.jsp");
    }
}
