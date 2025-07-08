package myshop.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class AdminCategorySalesByMonthJSON extends AbstractController {

    private BookDAO bdao;

    public AdminCategorySalesByMonthJSON() {
        bdao = new BookDAO_imple();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

        if (loginuser != null && "admin".equals(loginuser.getUserid())) {

            List<Map<String, String>> data = bdao.adminCategorySalesByMonth();

            JSONArray jsonArr = new JSONArray();

            if (data != null && !data.isEmpty()) {
                for (Map<String, String> map : data) {
                    JSONObject obj = new JSONObject();

                    obj.put("categoryname", map.get("categoryname") != null ? map.get("categoryname") : "");

                    // 월별 데이터 입력 (null 은 0 처리)
                    for (int i = 1; i <= 12; i++) {
                        String key = String.format("m_%02d", i);
                        String valStr = map.get(key);
                        int val = 0;
                        if (valStr != null) {
                            try {
                                val = Integer.parseInt(valStr);
                            } catch (NumberFormatException e) {
                                val = 0;
                            }
                        }
                        obj.put(key, val);
                    }

                    jsonArr.put(obj);
                }
            }

            request.setAttribute("json", jsonArr.toString());
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/jsonview.jsp");

        } else {
            // 로그인 안했거나 관리자가 아님
            request.setAttribute("message", "관리자만 접근할 수 있습니다.");
            request.setAttribute("loc", "javascript:history.back()");

            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
        }
    }
}
