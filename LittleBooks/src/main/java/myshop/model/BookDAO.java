package myshop.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import member.domain.MemberVO;
import myshop.domain.BookVO;
import myshop.domain.CategoryVO;
import myshop.domain.SpecVO;

public interface BookDAO {

	// 카테고리별 책 목록 조회 
	List<BookVO> selectBooksByCategory(String category) throws SQLException;
	
	// 도서 상세 정보 조회
	BookVO selectOneBookByBookseq(String bookseq) throws SQLException;

	// 카테고리 목록을 조회해오기
	List<CategoryVO> getCategoryList() throws SQLException;

	// SPEC 목록을 조회해오기
	List<SpecVO> getSpecList() throws SQLException;

	
	List<BookVO> searchBooks(String searchType, String searchWord) throws SQLException;

	int getTotalPage(Map<String, String> paraMap) throws SQLException;

	List<myshop.domain.BookVO> selectBookPaging(Map<String, String> paraMap) throws SQLException;

	int getTotalBookCount(Map<String, String> paraMap) throws SQLException;

	

}
