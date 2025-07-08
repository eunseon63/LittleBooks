package myshop.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import member.domain.MemberVO;
import myshop.domain.CategoryVO;
import myshop.domain.OrderVO;
import myshop.domain.PublishVO;
import myshop.domain.ReviewVO;
import myshop.domain.BookVO;
import myshop.domain.CartVO;
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
	            book.setFk_snum(rs.getInt("fk_snum"));
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


	@Override
    public List<BookVO> searchBooks(String searchType, String searchWord) throws SQLException {
        List<BookVO> bookList = new ArrayList<>();

        String column = "bname"; // 기본 검색 기준

        if ("author".equalsIgnoreCase(searchType)) {
            column = "author";
        } else if ("publisher".equalsIgnoreCase(searchType)) {
            column = "fk_publishseq"; // 외래키지만 예시로 사용
        }

        try {
            conn = ds.getConnection();

            String sql = "SELECT bookseq, bname, bcontent, price, bqty, author, bimage, "
                       + "fk_publishseq, fk_categoryseq, fk_snum "
                       + "FROM tbl_book "
                       + "WHERE " + column + " LIKE ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchWord + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookVO bvo = new BookVO();
                bvo.setBookseq(rs.getInt("bookseq"));
                bvo.setBname(rs.getString("bname"));
                bvo.setBcontent(rs.getString("bcontent"));
                bvo.setPrice(rs.getInt("price"));
                bvo.setBqty(rs.getInt("bqty"));
                bvo.setAuthor(rs.getString("author"));
                bvo.setBimage(rs.getString("bimage"));
                bvo.setFk_publishseq(rs.getInt("fk_publishseq"));
                bvo.setFk_categoryseq(rs.getInt("fk_categoryseq"));
               
                bvo.setFk_snum(rs.getInt("fk_snum"));

                bookList.add(bvo);
            }

        } finally {
            close();
        }

        return bookList;
    }

	@Override
	public int getTotalPage(Map<String, String> paraMap) throws SQLException {
	    int totalPage = 0;

	    Connection conn = ds.getConnection();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        String sql = "SELECT CEIL(COUNT(*) / ?) AS totalPage "
	                   + "FROM tbl_book "
	                   + "WHERE 1=1 ";

	        String searchType = paraMap.get("searchType");
	        String searchWord = paraMap.get("searchWord");

	        if (searchType != null && !searchType.trim().isEmpty() && searchWord != null && !searchWord.trim().isEmpty()) {
	            sql += "AND " + searchType + " LIKE '%' || ? || '%' ";
	        }

	        pstmt = conn.prepareStatement(sql);

	        int idx = 1;
	        pstmt.setInt(idx++, Integer.parseInt(paraMap.get("sizePerPage")));

	        if (searchType != null && !searchType.isBlank() && searchWord != null && !searchWord.isBlank()) {
	            pstmt.setString(idx++, searchWord);
	        }

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            totalPage = rs.getInt("totalPage");
	        }

	    } finally {
	    	if (rs != null) try { rs.close(); } catch (SQLException e) {}
	        if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
	        if (conn != null) try { conn.close(); } catch (SQLException e) {} 
	    }

	    return totalPage;
	}


	@Override
	public int getTotalBookCount(Map<String, String> paraMap) throws SQLException {
	    int count = 0;

	    Connection conn = ds.getConnection();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        String sql = "SELECT COUNT(*) AS cnt FROM tbl_book WHERE 1=1 ";

	        String searchType = paraMap.get("searchType");
	        String searchWord = paraMap.get("searchWord");

	        if (searchType != null && !searchType.trim().isEmpty() && searchWord != null && !searchWord.trim().isEmpty()) {
	            sql += "AND " + searchType + " LIKE '%' || ? || '%' ";
	        }

	        pstmt = conn.prepareStatement(sql);

	        if (searchType != null && !searchType.isBlank() && searchWord != null && !searchWord.isBlank()) {
	            pstmt.setString(1, searchWord);
	        }

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            count = rs.getInt("cnt");
	        }

	    } finally {
	        close();
	    }

	    return count;
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

	// 책 목록 정렬 
	@Override
	public List<BookVO> selectBooksByCategorySorted(String category, String sort) throws SQLException {
		List<BookVO> list = new ArrayList<>();

	    try {
	        conn = ds.getConnection();

	        String sql = "SELECT b.*, c.categoryname " +
	                     "FROM tbl_book b JOIN tbl_category c ON b.fk_categoryseq = c.categoryseq " +
	                     "WHERE c.categoryname = ?";

	        if ("new".equals(sort)) {
	            sql += " ORDER BY b.binputdate DESC";
	        }

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, category);
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
	        close();
	    }

	    return list;

	}

	// 책 목록 정렬
	@Override
	public List<BookVO> selectAllBooksSorted(String sort) throws SQLException {
			List<BookVO> list = new ArrayList<>();

		    try {
		        conn = ds.getConnection();

		        String sql = "SELECT b.*, c.categoryname " +
		                     "FROM tbl_book b JOIN tbl_category c ON b.fk_categoryseq = c.categoryseq";

		        if ("new".equals(sort)) {
		            sql += " ORDER BY b.binputdate DESC";
		        }

		        pstmt = conn.prepareStatement(sql);
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
		        close();
		    }

		    return list;
	}

	// 청소년 권장 도서 (메인)
	@Override
	public List<BookVO> selectBooksBySeqArray(int[] seqArr) throws SQLException {
		List<BookVO> list = new ArrayList<>();

	    if (seqArr == null || seqArr.length == 0) return list;

	    try {
	        conn = ds.getConnection();

	        StringBuilder sql = new StringBuilder("SELECT b.*, c.categoryname ")
	            .append("FROM tbl_book b JOIN tbl_category c ON b.fk_categoryseq = c.categoryseq ")
	            .append("WHERE b.bookseq IN (");

	        for (int i = 0; i < seqArr.length; i++) {
	            sql.append("?");
	            if (i < seqArr.length - 1) sql.append(",");
	        }
	        sql.append(")");

	        pstmt = conn.prepareStatement(sql.toString());

	        for (int i = 0; i < seqArr.length; i++) {
	            pstmt.setInt(i + 1, seqArr[i]);
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
	        close();
	    }

	    return list;
	}

	

	
	@Override
	public List<BookVO> selectBookPaging(Map<String, String> paraMap) throws SQLException {
	    List<BookVO> bookList = new ArrayList<>();

	    Connection conn = ds.getConnection();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        String sql = "SELECT * FROM ( "
	                   + "SELECT ROWNUM AS rno, B.* FROM ( "
	                   + "  SELECT BOOKSEQ, BNAME, AUTHOR, BIMAGE, PRICE "
	                   + "  FROM tbl_book "
	                   + "  WHERE 1=1 ";

	        String searchType = paraMap.get("searchType");
	        String searchWord = paraMap.get("searchWord");

	        if (searchType != null && !searchType.trim().isEmpty() && searchWord != null && !searchWord.trim().isEmpty()) {
	            sql += "AND " + searchType + " LIKE '%' || ? || '%' ";
	        }

	        sql += "  ORDER BY BINPUTDATE DESC "
	             + ") B "
	             + ") "
	             + "WHERE rno BETWEEN ? AND ?";

	        pstmt = conn.prepareStatement(sql);

	        int idx = 1;
	        if (searchType != null && !searchType.isBlank() && searchWord != null && !searchWord.isBlank()) {
	            pstmt.setString(idx++, searchWord);
	        }

	        int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
	        int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));

	        int startRno = (currentShowPageNo - 1) * sizePerPage + 1;
	        int endRno = startRno + sizePerPage - 1;

	        pstmt.setInt(idx++, startRno);
	        pstmt.setInt(idx++, endRno);

	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            BookVO bvo = new BookVO();
	            bvo.setBookseq(rs.getInt("BOOKSEQ"));
	            bvo.setBname(rs.getString("BNAME"));
	            bvo.setAuthor(rs.getString("AUTHOR"));
	            bvo.setBimage(rs.getString("BIMAGE"));
	            bvo.setPrice(rs.getInt("PRICE"));
	            bookList.add(bvo);
	        }

	    } finally {
	        close();
	    }

	    return bookList;
	}

	// best, new 도서 조회
	@Override
	public List<BookVO> selectBooksBySpec(int snum) throws SQLException {
		List<BookVO> list = new ArrayList<>();

	    try {
	        conn = ds.getConnection();

	        String sql = "SELECT b.bookseq, b.bname, b.bcontent, b.price, b.bqty, b.author, b.bimage, "
	                   + "b.fk_publishseq, b.fk_categoryseq, b.binputdate, b.fk_snum, c.categoryname "
	                   + "FROM tbl_book b JOIN tbl_category c ON b.fk_categoryseq = c.categoryseq "
	                   + "WHERE b.fk_snum = ? "
	                   + "ORDER BY b.binputdate DESC";  // 최신순 정렬

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, snum);

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

	            list.add(book);
	        }
	    } finally {
	        close();
	    }

	    return list;
	}

	// 장바구니에 담기
	@Override
	public int addCart(Map<String, String> paraMap) throws SQLException {
	    int n = 0;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        conn = ds.getConnection();

	        String sql = "select cartseq from tbl_cart where fk_userid = ? and fk_bookseq = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, paraMap.get("fk_userid"));
	        pstmt.setString(2, paraMap.get("fk_bookseq"));
	        rs = pstmt.executeQuery();

	        int cqty = Integer.parseInt(paraMap.get("cqty"));

	        if(rs.next()) {
	            int cartseq = rs.getInt("cartseq");
	            rs.close();
	            pstmt.close();

	            sql = "update tbl_cart set cqty = cqty + ? where cartseq = ?";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, cqty);
	            pstmt.setInt(2, cartseq);
	            n = pstmt.executeUpdate();
	        } else {
	            rs.close();
	            pstmt.close();

	            sql = "insert into tbl_cart(cartseq, fk_userid, fk_bookseq, cqty) values(seq_cart.nextval, ?, ?, ?)";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, paraMap.get("fk_userid"));
	            pstmt.setInt(2, Integer.parseInt(paraMap.get("fk_bookseq")));
	            pstmt.setInt(3, cqty);
	            n = pstmt.executeUpdate();
	        }

	    } finally {
	        if(rs != null) try { rs.close(); } catch(Exception e) {}
	        if(pstmt != null) try { pstmt.close(); } catch(Exception e) {}
	        if(conn != null) try { conn.close(); } catch(Exception e) {}
	    }

	    return n;
	}

	// 장바구니 목록 조회
	@Override
	public List<CartVO> selectProductCart(String userid) throws SQLException {
	    
	    List<CartVO> cartList = new ArrayList<>(); // 빈 리스트로 초기화
	    
	    try {
	        conn = ds.getConnection();
	        
	        String sql =  " SELECT C.cartseq, C.fk_userid, C.fk_bookseq, C.cqty, B.bname, B.bimage, B.price, B.bqty "
	                    + " FROM "
	                    + " (select cartseq, fk_userid, fk_bookseq, cqty "
	                    + "  from tbl_cart "
	                    + "  where fk_userid = ?) C "
	                    + " JOIN tbl_book B "
	                    + " ON C.fk_bookseq = B.bookseq "
	                    + " ORDER BY C.cartseq DESC ";
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);
	        
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
	            
	            int cartseq = rs.getInt("cartseq");
	            String fk_userid = rs.getString("fk_userid");
	            int fk_bookseq = rs.getInt("fk_bookseq");
	            int cqty = rs.getInt("cqty"); // 장바구니 수량
	            String bname = rs.getString("bname");
	            String bimage = rs.getString("bimage");
	            int price = rs.getInt("price");
	            int bqty = rs.getInt("bqty");
	            
	            BookVO bvo = new BookVO();
	            bvo.setBookseq(fk_bookseq);
	            bvo.setBname(bname);
	            bvo.setBimage(bimage);
	            bvo.setPrice(price);
	            bvo.setBqty(bqty);

	            // 총 가격 = 가격 * 수량
	            bvo.setTotalPrice(price * cqty);
	            
	            CartVO cvo = new CartVO();
	            cvo.setCartseq(cartseq);
	            cvo.setFk_userid(fk_userid);
	            cvo.setFk_bookseq(fk_bookseq);
	            cvo.setCqty(cqty);
	            cvo.setBvo(bvo);
	            
	            cartList.add(cvo);
	        }
	        
	    } finally {
	        close();
	    }
	    
	    return cartList;
	}

	// 장바구니에 담긴 책 가격 총합
	@Override
	public Map<String, Integer> selectCartSumPrice(String userid) throws SQLException {

		Map<String, Integer> sumMap = new HashMap<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT NVL(SUM(C.cqty * B.price), 0) AS SUMTOTALPRICE "
					   + " FROM "
					   + " ( select fk_bookseq, cqty "
					   + "   from tbl_cart "
					   + "   where fk_userid = ? ) C "
					   + " JOIN tbl_book B "
					   + " ON C.fk_bookseq = B.bookseq ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			rs.next();
			
			sumMap.put("SUMTOTALPRICE", rs.getInt("SUMTOTALPRICE"));
			
		} finally {
			close();
		}
		
		return sumMap;
		
	}

	// 장바구니 테이블에서 특정제품을 장바구니에서 비우기
	@Override
	public int delCart(String cartseq) throws SQLException {
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " delete from tbl_cart "
					   + " where cartseq = to_number(?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cartseq);
						
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return n;	
	}

	// 장바구니 테이블에서 특정제품의 주문량 변경시키기
	@Override
	public int updateCart(Map<String, String> paraMap) throws SQLException {
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " update tbl_cart set cqty = to_number(?) "
					   + " where cartseq = to_number(?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("cqty"));
			pstmt.setString(2, paraMap.get("cartseq"));
						
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return n;			
	}

	// 로그인한 사용자가 특정 제품을 구매했는지 여부를 알아오는 것. 
	@Override
	public boolean isOrder(Map<String, String> paraMap) throws SQLException {
		boolean bool = false;
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT D.odrseq "
					+ " FROM tbl_order O "
					+ " JOIN tbl_orderdetail D ON O.ordercode = D.fk_ordercode "
					+ " WHERE O.fk_userid = ? "
					+ "  AND D.fk_bookseq = TO_NUMBER(?) ";
					

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("fk_userid"));     // 첫 번째 ? = 유저 아이디
			pstmt.setString(2, paraMap.get("fk_bookseq"));    // 두 번째 ? = 책 번호 (문자지만 SQL에서 TO_NUMBER로 처리)

			
			rs = pstmt.executeQuery();
			
			bool = rs.next();
					
		} finally {
			close();
		}
		
		return bool;
	}

	// 특정 사용자가 특정 제품에 대해 상품후기를 입력하기(insert)
	@Override
	public int addReview(ReviewVO reviewVO) throws SQLException {
	    int n = 0;

	    String sqlCheck = "SELECT COUNT(*) FROM tbl_review WHERE fk_userid = ? AND fk_bookseq = ?";
	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
	         
	        pstmtCheck.setString(1, reviewVO.getFk_userid());
	        pstmtCheck.setInt(2, reviewVO.getFk_bookseq());
	        
	        try (ResultSet rs = pstmtCheck.executeQuery()) {
	            if (rs.next()) {
	                int count = rs.getInt(1);
	                if (count > 0) {
	                    return -1;  // 이미 리뷰가 존재함 (중복)
	                }
	            }
	        }

	        // 중복 없으면 삽입 진행
	        String sqlInsert = "INSERT INTO tbl_review (reviewseq, reviewcomment, rating, writedate, fk_bookseq, fk_userid) "
	                         + "VALUES (seq_review.NEXTVAL, ?, ?, SYSDATE, ?, ?)";
	        try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
	            pstmtInsert.setString(1, reviewVO.getReviewComment());
	            pstmtInsert.setInt(2, reviewVO.getRating());
	            pstmtInsert.setInt(3, reviewVO.getFk_bookseq());
	            pstmtInsert.setString(4, reviewVO.getFk_userid());

	            n = pstmtInsert.executeUpdate();
	        }
	    }

	    return n;
	}


	// 특정 제품의 사용후기를 조회하기(select)
	@Override
	public List<ReviewVO> reviewList(String fk_bookseq) throws SQLException {
	    List<ReviewVO> reviewList = new ArrayList<>();

	    if (fk_bookseq == null || fk_bookseq.trim().isEmpty()) {
	        System.out.println(">> reviewList(): fk_bookseq가 null 또는 빈 문자열임");
	        return reviewList;
	    }

	    int bookseq = Integer.parseInt(fk_bookseq);

	    String sql = "SELECT r.reviewseq, r.reviewcomment, r.rating, r.writedate, r.fk_bookseq, r.fk_userid, m.name "
	               + "FROM tbl_review r JOIN tbl_member m ON r.fk_userid = m.userid "
	               + "WHERE r.fk_bookseq = ? ORDER BY r.writedate DESC";

	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, bookseq);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                ReviewVO review = new ReviewVO();
	                review.setReviewseq(rs.getInt("reviewseq"));
	                review.setReviewComment(rs.getString("reviewcomment"));
	                review.setRating(rs.getInt("rating")); // 별점 추가
	                review.setWriteDate(rs.getTimestamp("writedate"));
	                review.setFk_bookseq(rs.getInt("fk_bookseq"));
	                review.setFk_userid(rs.getString("fk_userid"));

	                MemberVO member = new MemberVO();
	                member.setName(rs.getString("name"));
	                review.setMvo(member);

	                reviewList.add(review);
	            }
	        }
	    }

	    return reviewList;
	}
	// 특정 제품의 사용후기를 삭제하기 
	@Override
	public int reviewDel(String reviewseq) throws SQLException {
	    int n = 0;

	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement("DELETE FROM tbl_review WHERE reviewseq = ?")) {

	        pstmt.setString(1, reviewseq);
	        n = pstmt.executeUpdate();
	    }

	    return n;
	}

	// 특정 제품의 사용후기를 수정하기(update)
	@Override
	public int reviewUpdate(Map<String, String> paraMap) throws SQLException {
	    int n = 0;

	    try {
	        conn = ds.getConnection();

	        String sql = "UPDATE tbl_review "
	                   + "SET reviewcomment = ?, "
	                   + "    rating = ?, "
	                   + "    writedate = SYSDATE "
	                   + "WHERE reviewseq = ?";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, paraMap.get("contents"));
	        pstmt.setInt(2, Integer.parseInt(paraMap.get("rating")));  // rating 파라미터 추가
	        pstmt.setInt(3, Integer.parseInt(paraMap.get("reviewseq")));

	        n = pstmt.executeUpdate();

	    } finally {
	        close();
	    }

	    return n;
	}

	// tbl_orderdetail 시퀀스 번호 채번 
	@Override
	public int get_seq_orderdetail() throws SQLException {

		int seq = 0;
		
		try {
			 conn = ds.getConnection();
				 
			 String sql = " select seq_orderdetail.nextval AS seq "
			 		    + " from dual";
				 
			 pstmt = conn.prepareStatement(sql);
				 
			 rs = pstmt.executeQuery();
				 
			 rs.next();
				 
			 seq = rs.getInt("seq");
				 
			} finally {
			  close();
		}
			
		return seq;
		
	}


	// tbl_order 테이블에 insert, tbl_cart delete
	public int orderAdd(Map<String,Object> paraMap) throws SQLException {
	    
	    int result = 0;
	    
	    String odrcode = (String) paraMap.get("odrcode");
	    String userid = (String) paraMap.get("userid");
	    String usepointStr = (String) paraMap.get("usepoint");
	    int usepoint = (usepointStr == null || usepointStr.isEmpty()) ? 0 : Integer.parseInt(usepointStr);
	    String sum_totalPriceStr = (String) paraMap.get("sum_totalPrice");
	    int totalPrice = Integer.parseInt(sum_totalPriceStr);
	    
	    String receiverName = (String) paraMap.get("receiver_name"); // 배송정보가 있다면
	    String receiverPhone = (String) paraMap.get("receiver_phone");
	    String postcode = (String) paraMap.get("postcode");
	    String address = (String) paraMap.get("address");
	    String detailAddress = (String) paraMap.get("detail_address");
	    String extraAddress = (String) paraMap.get("extra_address");
	    String impUid = (String) paraMap.get("imp_uid"); // 결제 고유번호
	    
	    String[] pnum_arr = (String[]) paraMap.get("pnum_arr");
	    
	    String[] oqty_arr = (String[]) paraMap.get("oqty_arr");
	    String[] totalPrice_arr = (String[]) paraMap.get("totalPrice_arr");
	    String[] cartseq_arr = (String[]) paraMap.get("cartseq_arr");
	    
	    try {
	        conn = ds.getConnection();
	        conn.setAutoCommit(false);
	        
	        // 1) tbl_order insert
	        String sqlOrder = "INSERT INTO tbl_order (ORDERCODE, ORDERDATE, USEPOINT, TOTALPRICE, FK_USERID, RECEIVER_NAME, RECEIVER_PHONE, POSTCODE, ADDRESS, DETAIL_ADDRESS, EXTRA_ADDRESS, IMP_UID) " +
	                          "VALUES (?, SYSDATE, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        pstmt = conn.prepareStatement(sqlOrder);
	        pstmt.setString(1, odrcode);
	        pstmt.setInt(2, usepoint);
	        pstmt.setInt(3, totalPrice);
	        pstmt.setString(4, userid);
	        pstmt.setString(5, receiverName);
	        pstmt.setString(6, receiverPhone);
	        pstmt.setString(7, postcode);
	        pstmt.setString(8, address);
	        pstmt.setString(9, detailAddress);
	        pstmt.setString(10, extraAddress);
	        pstmt.setString(11, impUid);
	        
	        pstmt.executeUpdate();
	        
	        // 2) tbl_orderdetail insert (여러 건)
	        String sqlDetail = "INSERT INTO tbl_orderdetail (ODRSEQ, FK_BOOKSEQ, FK_ORDERCODE, OQTY, ODRPRICE, DELIVERSTATUS, DELIVERDATE) " +
	                           "VALUES (seq_orderdetail.nextval, ?, ?, ?, ?, 0, NULL)";
	        pstmt = conn.prepareStatement(sqlDetail);
	        
	        for (int i=0; i<pnum_arr.length; i++) {
	            int bookseq = Integer.parseInt(pnum_arr[i]);
	            int oqty = Integer.parseInt(oqty_arr[i]);
	            int odrprice = Integer.parseInt(totalPrice_arr[i]);
	            
	            pstmt.setInt(1, bookseq);
	            pstmt.setString(2, odrcode);
	            pstmt.setInt(3, oqty);
	            pstmt.setInt(4, odrprice);
	            
	            pstmt.addBatch();
	        }
	        
	        pstmt.executeBatch();
	        pstmt.close();
	        
	        // 장바구니에서 삭제 (필요한 경우만)
	        if (cartseq_arr != null && cartseq_arr.length > 0) {
	            StringBuilder sb = new StringBuilder();
	            sb.append("DELETE FROM tbl_cart WHERE cartseq IN (");

	            for (int i = 0; i < cartseq_arr.length; i++) {
	                sb.append("?");
	                if (i < cartseq_arr.length - 1) sb.append(", ");
	            }
	            sb.append(")");

	            pstmt = conn.prepareStatement(sb.toString());
	            for (int i = 0; i < cartseq_arr.length; i++) {
	                pstmt.setInt(i + 1, Integer.parseInt(cartseq_arr[i]));
	            }
	            pstmt.executeUpdate();
	            pstmt.close();
	        }

	        conn.commit();
	        result = 1;
	    } catch(SQLException e) {
	        if(conn != null) {
	            conn.rollback();
	        }
	        e.printStackTrace();
	        throw e;
	    } finally {
	        if(pstmt != null) pstmt.close();
	        if(conn != null) conn.close();
	    }
	    
	    return result;
	}

	// 주문한 책 정보 가져오기 
	@Override
	public List<BookVO> getBookList(String bseq) throws SQLException {
	    List<BookVO> bookList = new ArrayList<>();
	    
	    try {
	        conn = ds.getConnection();

	        // bseq 가 "5,3,7" 같은 문자열이라 가정하고, IN절에 넣기 위해 '5','3','7' 형태로 변환
	        // 만약 bseq에 이미 쿼리용으로 변환된 문자열이 들어온다면 그대로 쓰면 됨
	        String[] bseqArr = bseq.split(",");
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < bseqArr.length; i++) {
	            sb.append("?");
	            if (i < bseqArr.length - 1) sb.append(",");
	        }
	        String sql = "SELECT BOOKSEQ, BNAME, BCONTENT, PRICE, BQTY, AUTHOR, BIMAGE, FK_PUBLISHSEQ, FK_CATEGORYSEQ, BINPUTDATE, FK_SNUM " +
	                     "FROM tbl_book WHERE BOOKSEQ IN (" + sb.toString() + ")";
	        
	        pstmt = conn.prepareStatement(sql);
	        
	        for (int i = 0; i < bseqArr.length; i++) {
	            pstmt.setInt(i + 1, Integer.parseInt(bseqArr[i].trim()));
	        }
	        
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	            BookVO book = new BookVO();
	            book.setBookseq(rs.getInt("BOOKSEQ"));
	            book.setBname(rs.getString("BNAME"));
	            book.setBcontent(rs.getString("BCONTENT"));
	            book.setPrice(rs.getInt("PRICE"));
	            book.setBqty(rs.getInt("BQTY"));
	            book.setAuthor(rs.getString("AUTHOR"));
	            book.setBimage(rs.getString("BIMAGE"));
	            book.setFk_publishseq(rs.getInt("FK_PUBLISHSEQ"));
	            book.setFk_categoryseq(rs.getInt("FK_CATEGORYSEQ"));
	            book.setBinputdate(rs.getDate("BINPUTDATE").toString());
	            book.setFk_snum(rs.getInt("FK_SNUM"));
	            
	            bookList.add(book);
	        }
	    } finally {
	        close(); // BookDAO_imple에 이미 있는 자원 반납 메소드 호출
	    }
	    
	    return bookList;
	}

	// 사용자 포인트 내역 업데이트 
	@Override
	public int updateUserPoint(String userid, int newPoint) throws SQLException {
	    
		int result = 0;
	    String sql = "UPDATE tbl_member SET point = ? WHERE userid = ?";
	    
	    try {
	        conn = ds.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, newPoint);
	        pstmt.setString(2, userid);
	        
	        result = pstmt.executeUpdate();
	    } finally {
	        if (pstmt != null) pstmt.close();
	        if (conn != null) conn.close();
	    }
	    
	    return result;
	    
	}


	@Override
	public List<Map<String, String>> myPurchase_byCategory(String userid) throws SQLException {
		List<Map<String, String>> myPurchase_map_List = new ArrayList<>();
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " WITH O AS ( "
	         		+ "    SELECT ordercode "
	         		+ "    FROM tbl_order "
	         		+ "    WHERE fk_userid = ? "
	         		+ " ), "
	         		+ " OD AS ( "
	         		+ "    SELECT fk_ordercode, fk_bookseq, oqty, odrprice "
	         		+ "    FROM tbl_orderdetail "
	         		+ " ) "
	         		+ " SELECT "
	         		+ "    C.categoryname, "
	         		+ "    COUNT(C.categoryname) AS cnt, "
	         		+ "    SUM(OD.oqty * OD.odrprice) AS sumpay, "
	         		+ "    ROUND( "
	         		+ "        SUM(OD.oqty * OD.odrprice) / "
	         		+ "        ( "
	         		+ "            SELECT SUM(OD2.oqty * OD2.odrprice) "
	         		+ "            FROM O "
	         		+ "            JOIN OD OD2 ON O.ordercode = OD2.fk_ordercode "
	         		+ "        ) * 100, "
	         		+ "        2\r\n"
	         		+ "    ) AS sumpay_pct "
	         		+ " FROM O "
	         		+ " JOIN OD ON O.ordercode = OD.fk_ordercode "
	         		+ " JOIN tbl_book B ON OD.fk_bookseq = B.bookseq "
	         		+ " JOIN tbl_category C ON B.fk_categoryseq = C.categoryseq "
	         		+ " GROUP BY C.categoryname "
	         		+ " ORDER BY 3 DESC ";
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, userid);
	         
	         rs = pstmt.executeQuery();
	                  
	         while(rs.next()) {
	            String categoryname = rs.getString("CATEGORYNAME");
	            String cnt = rs.getString("CNT");
	            String sumpay = rs.getString("SUMPAY");
	            String sumpay_pct = rs.getString("SUMPAY_PCT");
	            
	            Map<String, String> map = new HashMap<>();
	            map.put("categoryname", categoryname);
	            map.put("cnt", cnt);
	            map.put("sumpay", sumpay);
	            map.put("sumpay_pct", sumpay_pct);
	            
	            myPurchase_map_List.add(map);
	         } // end of while----------------------------------
	         
	      } finally {
	         close();
	      }
	      
	      return myPurchase_map_List;
	}

	
	

	//나의 카테고리별 월별 주문 알아오기
	@Override
	public List<Map<String, String>> myPurchase_byMonth_byCategory(String userid) throws SQLException {

	    List<Map<String, String>> myPurchase_map_List = new ArrayList<>();

	    try {
	        conn = ds.getConnection();

	        String sql = " WITH "
	                + " O AS (SELECT ordercode, orderdate "
	                + "       FROM tbl_order "
	                + "       WHERE fk_userid = ? "
	                + "     ), "
	                + " OD AS (SELECT fk_ordercode, fk_bookseq, oqty, odrprice "
	                + "        FROM tbl_orderdetail "
	                + "       ) "
	                + " SELECT C.CATEGORYNAME "
	                + "      , COUNT(*) AS CNT "
	                + "      , SUM(OD.oqty * OD.odrprice) AS SUMPAY "
	                + "      , ROUND(SUM(OD.oqty * OD.odrprice) / (SELECT SUM(OD.oqty * OD.odrprice) FROM O JOIN OD ON O.ordercode = OD.fk_ordercode) * 100, 2) AS SUMPAY_PCT "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '01' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_01 "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '02' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_02 "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '03' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_03 "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '04' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_04 "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '05' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_05 "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '06' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_06 "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '07' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_07 "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '08' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_08 "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '09' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_09 "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '10' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_10 "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '11' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_11 "
	                + "      , SUM(CASE WHEN TO_CHAR(O.orderdate, 'MM') = '12' THEN OD.oqty * OD.odrprice ELSE 0 END) AS m_12 "
	                + " FROM O "
	                + " JOIN OD ON O.ordercode = OD.fk_ordercode "
	                + " JOIN tbl_book B ON OD.fk_bookseq = B.bookseq "
	                + " JOIN tbl_category C ON B.fk_categoryseq = C.categoryseq "
	                + " GROUP BY C.CATEGORYNAME "
	                + " ORDER BY SUMPAY DESC";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);

	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Map<String, String> map = new HashMap<>();

	            map.put("categoryname", rs.getString("CATEGORYNAME"));
	            map.put("cnt", rs.getString("CNT"));
	            map.put("sumpay", rs.getString("SUMPAY"));
	            map.put("sumpay_pct", rs.getString("SUMPAY_PCT"));
	            map.put("m_01", rs.getString("m_01"));
	            map.put("m_02", rs.getString("m_02"));
	            map.put("m_03", rs.getString("m_03"));
	            map.put("m_04", rs.getString("m_04"));
	            map.put("m_05", rs.getString("m_05"));
	            map.put("m_06", rs.getString("m_06"));
	            map.put("m_07", rs.getString("m_07"));
	            map.put("m_08", rs.getString("m_08"));
	            map.put("m_09", rs.getString("m_09"));
	            map.put("m_10", rs.getString("m_10"));
	            map.put("m_11", rs.getString("m_11"));
	            map.put("m_12", rs.getString("m_12"));

	            myPurchase_map_List.add(map);
	        }

	    } finally {
	        close();
	    }

	    return myPurchase_map_List;
	}
	
	
	
	
	
	//관리자(admin) 전용 카테고리별 월별 매출 통계 
	@Override
	public List<Map<String, String>> adminCategorySalesByMonth() throws SQLException {
	    List<Map<String, String>> list = new ArrayList<>();
	    try {
	        conn = ds.getConnection();

	        String sql =
	            " WITH O AS ( " +
	            "     SELECT ordercode, TO_CHAR(orderdate, 'MM') AS order_month " +
	            "     FROM tbl_order " +
	            " ), " +
	            " OD AS ( " +
	            "     SELECT fk_ordercode, fk_bookseq, oqty, odrprice " +
	            "     FROM tbl_orderdetail " +
	            " ) " +
	            " SELECT C.categoryname, " +
	            "  SUM(CASE WHEN O.order_month='01' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_01, " +
	            "  SUM(CASE WHEN O.order_month='02' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_02, " +
	            "  SUM(CASE WHEN O.order_month='03' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_03, " +
	            "  SUM(CASE WHEN O.order_month='04' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_04, " +
	            "  SUM(CASE WHEN O.order_month='05' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_05, " +
	            "  SUM(CASE WHEN O.order_month='06' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_06, " +
	            "  SUM(CASE WHEN O.order_month='07' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_07, " +
	            "  SUM(CASE WHEN O.order_month='08' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_08, " +
	            "  SUM(CASE WHEN O.order_month='09' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_09, " +
	            "  SUM(CASE WHEN O.order_month='10' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_10, " +
	            "  SUM(CASE WHEN O.order_month='11' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_11, " +
	            "  SUM(CASE WHEN O.order_month='12' THEN OD.oqty*OD.odrprice ELSE 0 END) AS m_12 " +
	            " FROM O " +
	            " JOIN OD ON O.ordercode = OD.fk_ordercode " +
	            " JOIN tbl_book B ON OD.fk_bookseq = B.bookseq " +
	            " JOIN tbl_category C ON B.fk_categoryseq = C.categoryseq " +
	            " GROUP BY C.categoryname " +
	            " ORDER BY C.categoryname ";

	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Map<String, String> map = new HashMap<>();
	            map.put("categoryname", rs.getString("categoryname"));
	            for (int i = 1; i <= 12; i++) {
	                String key = String.format("m_%02d", i);
	                String val = rs.getString(key);
	                map.put(key, val != null ? val : "0");
	            }
	            list.add(map);
	        }
	    } finally {
	        close();
	    }
	    return list;
	}
	
	
	 // 총 매출액 조회 
	@Override
    public int getTotalSales() throws SQLException {
        int totalSales = 0;
        try {
            conn = ds.getConnection();
            String sql = " SELECT NVL(SUM(totalprice), 0) AS totalSales FROM tbl_order ";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalSales = rs.getInt("totalSales");
            }
        } finally {
            close();
        }
        return totalSales;
    }
	
	
	// 매출 상세 리스트 조회 
	@Override
    public List<Map<String, Object>> getSalesList() throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            conn = ds.getConnection();

            String sql =
                " SELECT o.ordercode, o.orderdate, o.totalprice, o.fk_userid, od.oqty, b.bname " +
                " FROM tbl_order o " +
                " JOIN tbl_orderdetail od ON o.ordercode = od.fk_ordercode " +
                " JOIN tbl_book b ON od.fk_bookseq = b.bookseq " +
                " ORDER BY o.orderdate DESC ";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("ordercode", rs.getString("ordercode"));
                map.put("orderdate", rs.getTimestamp("orderdate"));
                map.put("totalprice", rs.getInt("totalprice"));
                map.put("userid", rs.getString("fk_userid"));
                map.put("oqty", rs.getInt("oqty"));
                map.put("bname", rs.getString("bname"));

                list.add(map);
            }
        } finally {
            close();
        }
        return list;
    }

	@Override
	public List<Map<String, Object>> getUserTotalSpentList() throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();

	    try {
	        conn = ds.getConnection();

	        String sql = 
	            " SELECT fk_userid, SUM(totalprice) AS total_spent " +
	            " FROM tbl_order " +
	            " GROUP BY fk_userid " +
	            " ORDER BY total_spent DESC ";

	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("userid", rs.getString("fk_userid"));
	            map.put("totalSpent", rs.getInt("total_spent"));
	            list.add(map);
	        }
	    } finally {
	        close();
	    }

	    return list;
	}

	
	// 최근 30일 일별 매출 통계
	@Override
	public List<Map<String, String>> getDailySalesLast30Days() throws SQLException {
	    
		List<Map<String, String>> list = new ArrayList<>();

	    try {
	        conn = ds.getConnection();

	        String sql = 
	            " SELECT TO_CHAR(orderdate, 'YYYY-MM-DD') AS order_day, " +
	            "       SUM(totalprice) AS day_total " +
	            " FROM tbl_order " +
	            " WHERE orderdate >= SYSDATE - 30 " +
	            " GROUP BY TO_CHAR(orderdate, 'YYYY-MM-DD') " +
	            " ORDER BY order_day ASC ";

	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Map<String, String> map = new HashMap<>();
	            map.put("date", rs.getString("order_day"));         // key 변경
	            map.put("date_total", rs.getString("day_total"));    // key 변경
	            list.add(map);
	        }

	    } finally {
	        close();
	    }

	    return list;
	}



	
	


}

