package myshop.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import login.controller.GoogleMail;
import member.domain.MemberVO;
import myshop.domain.BookVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderAdd extends AbstractController {

    private BookDAO bdao = null;

    public OrderAdd() {
        bdao = new BookDAO_imple();
    }

    private String getOdrcode() {
        Date now = new Date();
        SimpleDateFormat smdatefm = new SimpleDateFormat("yyyyMMdd");
        String today = smdatefm.format(now);

        int seq = 0;
        try {
            seq = bdao.get_seq_orderdetail();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "b" + today + "-" + seq;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("message", "비정상적인 경로로 접근하셨습니다.");
            request.setAttribute("loc", "javascript:history.back()");
            super.setViewPage("/WEB-INF/msg.jsp");
            super.setRedirect(false);
            return;
        }

        HttpSession session = request.getSession();
        MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

        if (loginuser == null) {
            response.sendRedirect(request.getContextPath() + "/login/login.go");
            return;
        }

        // === 1. 파라미터 받기 ===
        String sum_totalPrice = request.getParameter("sum_totalPrice");
        String str_bookseq_join = request.getParameter("str_bookseq_join");
        String str_oqty_join = request.getParameter("str_oqty_join");
        String str_totalPrice_join = request.getParameter("str_price_join");
        String str_cartseq_join = request.getParameter("str_cartseq_join");
        String usepoint = request.getParameter("usepoint");

        // 배송 정보
        String receiver_name = request.getParameter("receiver_name");
        String receiver_phone = request.getParameter("receiver_phone");
        String postcode = request.getParameter("receiver_postcode");
        String address = request.getParameter("receiver_address");
        String detail_address = request.getParameter("receiver_detail_address");
        String extra_address = request.getParameter("receiver_extra_address");
        String imp_uid = request.getParameter("imp_uid"); // 결제 고유번호

        // === 2. 주문번호 생성 ===
        String odrcode = getOdrcode();

        // === 3. Map 생성해서 DAO로 전달할 값 준비 ===
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("odrcode", odrcode);
        paraMap.put("userid", loginuser.getUserid());
        paraMap.put("sum_totalPrice", sum_totalPrice);
        paraMap.put("usepoint", usepoint);
        paraMap.put("pnum_arr", str_bookseq_join.split(","));
        paraMap.put("oqty_arr", str_oqty_join.split(","));
        paraMap.put("totalPrice_arr", str_totalPrice_join.split(","));

        if (str_cartseq_join != null && !str_cartseq_join.trim().isEmpty()) {
            paraMap.put("cartseq_arr", str_cartseq_join.split(","));
        }

        // 배송 관련 정보도 추가
        paraMap.put("receiver_name", receiver_name);
        paraMap.put("receiver_phone", receiver_phone);
        paraMap.put("postcode", postcode);
        paraMap.put("address", address);
        paraMap.put("detail_address", detail_address);
        paraMap.put("extra_address", extra_address);
        paraMap.put("imp_uid", imp_uid);

        // === 4. DAO 호출 ===
        int isSuccess = bdao.orderAdd(paraMap);

        System.out.println("isSuccess : " + isSuccess);
        
        // === 5. 포인트 계산 및 세션 갱신 ===
        if (isSuccess == 1) {
            int usedPoint = 0;
            String usePointStr = request.getParameter("usepoint");
            if (usePointStr != null && !usePointStr.trim().isEmpty()) {
                usedPoint = Integer.parseInt(usePointStr);
            }

            int totalPriceInt = Integer.parseInt(sum_totalPrice);
            int earnedPoint = (int) (totalPriceInt * 0.1 / 100);

            int newPoint = loginuser.getPoint() - usedPoint + earnedPoint;

            int updateResult = bdao.updateUserPoint(loginuser.getUserid(), newPoint);
            
            if (updateResult == 1) {
                // DB 반영 성공 시 세션도 변경
                loginuser.setPoint(newPoint);
            GoogleMail mail = new GoogleMail();

            String bseq = str_bookseq_join;
            List<BookVO> bookList = bdao.getBookList(bseq);

            StringBuilder sb = new StringBuilder();
            sb.append("주문코드번호 : <span style='color: blue; font-weight: bold;'>" + odrcode + "</span><br><br>");
            sb.append("<주문상품><br>");

            String[] oqty_arr = str_oqty_join.split(",");
            for (int i = 0; i < bookList.size(); i++) {
                sb.append(bookList.get(i).getBname()).append("&nbsp;")
                  .append(oqty_arr[i]).append("권<br>");
            }

            sb.append("<br>이용해 주셔서 감사합니다.");
            String emailContents = sb.toString();

            mail.sendmail_OrderFinish(loginuser.getEmail(), loginuser.getName(), emailContents);
        
            }
        }
        else {
        	System.out.println();
        }

        // === 7. 결과 전송 ===
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"isSuccess\":" + isSuccess + "}");
    }
}
