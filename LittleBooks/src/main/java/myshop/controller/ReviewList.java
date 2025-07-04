package myshop.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;
import myshop.domain.ReviewVO;

public class ReviewList extends AbstractController {

    private BookDAO bdao = null;

    public ReviewList() {
        bdao = new BookDAO_imple();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fk_bookseq = request.getParameter("fk_bookseq"); // 책 번호

        List<ReviewVO> reviewList = bdao.reviewList(fk_bookseq);

        JSONArray jsArr = new JSONArray();

        if (reviewList != null && reviewList.size() > 0) {
            for (ReviewVO reviewVO : reviewList) {
                JSONObject jsobj = new JSONObject();

                jsobj.put("reviewseq", reviewVO.getReviewseq());
                jsobj.put("rating", reviewVO.getRating());  // 이거 꼭 추가!

                jsobj.put("fk_userid", reviewVO.getFk_userid());
                jsobj.put("name", reviewVO.getMvo() != null ? reviewVO.getMvo().getName() : "");
                jsobj.put("contents", reviewVO.getReviewComment());
                jsobj.put("writedate", reviewVO.getWriteDate() != null ? reviewVO.getWriteDate().toString() : "");

                jsArr.put(jsobj);
            }
        }

        String json = jsArr.toString();

        request.setAttribute("json", json);

        super.setRedirect(false);
        super.setViewPage("/WEB-INF/jsonview.jsp");
    }

}
