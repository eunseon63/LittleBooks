package myshop.model;

import java.sql.SQLException;
import java.util.List;

import myshop.domain.OrderDetailVO;
import myshop.domain.OrderVO;

public interface OrderDAO {
	 // 주문 + 상세 등록
    int insertOrder(OrderVO ovo, List<OrderDetailVO> detailList) throws SQLException;

    // 주문번호 생성용 시퀀스
    String getOrderCode() throws SQLException;
}
