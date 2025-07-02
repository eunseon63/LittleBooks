package myshop.domain;

import java.sql.Date;

public class BookVO {
	private int bookseq;
	private String bname;
	private String bcontent;
	private int price;
	private int bqty;
	private String author;
	private String bimage;
	private int fk_publishseq;
	private int fk_categoryseq;
	private String binputdate;
	private int fk_snum;
	
	private CategoryVO cvo;
	private SpecVO spvo;
	private PublishVO pvo;
	
	private int totalPrice;
	
	public BookVO(){}
	
	public BookVO(int bookseq, String bname, String bcontent, int price, int bqty, String author, String bimage,
			int fk_publishseq, int fk_categoryseq, String binputdate, int fk_snum) {
		super();
		this.bookseq = bookseq;
		this.bname = bname;
		this.bcontent = bcontent;
		this.price = price;
		this.bqty = bqty;
		this.author = author;
		this.bimage = bimage;
		this.fk_publishseq = fk_publishseq;
		this.fk_categoryseq = fk_categoryseq;
		this.binputdate = binputdate;
		this.fk_snum = fk_snum;
	}

	public int getBookseq() {
		return bookseq;
	}
	public void setBookseq(int bookseq) {
		this.bookseq = bookseq;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getBcontent() {
		return bcontent;
	}
	public void setBcontent(String bcontent) {
		this.bcontent = bcontent;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getBqty() {
		return bqty;
	}
	public void setBqty(int bqty) {
		this.bqty = bqty;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getBimage() {
		return bimage;
	}
	public void setBimage(String bimage) {
		this.bimage = bimage;
	}
	public int getFk_publishseq() {
		return fk_publishseq;
	}
	public void setFk_publishseq(int fk_publishseq) {
		this.fk_publishseq = fk_publishseq;
	}
	public int getFk_categoryseq() {
		return fk_categoryseq;
	}
	public void setFk_categoryseq(int fk_categoryseq) {
		this.fk_categoryseq = fk_categoryseq;
	}
	public String getBinputdate() {
		return binputdate;
	}
	public void setBinputdate(String binputdate) {
		this.binputdate = binputdate;
	}
	public int getFk_snum() {
		return fk_snum;
	}
	public void setFk_snum(int fk_snum) {
		this.fk_snum = fk_snum;
	}
	public CategoryVO getCvo() {
		return cvo;
	}
	public void setCvo(CategoryVO cvo) {
		this.cvo = cvo;
	}
	public SpecVO getSpvo() {
		return spvo;
	}
	public void setSpvo(SpecVO spvo) {
		this.spvo = spvo;
	}
	public PublishVO getPvo() {
		return pvo;
	}
	public void setPvo(PublishVO pvo) {
		this.pvo = pvo;
	}

	public void setImage(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setPublisher(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setTitle(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setBookid(int int1) {
		// TODO Auto-generated method stub
		
	}

	public void setBookname(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setFilename(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setTotalPrice(int cqty) {
		totalPrice = price * cqty;
	}
	
	public int getTotalPrice() {
		return totalPrice;
	}


}
