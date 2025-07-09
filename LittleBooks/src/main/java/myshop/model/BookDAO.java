package myshop.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import myshop.domain.BookVO;
import myshop.domain.CartVO;
import myshop.domain.CategoryVO;
import myshop.domain.ReviewVO;
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

	// 책번호 시퀀스 채번 (DAO에서 구현)
	int getBookseq() throws SQLException;

	// 책 정보를 tbl_book 테이블에 insert
	int bookInsert(BookVO bvo) throws SQLException;

	// 책 정렬
	List<BookVO> selectBooksByCategorySorted(String category, String sort) throws SQLException;
	

	List<BookVO> searchBooks(String searchType, String searchWord) throws SQLException;

	int getTotalPage(Map<String, String> paraMap) throws SQLException;

	List<myshop.domain.BookVO> selectBookPaging(Map<String, String> paraMap) throws SQLException;

	int getTotalBookCount(Map<String, String> paraMap) throws SQLException;

	

	// 책 정렬
	List<BookVO> selectAllBooksSorted(String sort) throws SQLException;
	
	// 청소년 권장 도서 (메인)
	List<BookVO> selectBooksBySeqArray(int[] seqArr) throws SQLException;

	// best, new 도서 조회
	List<BookVO> selectBooksBySpec(int snum) throws SQLException;

	// 장바구니에 담기
	int addCart(Map<String, String> paraMap) throws SQLException;

	// 장바구니 목록 조회
	List<CartVO> selectProductCart(String userid) throws SQLException;

	// 장바구니에 담긴 책 가격 총합
	Map<String, Integer> selectCartSumPrice(String userid) throws SQLException;

	// 장바구니 테이블에서 특정제품을 장바구니에서 비우기
	int delCart(String cartseq) throws SQLException;

	// 장바구니 테이블에서 특정제품의 주문량 변경시키기
	int updateCart(Map<String, String> paraMap) throws SQLException;

	// 로그인한 사용자가 특정 제품을 구매했는지 여부를 알아오는 것. 
	boolean isOrder(Map<String, String> paraMap) throws SQLException;

	// 특정 사용자가 특정 제품에 대해 상품후기를 입력하기(insert)
	int addReview(ReviewVO reviewVO) throws SQLException;

	// 특정 제품의 사용후기를 조회하기(select) 
	List<ReviewVO> reviewList(String fk_bookseq) throws SQLException;

	// 특정 제품의 사용후기를 삭제하기 
	int reviewDel(String review_seq) throws SQLException;

	// 특정 제품의 사용후기를 수정하기(update)
	int reviewUpdate(Map<String, String> paraMap) throws SQLException;

	// tbl_orderdetail 시퀀스 번호 채번 
	int get_seq_orderdetail() throws SQLException;
	// tbl_order 테이블에 insert
	int orderAdd(Map<String, Object> paraMap) throws SQLException;

	// 주문한 책 정보 가져오기 
	List<BookVO> getBookList(String bseq) throws SQLException;

	// 사용자 포인트 내역 업데이트 
	int updateUserPoint(String userid, int newPoint) throws SQLException;

	
	
	//나의 카테고리별 주문 통계정보 알아오기
	List<Map<String, String>> myPurchase_byCategory(String userid)throws SQLException;

	
	//나의 월별 카테고리별 주문 통계정보 알아오기 
	List<Map<String, String>> myPurchase_byMonth_byCategory(String userid)throws SQLException;

	
	//관리자(admin) 전용 카테고리별 월별 매출 통계 
	List<Map<String, String>> adminCategorySalesByMonth()throws SQLException;

	 // 총 매출액 조회 
	int getTotalSales()throws SQLException;

	
	
	// 매출 상세 리스트 조회 
	List<Map<String, Object>> getSalesList()throws SQLException;
	
	
	//회원별 누적 구매 금액
	List<Map<String, Object>> getUserTotalSpentList()throws SQLException;

	
	//최근 30일 일별 매출 통계
	List<Map<String, String>> getDailySalesLast30Days()throws SQLException;
	
	
	
	
	
	
}
