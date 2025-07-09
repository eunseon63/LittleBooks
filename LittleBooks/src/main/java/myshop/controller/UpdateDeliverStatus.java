package myshop.controller;

import java.io.PrintWriter;
import java.util.HashMap;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.model.OrderDAO;
import myshop.model.OrderDAO_imple;
import net.nurigo.java_sdk.api.Message;

// 주문 상태 변경 처리
public class UpdateDeliverStatus extends AbstractController {

    OrderDAO odao = new OrderDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String ordercode = request.getParameter("ordercode");
        String status = request.getParameter("status");

        int result = odao.updateDeliverStatus(ordercode, Integer.parseInt(status));

        // 배송 상태가 "1"(배송중)일 때만 문자 발송
        if ("1".equals(status)) {
            String api_key = "NCSO1YI9QPAQI6IF";
            String api_secret = "OEYJH0XUO828W17WMZHKU6T2YPCLQNGN";

            // 유저아이디 찾는 메서드
            String userid = odao.selectUserid(ordercode);
            System.out.println(userid);

            // 전화번호 찾는 메서드
            String mobile = odao.selectMobile(userid);
            System.out.println(mobile);

            String smsContent = "LittleBooks 에서 주문하신 책 주문번호[" + ordercode + "] 를 우체국택배로 배송하였습니다.";

            Message coolsms = new Message(api_key, api_secret);

            HashMap<String, String> paraMap = new HashMap<>();
            paraMap.put("to", mobile); // 수신번호
            paraMap.put("from", "01093770449"); // 발신번호
            paraMap.put("type", "SMS"); // Message type
            paraMap.put("text", smsContent); // 문자내용
            paraMap.put("app_version", "JAVA SDK v2.2");

            // 메시지 전송
            coolsms.send(paraMap);
        }

        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (result > 0) {
            out.print("success");
        } else {
            out.print("fail");
        }

        out.close();
    }
}
