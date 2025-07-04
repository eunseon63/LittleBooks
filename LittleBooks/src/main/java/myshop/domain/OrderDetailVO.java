package myshop.domain;

public class OrderDetailVO {
    private String fk_ordercode;  // 주문번호 (외래키)
    private int fk_bookseq;       // 주문한 책 번호 (bookseq)
    private int qty;              // 수량
    private int price;            // 개별 가격 (책당 가격, 수량 아님!)

    // getter / setter
    public String getFk_ordercode() {
        return fk_ordercode;
    }

    public void setFk_ordercode(String fk_ordercode) {
        this.fk_ordercode = fk_ordercode;
    }

    // DAO에서 사용되는 메소드명에 맞게 getFkid()로 변경 (fk_bookseq를 리턴)
    public int getFkid() {
        return fk_bookseq;
    }

    public void setFkid(int fk_bookseq) {
        this.fk_bookseq = fk_bookseq;
    }

    // DAO에서 사용하는 getOrderQty() 메소드명에 맞게 변경
    public int getOrderQty() {
        return qty;
    }

    public void setOrderQty(int qty) {
        this.qty = qty;
    }

    // DAO에서 사용하는 getOrderPrice() 메소드명에 맞게 변경
    public int getOrderPrice() {
        return price;
    }

    public void setOrderPrice(int price) {
        this.price = price;
    }

	public int getOdrprice() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getOqty() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setOdrseq(int int1) {
		// TODO Auto-generated method stub
		
	}

	public void setFk_bookseq(int int1) {
		// TODO Auto-generated method stub
		
	}

	public void setOqty(int int1) {
		// TODO Auto-generated method stub
		
	}

	public void setOdrprice(int int1) {
		// TODO Auto-generated method stub
		
	}

	public void setDeliverstatus(int int1) {
		// TODO Auto-generated method stub
		
	}

	public void setDeliverdate(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setBname(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setBimage(String string) {
		// TODO Auto-generated method stub
		
	}
}
