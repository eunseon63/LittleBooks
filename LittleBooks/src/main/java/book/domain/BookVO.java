package book.domain;

public class BookVO {
	private int bookseq;  // 고유번호 추가
    private String title;
    private String author;
    private String publish;
    private String image;
	private String comment;

    public BookVO(int bookseq, String title, String author, String publish, String image, String comment) {
    	this.bookseq = bookseq;
        this.title = title;
        this.author = author;
        this.publish = publish;
        this.image = image;
        this.comment = comment;
    }
    
    public int getBookseq() {
        return bookseq;
    }

    public void setBookseq(int bookseq) {
        this.bookseq = bookseq;
    }
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

    public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}