package myshop.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import member.domain.MemberVO;
import myshop.domain.OrderVO;
import util.security.AES256;
import util.security.SecretMyKey;
import myshop.domain.BookVO;
import myshop.domain.CategoryVO;
import myshop.domain.OrderDetailVO;

public class OrderDAO_imple implements OrderDAO {

    private DataSource ds;
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private AES256 aes;

    public OrderDAO_imple() {
        try {
         Context initContext = new InitialContext();
          Context envContext  = (Context)initContext.lookup("java:/comp/env");
          ds = (DataSource)envContext.lookup("jdbc/semiproject");
          
          aes = new AES256(SecretMyKey.KEY);
          // SecretMyKey.KEY 은 우리가 만든 암호화/복호화 키이다.
          
        } catch (NamingException e) {
            e.printStackTrace();
        } catch(UnsupportedEncodingException e) {
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

//    // user 주문내역 받아오기.   
//   @Override
//   public List<OrderDetailVO> getOrderDetailList(String ordercode, String userid) throws SQLException {
//      
//       List<OrderDetailVO> detailList = new ArrayList<>();
//
//       Connection conn = ds.getConnection();
//       
//       String sql = 
//           " SELECT od.odrseq, od.fk_bookseq, od.fk_ordercode, od.oqty, od.odrprice, " +
//           "       od.deliverstatus, TO_CHAR(od.deliverdate, 'YYYY-MM-DD') AS deliverdate, " +
//           "       b.bname, b.bimage " +
//           " FROM tbl_orderdetail od " +
//           " JOIN tbl_book b ON od.fk_bookseq = b.bookseq " +
//           " JOIN tbl_order o ON od.fk_ordercode = o.ordercode " +
//           " WHERE od.fk_ordercode = ? AND o.fk_userid = ?";
//
//       PreparedStatement pstmt = conn.prepareStatement(sql);
//       pstmt.setString(1, ordercode);
//       pstmt.setString(2, userid);
//
//       ResultSet rs = pstmt.executeQuery();
//       while (rs.next()) {
//           OrderDetailVO vo = new OrderDetailVO();
//           vo.setOdrseq(rs.getInt("odrseq"));
//           vo.setFk_bookseq(rs.getInt("fk_bookseq"));
//           vo.setFk_ordercode(rs.getString("fk_ordercode"));
//           vo.setOqty(rs.getInt("oqty"));
//           vo.setOdrprice(rs.getInt("odrprice"));
//           vo.setDeliverstatus(rs.getInt("deliverstatus"));
//           vo.setDeliverdate(rs.getString("deliverdate"));
//           vo.setBname(rs.getString("bname"));
//           vo.setBimage(rs.getString("bimage"));
//           
//           detailList.add(vo);
//       }
//
//       close();
//
//       return detailList;
//   }

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
    

   // 주문상세 정보 찾는 함수
   @Override
   public List<OrderDetailVO> selectAllDetail(String userid) throws SQLException {
       List<OrderDetailVO> orderDetailList = new ArrayList<>();

       try {
           conn = ds.getConnection();

           String sql = "SELECT "
                   + "    od.fk_ordercode, "
                   + "    od.deliverdate, "
                   + "    od.odrseq, "
                   + "    od.fk_bookseq AS bookseq, "
                   + "    od.oqty, "
                   + "    od.odrprice, "
                   + "    od.deliverstatus, "
                   + "    b.bname, "
                   + "    b.price, "
                   + "    b.bimage, "
                   + "    b.author "
                   + "FROM tbl_orderdetail od "
                   + "JOIN tbl_order o ON od.fk_ordercode = o.ordercode "
                   + "JOIN tbl_book b ON od.fk_bookseq = b.bookseq "
                   + "JOIN tbl_member m ON o.fk_userid = m.userid ";

           if (!"admin".equals(userid)) {
               sql += "WHERE m.userid = ? ";
           }

           pstmt = conn.prepareStatement(sql);

           if (!"admin".equals(userid)) {
               pstmt.setString(1, userid);
           }

           rs = pstmt.executeQuery();

           while (rs.next()) {
               OrderDetailVO detailVO = new OrderDetailVO();

               detailVO.setOdrseq(rs.getString("odrseq"));
               detailVO.setFk_ordercode(rs.getString("fk_ordercode"));
               detailVO.setFk_bookseq(rs.getInt("bookseq"));
               detailVO.setOqty(rs.getInt("oqty"));
               detailVO.setOdrprice(rs.getInt("odrprice"));
               detailVO.setDeliverdate(rs.getString("deliverdate"));
               detailVO.setDeliverstatus(rs.getString("deliverstatus"));

               BookVO book = new BookVO();
               book.setBookseq(rs.getInt("bookseq"));
               book.setBname(rs.getString("bname"));
               book.setPrice(rs.getInt("price"));
               book.setBimage(rs.getString("bimage"));
               book.setAuthor(rs.getString("author"));

               detailVO.setBook(book);

               orderDetailList.add(detailVO);
           }

       } finally {
           close();
       }

       return orderDetailList;
   } // end of public List<OrderDetailVO> selectAllDetail(String userid) throws SQLException {}-----------

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
	
	
	// 판매량순정렬
	@Override
	public List<BookVO> selectBooksOrderBySales(int categorySeq) throws SQLException {
	    List<BookVO> list = new ArrayList<>();

	    try {
	        conn = ds.getConnection();

	        String sql = "SELECT b.bookseq, b.bname, b.bcontent, b.price, b.bqty, b.author, b.bimage, " +
	                     "b.fk_publishseq, b.fk_categoryseq, b.binputdate, b.fk_snum, c.categoryname, " +
	                     "NVL(SUM(od.oqty), 0) AS total_sales " +
	                     "FROM tbl_book b " +
	                     "JOIN tbl_category c ON b.fk_categoryseq = c.categoryseq " +
	                     "LEFT JOIN tbl_orderdetail od ON b.bookseq = od.fk_bookseq " +
	                     "WHERE (? = 0 OR b.fk_categoryseq = ?) " +  // 0이면 전체 카테고리
	                     "GROUP BY b.bookseq, b.bname, b.bcontent, b.price, b.bqty, b.author, b.bimage, " +
	                     "b.fk_publishseq, b.fk_categoryseq, b.binputdate, b.fk_snum, c.categoryname " +
	                     "ORDER BY total_sales DESC";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, categorySeq);
	        pstmt.setInt(2, categorySeq);

	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            BookVO book = new BookVO();
	            book.setBookseq(rs.getInt("bookseq"));
	            book.setBname(rs.getString("bname"));
	            book.setBcontent(rs.getString("bcontent"));
	            book.setPrice(rs.getInt("price"));
	            book.setBqty(rs.getInt("bqty"));
	            book.setAuthor(rs.getString("author"));
	            book.setBimage(rs.getString("bimage"));
	            book.setFk_publishseq(rs.getInt("fk_publishseq"));
	            book.setFk_categoryseq(rs.getInt("fk_categoryseq"));
	            book.setBinputdate(rs.getString("binputdate"));
	            book.setFk_snum(rs.getInt("fk_snum"));

	            CategoryVO cvo = new CategoryVO();
	            cvo.setCategoryseq(rs.getInt("fk_categoryseq"));
	            cvo.setCategoryname(rs.getString("categoryname"));
	            book.setCvo(cvo);

	            // 판매량 저장할 필드가 BookVO에 있다면 세팅
	            book.setTotalSales(rs.getInt("total_sales"));

	            list.add(book);
	        }

	    } finally {
	        close();
	    }

	    return list;
	}

	// 주문자 아이디 찾기
	@Override
	public String selectUserid(String ordercode) throws SQLException {
		String userid = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select fk_userid "
					+ " from tbl_order "
					+ " where ordercode = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ordercode);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				userid = rs.getString("fk_userid");
			}
		} finally {
			close();
		}
		
		return userid;
	} // end of public String selectUserid(String ordercode) throws SQLException {}-------------------

	// 주문자 정보 찾기
	@Override
	public MemberVO selectOrderMember(Map<String, String> paraMap) throws SQLException {
	    MemberVO member = null;

	    try {
	        conn = ds.getConnection();

	        String sql = " SELECT m.userid, m.name, m.email, m.mobile, m.postcode, m.address, m.detailaddress, m.extraaddress " +
	                     " FROM tbl_order o " +
	                     " JOIN tbl_member m ON o.fk_userid = m.userid " +
	                     " WHERE o.ordercode = ? AND o.fk_userid = ? ";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, paraMap.get("ordercode"));
	        pstmt.setString(2, paraMap.get("userid"));

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            member = new MemberVO();
	            member.setUserid(rs.getString("userid"));
	            member.setName(rs.getString("name"));
	            member.setEmail(aes.decrypt(rs.getString("email")));
	            member.setMobile(aes.decrypt(rs.getString("mobile")));
	            member.setPostcode(rs.getString("postcode"));
	            member.setAddress(rs.getString("address"));
	            member.setDetailaddress(rs.getString("detailaddress"));
	            member.setExtraaddress(rs.getString("extraaddress"));
	        }
	    } catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}

	    return member;
	} // end of public MemberVO selectOrderMember(Map<String, String> paraMap) throws SQLException {]-----------

	// 주문번호로 전화번호 찾기
	@Override
	public String selectMobile(String userid) throws SQLException {
		String mobile = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select mobile "
					+ " from tbl_member join tbl_order "
					+ " on userid = fk_userid "
					+ " where userid = ? ";
			
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);
			
	        rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	mobile = aes.decrypt(rs.getString("mobile"));
	        }
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return mobile;
	} // end of public String selectMobile(String userid) throws SQLException {}-------------------

	// 배송상태 업데이트
	@Override
	public void updateDeliverstatus(String ordercode) throws SQLException {
		
		try {
			conn = ds.getConnection();
			
			String sql = " update tbl_orderdetail "
					+ " set deliverstatus = 1 "
					+ " where fk_ordercode = ? ";

			
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, ordercode);
			
	        pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
	} // end of public int updateDeliverstatus(String ordercode) throws SQLException {}-----------------

}


