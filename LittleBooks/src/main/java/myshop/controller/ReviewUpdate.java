package myshop.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class ReviewUpdate extends AbstractController {
    
    private BookDAO bdao = null;

    public ReviewUpdate() {
        bdao = new BookDAO_imple();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();
        
        if ("POST".equalsIgnoreCase(method)) {
            // POST 방식일 때만 처리
        	String ratingStr = request.getParameter("rating");  // 추가
            String reviewseq = request.getParameter("review_seq");
            String contents = request.getParameter("contents");

            System.out.println(">> 수정할 reviewseq: " + reviewseq);
            System.out.println(">> 수정할 내용: " + contents);

            // 입력한 내용에서 엔터는 <br>로 변환
            contents = contents.replaceAll("\r\n", "<br>");

            // ★ 여기 키 이름 주의 ★
            Map<String, String> paraMap = new HashMap<>();
            paraMap.put("reviewseq", reviewseq);   // DAO에서 요구하는 key는 reviewseq
            paraMap.put("contents", contents);
            if (ratingStr != null) {
                paraMap.put("rating", ratingStr);
            }
            
            int n = 0;
            try {
                n = bdao.reviewUpdate(paraMap);
            } catch (SQLException e) {
                e.printStackTrace(); // 오류 로그 출력
            }

            JSONObject jsobj = new JSONObject(); 
            jsobj.put("n", n);

            request.setAttribute("json", jsobj.toString());

            super.setRedirect(false);
            super.setViewPage("/WEB-INF/jsonview.jsp");
        } else {
            // GET 방식 접근 차단
            String message = "비정상적인 경로를 통해 들어왔습니다.!!";
            String loc = "javascript:history.back()";

            request.setAttribute("message", message);
            request.setAttribute("loc", loc);

            super.setViewPage("/WEB-INF/msg.jsp");
        }
    }
}
