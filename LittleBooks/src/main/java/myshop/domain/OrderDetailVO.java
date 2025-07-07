

package myshop.domain;

import member.domain.MemberVO;

public class OrderDetailVO {
	private String odrseq; 		  // 주문상세코드
    private String fk_ordercode;  // 주문번호 (외래키)
    private int fk_bookseq;       // 주문한 책 번호 (bookseq)
    private int oqty;             // 수량
    private int odrprice;         // 개별 가격 (책당 가격, 수량 아님!)
    private String deliverdate;   // 주문날짜
    private String deliverstatus; // 주문상태 
    
    private BookVO book;
    private MemberVO member;

	public BookVO getBook() {
		return book;
	}

	public void setBook(BookVO book) {
		this.book = book;
	}
	

	public MemberVO getMember() {
		return member;
	}

	public void setMember(MemberVO member) {
		this.member = member;
	}

	public String getOdrseq() {
		return odrseq;
	}

	public void setOdrseq(String odrseq) {
		this.odrseq = odrseq;
	}

	public String getFk_ordercode() {
		return fk_ordercode;
	}

	public void setFk_ordercode(String fk_ordercode) {
		this.fk_ordercode = fk_ordercode;
	}

	public int getFk_bookseq() {
		return fk_bookseq;
	}

	public void setFk_bookseq(int fk_bookseq) {
		this.fk_bookseq = fk_bookseq;
	}

	public int getOqty() {
		return oqty;
	}

	public void setOqty(int oqty) {
		this.oqty = oqty;
	}

	public int getOdrprice() {
		return odrprice;
	}

	public void setOdrprice(int odrprice) {
		this.odrprice = odrprice;
	}

	public String getDeliverdate() {
		return deliverdate;
	}

	public void setDeliverdate(String deliverdate) {
		this.deliverdate = deliverdate;
	}

	public String getDeliverstatus() {
		return deliverstatus;
	}

	public void setDeliverstatus(String deliverstatus) {
		this.deliverstatus = deliverstatus;
	}

	public int getBookseq() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}

