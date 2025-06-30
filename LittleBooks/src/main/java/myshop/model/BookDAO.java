package myshop.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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

	// 책 정렬
	List<BookVO> selectBooksByCategorySorted(String category, String sort) throws SQLException;
	
	// 책 정렬
	List<BookVO> selectAllBooksSorted(String sort) throws SQLException;
	
	// 청소년 권장 도서 (메인)
	List<BookVO> selectBooksBySeqArray(int[] seqArr) throws SQLException;

	// best, new 도서 조회
	List<BookVO> selectBooksBySpec(int snum) throws SQLException;

}
