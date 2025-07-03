package myshop.domain;

import java.util.Date;

import member.domain.MemberVO;

public class ReviewVO {
    private int reviewseq;
    private String reviewComment;
    private int rating;
    private Date writeDate;
    private int fk_bookseq;
    private String fk_userid;
    
	private MemberVO mvo;
	private BookVO bvo;
	
	public ReviewVO() { }

	public ReviewVO(int reviewseq, String reviewComment, int rating, Date writeDate, int fk_bookseq, String fk_userid,
			MemberVO mvo, BookVO bvo) {
		super();
		this.reviewseq = reviewseq;
		this.reviewComment = reviewComment;
		this.rating = rating;
		this.writeDate = writeDate;
		this.fk_bookseq = fk_bookseq;
		this.fk_userid = fk_userid;
		this.mvo = mvo;
		this.bvo = bvo;
	}

	public int getReviewseq() {
		return reviewseq;
	}

	public void setReviewseq(int reviewseq) {
		this.reviewseq = reviewseq;
	}

	public String getReviewComment() {
		return reviewComment;
	}

	public void setReviewComment(String reviewComment) {
		this.reviewComment = reviewComment;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

	public int getFk_bookseq() {
		return fk_bookseq;
	}

	public void setFk_bookseq(int fk_bookseq) {
		this.fk_bookseq = fk_bookseq;
	}

	public String getFk_userid() {
		return fk_userid;
	}

	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}

	public MemberVO getMvo() {
		return mvo;
	}

	public void setMvo(MemberVO mvo) {
		this.mvo = mvo;
	}

	public BookVO getPvo() {
		return bvo;
	}

	public void setPvo(BookVO pvo) {
		this.bvo = bvo;
	}

	
}
