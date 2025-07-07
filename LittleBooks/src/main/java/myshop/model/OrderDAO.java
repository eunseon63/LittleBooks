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

    // user 주문내역 받아오기
	// List<OrderDetailVO> getOrderDetailList(String ordercode, String userid) throws SQLException;

	OrderVO getOrderInfo(String ordercode, String userid) throws SQLException;

	List<OrderVO> getOrderListByUserid(String userid) throws SQLException;

    // 주문코드를 찾는 함수
	String selectOrdercode(String userid) throws SQLException;

	// 주문상세 정보 찾는 함수
	List<OrderDetailVO> selectAllDetail(String userid) throws SQLException;

	List<OrderDetailVO> getOrderDetailList(String ordercode, String userid) throws SQLException;


}
