package myshop.domain;

public class CategoryVO {

	private int categoryseq;
	private String categoryname;
	
	public CategoryVO(){}
	
	public CategoryVO(int categoryseq, String categoryname) {
		super();
		this.categoryseq = categoryseq;
		this.categoryname = categoryname;
	}

	public int getCategoryseq() {
		return categoryseq;
	}

	public void setCategoryseq(int categoryseq) {
		this.categoryseq = categoryseq;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}
}
