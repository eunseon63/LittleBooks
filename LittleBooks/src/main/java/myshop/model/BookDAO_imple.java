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
		
		try {
			conn = ds.getConnection();
			
			String sql = " select cartseq "
					   + " from tbl_cart "
					   + " where fk_userid = ? and fk_bookseq = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, paraMap.get("bnum"));
			
			rs = pstmt.executeQuery();
			
			// 있던 제품을 장바구니에 추가로 담음 
			if(rs.next()) {
				sql = " update tbl_cart set cqty = cqty + to_number(?) "
					+ " where cartseq = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, paraMap.get("oqty") );
				pstmt.setInt(2, rs.getInt("cartno") );
				
				n = pstmt.executeUpdate();
			}
			// 장바구니에 해당 제품을 처음 담음 
			else {
				sql = " insert into tbl_cart(cartseq, fk_userid, fk_bookseq, cqty) "
					+ " values(seq_cart.nextval, ?, ?, ?) ";
					
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, paraMap.get("userid"));
				pstmt.setInt(2, Integer.parseInt(paraMap.get("bnum")));
				pstmt.setInt(3, Integer.parseInt(paraMap.get("oqty")));
				
				n = pstmt.executeUpdate();
			}
			
		} finally {
			close();
		}
		
		return n;
		
	}


}

