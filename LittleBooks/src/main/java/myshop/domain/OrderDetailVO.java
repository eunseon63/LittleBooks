package myshop.domain;

public class OrderDetailVO {

    private int odrseq;             // 주문상세 시퀀스 (PK)
    private String fk_ordercode;    // 주문코드 (FK)
    private int fk_bookseq;         // 책 번호 (FK)
    private int oqty;               // 주문 수량
    private int odrprice;           // 주문 가격 (수량 단위 가격 * 수량 아님, 개별 단가)

    private int deliverstatus;      // 배송 상태
    private String deliverdate;     // 배송 날짜 (문자열 YYYY-MM-DD)
    
    private String bname;           // 책 이름 (조인 결과)
    private String bimage;          // 책 이미지 파일명 (조인 결과)

    // getter / setter

    public int getOdrseq() {
        return odrseq;
    }

    public void setOdrseq(int odrseq) {
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

    public int getDeliverstatus() {
        return deliverstatus;
    }

    public void setDeliverstatus(int deliverstatus) {
        this.deliverstatus = deliverstatus;
    }

    public String getDeliverdate() {
        return deliverdate;
    }

    public void setDeliverdate(String deliverdate) {
        this.deliverdate = deliverdate;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getBimage() {
        return bimage;
    }

    public void setBimage(String bimage) {
        this.bimage = bimage;
    }

    // 기존 DAO에서 쓰던 메서드명에 맞춘 메서드 추가 (호환용)
    public int getFkid() {
        return getFk_bookseq();
    }

    public void setFkid(int fk_bookseq) {
        setFk_bookseq(fk_bookseq);
    }

    public int getOrderQty() {
        return getOqty();
    }

    public void setOrderQty(int oqty) {
        setOqty(oqty);
    }

    public int getOrderPrice() {
        return getOdrprice();
    }

    public void setOrderPrice(int odrprice) {
        setOdrprice(odrprice);
    }
}
