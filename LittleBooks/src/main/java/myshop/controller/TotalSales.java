package myshop.controller;

import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class TotalSales extends AbstractController {
    private BookDAO bdao = null;

    public TotalSales() {
        bdao = new BookDAO_imple();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 총 매출액 조회
        int totalSales = bdao.getTotalSales();

        // 주문별 매출 상세 리스트 조회
        List<Map<String, Object>> salesList = bdao.getSalesList();

        request.setAttribute("totalSales", totalSales);
        request.setAttribute("salesList", salesList);

        super.setRedirect(false);
        super.setViewPage("/WEB-INF/myshop/totalSales.jsp");
    }
}
