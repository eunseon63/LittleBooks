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

        // 1. 파라미터 받기
        String sum_totalPrice = request.getParameter("sum_totalPrice");
        String str_bookseq_join = request.getParameter("str_bookseq_join");
        String str_oqty_join = request.getParameter("str_oqty_join");
        String str_totalPrice_join = request.getParameter("str_price_join");
        String str_cartseq_join = request.getParameter("str_cartseq_join");
        String usepoint = request.getParameter("usepoint");

        String receiver_name = request.getParameter("receiver_name");
        String receiver_phone = request.getParameter("receiver_phone");
        String postcode = request.getParameter("receiver_postcode");
        String address = request.getParameter("receiver_address");
        String detail_address = request.getParameter("receiver_detail_address");
        String extra_address = request.getParameter("receiver_extra_address");
        String imp_uid = request.getParameter("imp_uid");

        // 2. 주문번호 생성
        String odrcode = getOdrcode();

        // 3. Map 데이터 생성
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

        paraMap.put("receiver_name", receiver_name);
        paraMap.put("receiver_phone", receiver_phone);
        paraMap.put("postcode", postcode);
        paraMap.put("address", address);
        paraMap.put("detail_address", detail_address);
        paraMap.put("extra_address", extra_address);
        paraMap.put("imp_uid", imp_uid);

        // 4. 주문 등록
        int isSuccess = bdao.orderAdd(paraMap);

        // 5. 포인트 처리 및 메일 발송
        if (isSuccess == 1) {
            int usedPoint = 0;
            if (usepoint != null && !usepoint.trim().isEmpty()) {
                usedPoint = Integer.parseInt(usepoint);
            }

            int totalPriceInt = Integer.parseInt(sum_totalPrice);
            int earnedPoint = (int) (totalPriceInt * 0.01);
            int newPoint = loginuser.getPoint() - usedPoint + earnedPoint;

            int updateResult = bdao.updateUserPoint(loginuser.getUserid(), newPoint);

            if (updateResult == 1) {
                loginuser.setPoint(newPoint);

                // 이메일 전송
                GoogleMail mail = new GoogleMail();
                List<BookVO> bookList = bdao.getBookList(str_bookseq_join);

                StringBuilder sb = new StringBuilder();
                sb.append("<div style='font-family: Arial, sans-serif; font-size: 15px; line-height: 1.6; color: #000000;'>");

                sb.append("<p style='font-size:18px; font-weight:bold; color:#2E86C1;'>주문이 완료되었습니다 🎉</p>");
                sb.append("<p>안녕하세요 <span style='font-weight:bold;'>").append(loginuser.getName()).append("</span>님,</p>");
                sb.append("<p>주문해주셔서 감사합니다.<br>아래는 주문하신 내역입니다.</p>");

                sb.append("<p><span style='font-weight:bold;'>주문코드번호:</span> <span style='color: #2874A6;'>")
                  .append(odrcode).append("</span></p>");

                sb.append("<table style='border-collapse: collapse; width: 100%; max-width: 600px;'>")
                  .append("<thead>")
                  .append("<tr style='background-color: #f2f2f2;'>")
                  .append("<th style='border: 1px solid #ddd; padding: 8px;'>도서명</th>")
                  .append("<th style='border: 1px solid #ddd; padding: 8px;'>수량</th>")
                  .append("</tr>")
                  .append("</thead><tbody>");

                String[] oqty_arr = str_oqty_join.split(",");
                for (int i = 0; i < bookList.size(); i++) {
                    sb.append("<tr>")
                      .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(bookList.get(i).getBname()).append("</td>")
                      .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(oqty_arr[i]).append("권</td>")
                      .append("</tr>");
                }

                sb.append("</tbody></table>");
                sb.append("<p style='margin-top: 20px;'>📦 배송은 영업일 기준 1~2일 내에 시작됩니다.</p>");
                sb.append("<p>언제든지 다시 찾아주세요.<br><strong>감사합니다.</strong></p>");
                sb.append("</div>");

                mail.sendmail_OrderFinish(loginuser.getEmail(), loginuser.getName(), sb.toString());
            }
        }

        // 6. JSON 응답
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"isSuccess\":" + isSuccess + "}");
    }
}
