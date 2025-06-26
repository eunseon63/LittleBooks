package myshop.domain;

public class PublishVO {
	
	private int publisheq;
	private String pname;
	private String plocation;
	
	public PublishVO(){}
	
	public PublishVO(int publisheq, String pname, String plocation) {
		super();
		this.publisheq = publisheq;
		this.pname = pname;
		this.plocation = plocation;
	}
	
	public int getPublisheq() {
		return publisheq;
	}
	public void setPublisheq(int publisheq) {
		this.publisheq = publisheq;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPlocation() {
		return plocation;
	}
	public void setPlocation(String plocation) {
		this.plocation = plocation;
	}
	
}
