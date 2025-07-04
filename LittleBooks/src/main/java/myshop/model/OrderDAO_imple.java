package myshop.model;

import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import myshop.domain.OrderVO;
import myshop.domain.BookVO;
import myshop.domain.OrderDetailVO;

public class OrderDAO_imple implements OrderDAO {

    private DataSource ds;
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public OrderDAO_imple() {
        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/semiproject");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            if (rs    != null) { rs.close();    rs = null; }
            if (pstmt != null) { pstmt.close(); pstmt = null; }
            if (conn  != null) { conn.close();  conn = null; }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 주문 번호 시퀀스 가져오기
    @Override
    public String getOrderCode() throws SQLException {
        String orderCode = null;

        try {
            conn = ds.getConnection();
            String sql = "SELECT 'ORD' || TO_CHAR(SYSDATE, 'YYYYMMDD') || LPAD(seq_ordercode.NEXTVAL, 6, '0') AS ordercode FROM dual";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                orderCode = rs.getString("ordercode");
            }
        } finally {
            close();
        }

        return orderCode;
    }

    // 주문 + 주문상세 등록 
    @Override
    public int insertOrder(OrderVO ovo, List<OrderDetailVO> detailList) throws SQLException {
        int result = 0;

        String sqlOrder = " INSERT INTO tbl_order " +
                " (ordercode, orderdate, fk_userid, totalprice, usepoint, imp_uid, " +
                " receiver_name, receiver_phone, postcode, address, detail_address, extra_address) " +
                " VALUES (?, SYSDATE, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        String sqlDetail = " INSERT INTO tbl_order_detail " +
                " (odrseq, fk_ordercode, bookseq, oqty, unit_price) " +
                " VALUES (seq_order_detail.NEXTVAL, ?, ?, ?, ?) ";

        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 주문 등록
            pstmt = conn.prepareStatement(sqlOrder);
            pstmt.setString(1, ovo.getOrdercode());
            pstmt.setString(2, ovo.getFk_userid());
            pstmt.setInt(3, ovo.getTotalPrice());
            pstmt.setInt(4, ovo.getUsePoint());
            pstmt.setString(5, ovo.getImpUid());
            pstmt.setString(6, ovo.getReceiverName());
            pstmt.setString(7, ovo.getReceiverPhone());
            pstmt.setString(8, ovo.getPostcode());
            pstmt.setString(9, ovo.getAddress());
            pstmt.setString(10, ovo.getDetailAddress());
            pstmt.setString(11, ovo.getExtraAddress());

            result = pstmt.executeUpdate();
            pstmt.close(); 

            // 주문 상세 등록
            pstmt = conn.prepareStatement(sqlDetail);
            for (OrderDetailVO dvo : detailList) {
                pstmt.setString(1, ovo.getOrdercode());
                pstmt.setString(2, dvo.getOdrseq());
                pstmt.setInt(3, dvo.getOqty());
                pstmt.setInt(4, dvo.getOdrprice());
                pstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            close();
        }

        return result;
    }

    // 주문코드를 찾는 함수
	@Override
	public String selectOrdercode(String userid) throws SQLException {

		String ordercode = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select ordercode "
					+ " from tbl_order "
					+ " where fk_userid = ? ";
			
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
            	ordercode = rs.getString("ordercode");
            }
			
		} finally {
			close();
		}
		
		return ordercode;

	} // end of public String selectOrdercode(String userid) throws SQLException {}---------------

	// 주문상세 정보 찾는 함수
	@Override
	public List<OrderDetailVO> selectAllDetail() throws SQLException {
	    List<OrderDetailVO> orderDetailList = new ArrayList<>();
	    
	    try {
	        conn = ds.getConnection();
	        
	        String sql = "SELECT "
	                + "    od.fk_ordercode, "
	                + "    od.deliverdate, "
	                + "    od.odrseq, "
	                + "    b.bookseq, "
	                + "    od.oqty, "
	                + "    od.odrprice, "
	                + "    od.deliverstatus, "
	                + "    b.bname, "
	                + "    b.price, "
	                + "	   b.bimage AS bimage, "
	                + "    b.author "
	                + " FROM "
	                + "    tbl_orderdetail od "
	                + " JOIN "
	                + "    tbl_book b ON od.fk_bookseq = b.bookseq";
	        
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();
	        
	        // 데이터가 있을 경우 반복문 실행
	        while (rs.next()) {
	            OrderDetailVO detailVO = new OrderDetailVO();
	            
	            // OrderDetailVO에 데이터 세팅
	            detailVO.setOdrseq(rs.getString("odrseq"));              // 주문 상세 코드
	            detailVO.setFk_ordercode(rs.getString("fk_ordercode"));  // 주문 코드
	            detailVO.setFk_bookseq(rs.getInt("bookseq"));            // 책 번호
	            detailVO.setOqty(rs.getInt("oqty"));                     // 수량
	            detailVO.setOdrprice(rs.getInt("odrprice"));             // 개별 가격
	            detailVO.setDeliverdate(rs.getString("deliverdate"));    // 주문 일자
	            detailVO.setDeliverstatus(rs.getString("deliverstatus"));// 배송 상태
	            
	            // 책 정보 추가
	            BookVO book = new BookVO();
	            book.setBookseq(rs.getInt("bookseq"));
	            book.setBname(rs.getString("bname"));
	            book.setPrice(rs.getInt("price"));
	            book.setBimage(rs.getString("bimage"));
	            book.setAuthor(rs.getString("author"));
	            
	            // OrderDetailVO에 책 정보 설정
	            detailVO.setBook(book);
	            
	            // 리스트에 추가
	            orderDetailList.add(detailVO);
	        }
	        
	    } finally {
	        close();  // 자원 정리
	    }
	    
	    return orderDetailList;
	} // end of public List<OrderDetailVO> selectAllDetail() throws SQLException {}-----------

}
