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

        String sqlDetail = " INSERT INTO tbl_order_detail " +
                " (detail_id, fk_ordercode, bookseq, qty, unit_price) " +
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
}
