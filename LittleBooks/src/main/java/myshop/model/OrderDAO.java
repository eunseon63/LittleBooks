package myshop.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


import member.domain.MemberVO;

import myshop.domain.BookVO;

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

	// 주문자 아이디 찾기
	String selectUserid(String ordercode) throws SQLException;
	
	// 주문자 정보 찾기
	MemberVO selectOrderMember(Map<String, String> paraMap) throws SQLException;

	// 전화번호 찾기
	String selectMobile(String userid) throws SQLException;

   // 배송 정보 변경
   int updateDeliverStatus(String ordercode, int status) throws SQLException;

   // 한 주문 내역 조회
   List<OrderDetailVO> selectOneDetail(String ordercode) throws SQLException;
	// 배송상태 변경
	void updateDeliverstatus(String ordercode) throws SQLException;

	// 주문상세 정보 찾는 함수
	List<OrderDetailVO> selectAllDetail(String userid) throws SQLException;
	
	// 판매량순정렬
	List<BookVO> selectBooksOrderBySales(int categorySeq) throws SQLException;


	List<BookVO> selectAllBooksOrderBySales() throws SQLException;

	// 주문 기간별 상세 정보 찾는 함수
	List<OrderDetailVO> selectOrderDetailByPeriod(String userid, String startDateStr) throws SQLException;

}