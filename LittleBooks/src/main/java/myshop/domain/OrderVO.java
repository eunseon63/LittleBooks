package myshop.domain;

import java.util.List;

public class OrderVO {

    private String ordercode;         // 주문 번호 (merchant_uid)
    private String impUid;            // 아임포트 결제 고유값
    private int totalPrice;           // 총 결제 금액
    private int usePoint;             // 사용한 포인트 (추후 확장용)
    private String fk_userid;         // 주문자 아이디 (로그인된 사용자)

    // 배송 정보
    private String receiverName;       
    private String receiverPhone;      
    private String postcode;           
    private String address;           
    private String detailAddress;     
    private String extraAddress;      

    // 주문 상세 목록 (1:N 관계)
    private List<OrderDetailVO> orderDetailList;

    // 추가 배송 정보 필드 (DAO 쿼리에서 받는 alias)
    private String recipient;    // receiver_name AS recipient
    private String phone;        // receiver_phone AS phone
    private String memo;         // memo 컬럼 (빈 문자열로 처리)

    // 주문 날짜, 상태 등
    private String orderdate;
    private int orderstatus;

    // --- Getter & Setter ---

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public String getImpUid() {
        return impUid;
    }

    public void setImpUid(String impUid) {
        this.impUid = impUid;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getUsePoint() {
        return usePoint;
    }

    public void setUsePoint(int usePoint) {
        this.usePoint = usePoint;
    }

    public String getFk_userid() {
        return fk_userid;
    }

    public void setFk_userid(String fk_userid) {
        this.fk_userid = fk_userid;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getExtraAddress() {
        return extraAddress;
    }

    public void setExtraAddress(String extraAddress) {
        this.extraAddress = extraAddress;
    }

    public List<OrderDetailVO> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetailVO> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public int getOrderstatus() {
        return orderstatus;
    }

	public void setTotalprice(int int1) {
		// TODO Auto-generated method stub
		
	}

	public void setOrderstatus(int int1) {
		// TODO Auto-generated method stub
		
	}
	
}
