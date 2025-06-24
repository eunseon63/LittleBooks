package myshop.domain;

public class SpecVO {

	private int snum; // 스펙 번호
	private String sname; // 스펙 이름
	
	public SpecVO() {}
	
	public SpecVO(int snum, String sname) {
	
		this.snum = snum;
		this.sname = sname;
	}

	public int getSnum() {
		return snum;
	}
	public void setSnum(int snum) {
		this.snum = snum;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	
}
