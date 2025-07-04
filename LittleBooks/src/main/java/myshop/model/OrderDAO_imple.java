package myshop.model;

import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import myshop.domain.OrderVO;
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

        String sqlDetail = " INSERT INTO TBL_ORDERDETAIL " +
                " (odrseq, fk_ordercode, fk_bookseq, oqty, odrprice) " +
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
                pstmt.setInt(2, dvo.getFkid());
                pstmt.setInt(3, dvo.getOrderQty());
                pstmt.setInt(4, dvo.getOrderPrice());
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

    
    // user 주문내역 받아오기.	
	@Override
	public List<OrderDetailVO> getOrderDetailList(String ordercode, String userid) throws SQLException {
	    List<OrderDetailVO> detailList = new ArrayList<>();

	    Connection conn = ds.getConnection();
	    
	    String sql = 
	        " SELECT od.odrseq, od.fk_bookseq, od.fk_ordercode, od.oqty, od.odrprice, " +
	        "       od.deliverstatus, TO_CHAR(od.deliverdate, 'YYYY-MM-DD') AS deliverdate, " +
	        "       b.bname, b.bimage " +
	        " FROM tbl_orderdetail od " +
	        " JOIN tbl_book b ON od.fk_bookseq = b.bookseq " +
	        " JOIN tbl_order o ON od.fk_ordercode = o.ordercode " +
	        " WHERE od.fk_ordercode = ? AND o.fk_userid = ?";

	    PreparedStatement pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, ordercode);
	    pstmt.setString(2, userid);

	    ResultSet rs = pstmt.executeQuery();
	    while (rs.next()) {
	        OrderDetailVO vo = new OrderDetailVO();
	        vo.setOdrseq(rs.getInt("odrseq"));
	        vo.setFk_bookseq(rs.getInt("fk_bookseq"));
	        vo.setFk_ordercode(rs.getString("fk_ordercode"));
	        vo.setOqty(rs.getInt("oqty"));
	        vo.setOdrprice(rs.getInt("odrprice"));
	        vo.setDeliverstatus(rs.getInt("deliverstatus"));
	        vo.setDeliverdate(rs.getString("deliverdate"));
	        vo.setBname(rs.getString("bname"));
	        vo.setBimage(rs.getString("bimage"));
	        detailList.add(vo);
	    }

	    close();

	    return detailList;
	}

	@Override
	public OrderVO getOrderInfo(String ordercode, String userid) throws SQLException {
		
	    OrderVO vo = null;

	    Connection conn = ds.getConnection();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	    	String sql = " SELECT receiver_name AS recipient, receiver_phone AS phone, " +
	                "        postcode || ' ' || address || ' ' || detail_address || ' ' || extra_address AS address, " +
	                "        '' AS memo " +  // memo 컬럼이 없다면 빈 문자열로 대체
	                " FROM tbl_order " +
	                " WHERE ordercode = ? AND fk_userid = ?";


	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, ordercode);
	        pstmt.setString(2, userid);

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            vo = new OrderVO();
	            vo.setRecipient(rs.getString("recipient"));
	            vo.setPhone(rs.getString("phone"));
	            vo.setAddress(rs.getString("address"));
	            vo.setMemo(rs.getString("memo"));
	        }
	    } finally {
	        close(); // 반드시 connection, pstmt, rs 닫아주세요
	    }

	    return vo;
	}

	// 유저의 주문 목록 조회
    @Override
    public List<OrderVO> getOrderListByUserid(String userid) throws SQLException {
        List<OrderVO> orderList = new ArrayList<>();

        Connection conn = ds.getConnection();
        String sql = "SELECT ordercode, orderdate, totalprice, orderstatus FROM tbl_order WHERE fk_userid = ? ORDER BY orderdate DESC";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userid);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            OrderVO ovo = new OrderVO();
            ovo.setOrdercode(rs.getString("ordercode"));
            ovo.setOrderdate(rs.getString("orderdate"));
            ovo.setTotalprice(rs.getInt("totalprice"));
            ovo.setOrderstatus(rs.getInt("orderstatus"));
            orderList.add(ovo);
        }

        close();
        return orderList;
    }
}


	


