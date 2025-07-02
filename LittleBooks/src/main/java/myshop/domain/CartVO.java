package myshop.domain;

public class CartVO {

	private int cartseq;
	private int fk_bookseq;
	private int cqty;
	private String fk_userid;
	
	private BookVO bvo;

	public CartVO() {}
	
	public CartVO(int cartseq, int fk_bookseq, int cqty, String fk_userid) {
		super();
		this.cartseq = cartseq;
		this.fk_bookseq = fk_bookseq;
		this.cqty = cqty;
		this.fk_userid = fk_userid;
	}

	public int getCartseq() {
		return cartseq;
	}

	public void setCartseq(int cartseq) {
		this.cartseq = cartseq;
	}

	public int getFk_bookseq() {
		return fk_bookseq;
	}

	public void setFk_bookseq(int fk_bookseq) {
		this.fk_bookseq = fk_bookseq;
	}

	public int getCqty() {
		return cqty;
	}

	public void setCqty(int cqty) {
		this.cqty = cqty;
	}

	public String getFk_userid() {
		return fk_userid;
	}

	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}

	public BookVO getBvo() {
		return bvo;
	}

	public void setBvo(BookVO bvo) {
		this.bvo = bvo;
	}
	
}
