package myshop.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.domain.ReviewVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;


import java.sql.SQLIntegrityConstraintViolationException;

import org.json.JSONObject;

import java.sql.SQLException;

public class ReviewRegister extends AbstractController {

    private BookDAO bdao = null;

    public ReviewRegister() {
        bdao = new BookDAO_imple();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();

        if ("POST".equalsIgnoreCase(method)) {

            String ratingStr = request.getParameter("rating");
            String reviewComment = request.getParameter("contents");
            String fk_userid = request.getParameter("fk_userid");
            String fk_bookseqStr = request.getParameter("fk_bookseq");
            
            // 디버깅용 출력
            System.out.println("ReviewRegister execute() 진입");
            System.out.println("rating = " + ratingStr);
            System.out.println("contents = " + reviewComment);
            System.out.println("fk_userid = " + fk_userid);
            System.out.println("fk_bookseq = " + fk_bookseqStr);

            // null 체크 및 기본값 처리
            if (ratingStr == null || reviewComment == null || fk_userid == null || fk_bookseqStr == null) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("n", 0);
                request.setAttribute("json", jsonObj.toString());
                super.setRedirect(false);
                super.setViewPage("/WEB-INF/jsonview.jsp");
                return;
            }

            // 내용 줄바꿈 <br> 변환
            reviewComment = reviewComment.replaceAll("\r\n", "<br>");

            int rating = Integer.parseInt(ratingStr);
            int fk_bookseq = Integer.parseInt(fk_bookseqStr);

            ReviewVO reviewVO = new ReviewVO();
            reviewVO.setRating(rating);
            reviewVO.setReviewComment(reviewComment);
            reviewVO.setFk_userid(fk_userid);
            reviewVO.setFk_bookseq(fk_bookseq);

            int n = 0;

            try {
                n = bdao.addReview(reviewVO); // addReview는 int 반환(insert 성공=1, 실패=0)
            } catch (SQLIntegrityConstraintViolationException e) {
                e.printStackTrace();
                n = -1;  // unique 제약 위배 (중복 작성 시)
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("n", n);

            request.setAttribute("json", jsonObj.toString());

            super.setRedirect(false);
            super.setViewPage("/WEB-INF/jsonview.jsp");
        }
    }
}
