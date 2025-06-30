package myshop.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import myshop.domain.CategoryVO;
import myshop.domain.PublishVO;
import myshop.domain.BookVO;
import myshop.domain.SpecVO;

public class BookDAO_imple implements BookDAO {

    private DataSource ds;  
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public BookDAO_imple() {
        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/semiproject");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    
	// 사용한 자원을 반납하는 close() 메소드 생성하기
	private void close() {
		try {
			if(rs    != null) {rs.close();	  rs=null;}
			if(pstmt != null) {pstmt.close(); pstmt=null;}
			if(conn  != null) {conn.close();  conn=null;}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

    // 카테고리별 책 목록 조회 
    @Override
    public List<BookVO> selectBooksByCategory(String category) throws SQLException {
        List<BookVO> list = new ArrayList<>();

        try {
            conn = ds.getConnection();

            String sql;
            if ("all".equals(category)) {
                sql = "SELECT b.bookseq, b.bname, b.bcontent, b.price, b.bqty, b.author, b.bimage, " +
                      "b.fk_publishseq, b.fk_categoryseq, b.binputdate, b.fk_snum, c.categoryname " +
                      "FROM tbl_book b JOIN tbl_category c ON b.fk_categoryseq = c.categoryseq";
                pstmt = conn.prepareStatement(sql);
            } else {
                sql = "SELECT b.bookseq, b.bname, b.bcontent, b.price, b.bqty, b.author, b.bimage, " +
                      "b.fk_publishseq, b.fk_categoryseq, b.binputdate, b.fk_snum, c.categoryname " +
                      "FROM tbl_book b JOIN tbl_category c ON b.fk_categoryseq = c.categoryseq " +
                      "WHERE c.categoryname = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, category);
            }

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

                java.sql.Date binputDate = rs.getDate("binputdate");
                if (binputDate != null) {
                    book.setBinputdate(binputDate.toString());
                } else {
                    book.setBinputdate(null);
                }

                book.setFk_snum(rs.getInt("fk_snum"));

                CategoryVO cvo = new CategoryVO();
                cvo.setCategoryseq(rs.getInt("fk_categoryseq"));
                cvo.setCategoryname(rs.getString("categoryname"));
                book.setCvo(cvo);

                SpecVO spvo = new SpecVO();
                spvo.setSnum(rs.getInt("fk_snum"));
                book.setSpvo(spvo);

                list.add(book);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
            if (conn != null) try { conn.close(); } catch (Exception e) {}
        }

        return list;
    }

    // 도서 상세 정보 조회
	@Override
	public BookVO selectOneBookByBookseq(String bookseq) throws SQLException {
		BookVO book = null;

	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        conn = ds.getConnection();
	        String sql = "SELECT B.*, C.categoryname, P.pname "
	                   + "FROM tbl_book B "
	                   + "JOIN tbl_category C ON B.fk_categoryseq = C.categoryseq "
	                   + "JOIN tbl_publish P ON B.fk_publishseq = P.publishseq "
	                   + "WHERE B.bookseq = ?";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, bookseq);

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            book = new BookVO();
	            book.setBookseq(rs.getInt("bookseq"));
	            book.setBname(rs.getString("bname"));
	            book.setAuthor(rs.getString("author"));
	            book.setPrice(rs.getInt("price"));
	            book.setBqty(rs.getInt("bqty"));
	            book.setBimage(rs.getString("bimage"));
	            book.setBinputdate(rs.getString("binputdate"));
	            book.setBcontent(rs.getString("bcontent")); 


	            CategoryVO cvo = new CategoryVO();
	            cvo.setCategoryname(rs.getString("categoryname"));
	            book.setCvo(cvo);

	            PublishVO pvo = new PublishVO();
	            pvo.setPname(rs.getString("pname"));
	            book.setPvo(pvo);
	        }

	    } finally {
	    	 if (rs != null) try { rs.close(); } catch (Exception e) {}
	         if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
	         if (conn != null) try { conn.close(); } catch (Exception e) {}
	    }

	    return book;
	}

	// 카테고리 목록을 조회해오기
	@Override
	public List<CategoryVO> getCategoryList() throws SQLException {

		List<CategoryVO> categoryList = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select categoryseq, categoryname " 
					   + " from tbl_category "
					   + " order by categoryseq asc ";
			
			  pstmt = conn.prepareStatement(sql);
			  
			  rs = pstmt.executeQuery();
			  
			  while(rs.next()) {
				  
				  CategoryVO cvo = new CategoryVO();
				  cvo.setCategoryseq(rs.getInt("categoryseq"));
				  cvo.setCategoryname(rs.getString("categoryname"));
				  
				  categoryList.add(cvo);
				  
			  }
			 
		} finally {
			close();
		}
		
		return categoryList;
	}

	// SPEC 목록을 조회해오기
	@Override
	public List<SpecVO> getSpecList() throws SQLException {

		List<SpecVO> specList = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select snum, sname "
					   + " from tbl_spec "
					   + " order by snum asc ";
			
			  pstmt = conn.prepareStatement(sql);
			  
			  rs = pstmt.executeQuery();
			  
			  while(rs.next()) {
				  
				  SpecVO spvo = new SpecVO();
				  spvo.setSnum(rs.getInt("snum"));
				  spvo.setSname(rs.getString("sname"));
				  
				  specList.add(spvo);
				  
			  }
			 
		} finally {
			close();
		}
		
		return specList;
	}

	// 책번호 시퀀스 채번
	@Override
	public int getBookseq() throws SQLException {
	    int bnum = 0;

	    try {
	        conn = ds.getConnection();

	        String sql = "SELECT seq_book.NEXTVAL AS bookseq FROM dual";

	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery(); 

	        if (rs.next()) {
	            bnum = rs.getInt("bookseq");
	        }

	    } finally {
	        close();
	    }

	    return bnum;
	}

	// 책 정보를 tbl_book 테이블에 insert
	@Override
	public int bookInsert(BookVO bvo) throws SQLException {
	    int result = 0;

	    String sql = "INSERT INTO tbl_book "
	               + "(bookseq, bname, bcontent, price, bqty, author, bimage, fk_publishseq, fk_categoryseq, binputdate, fk_snum) "
	               + "VALUES (seq_book.nextval, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)";

	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, bvo.getBname());
	        pstmt.setString(2, bvo.getBcontent());
	        pstmt.setInt(3, bvo.getPrice());
	        pstmt.setInt(4, bvo.getBqty());
	        pstmt.setString(5, bvo.getAuthor());
	        pstmt.setString(6, bvo.getBimage());

	        if (bvo.getFk_publishseq() != 0) {
	            pstmt.setInt(7, bvo.getFk_publishseq());
	        } else {
	            pstmt.setNull(7, java.sql.Types.INTEGER);
	        }

	        if (bvo.getFk_categoryseq() != 0) {
	            pstmt.setInt(8, bvo.getFk_categoryseq());
	        } else {
	            pstmt.setNull(8, java.sql.Types.INTEGER);
	        }

	        if (bvo.getFk_snum() != 0) {
	            pstmt.setInt(9, bvo.getFk_snum());
	        } else {
	            pstmt.setNull(9, java.sql.Types.INTEGER);
	        }

	        result = pstmt.executeUpdate();
	    }

	    return result;
	}

	
}

