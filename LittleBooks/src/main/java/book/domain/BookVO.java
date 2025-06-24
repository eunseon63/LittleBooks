package book.domain;

public class BookVO {
    private String title;
    private String author;
    private String publish;
    private String image;
	private String comment;

    public BookVO(String title, String author, String publish, String image, String comment) {
        this.title = title;
        this.author = author;
        this.publish = publish;
        this.image = image;
        this.comment = comment;
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