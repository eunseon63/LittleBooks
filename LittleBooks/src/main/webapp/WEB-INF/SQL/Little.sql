-- 시퀀스 생성
CREATE SEQUENCE seq_book START WITH 101 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_category START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_image START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_review START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_cart START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_orderdetail START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_publish START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_spec START WITH 1 INCREMENT BY 1 NOCACHE;

-- 테이블 생성
CREATE TABLE tbl_member (
	userid VARCHAR2(30) PRIMARY KEY,
	passwd VARCHAR2(200) NOT NULL,
	mobile VARCHAR2(20),
	registerday DATE DEFAULT sysdate,
	point NUMBER DEFAULT 0,
	name VARCHAR2(20) NOT NULL,
	email VARCHAR2(200) UNIQUE,
	idle NUMBER(1) DEFAULT 0 NOT NULL CHECK (idle IN (0,1)),
	status NUMBER(1) DEFAULT 1 NOT NULL CHECK (status IN (0,1)),
	birthday DATE,
	lastpwdchangedate DATE DEFAULT sysdate,
	postcode VARCHAR2(5),
	address VARCHAR2(200),
	detailaddress VARCHAR2(200),
	extraaddress VARCHAR2(200)
);

CREATE TABLE tbl_loginhistory (
	logindate DATE DEFAULT sysdate NOT NULL,
	fk_userid VARCHAR2(30),
	CONSTRAINT FK_tbl_member_TO_tbl_loginhistory FOREIGN KEY (fk_userid) REFERENCES tbl_member(userid)
);

CREATE TABLE tbl_spec (
	snum NUMBER(8) PRIMARY KEY,
	sname VARCHAR2(100) NOT NULL UNIQUE
);

CREATE TABLE tbl_category (
	categoryseq NUMBER PRIMARY KEY,
	categoryname VARCHAR2(30) NOT NULL
);

CREATE TABLE tbl_publish (
	publishseq NUMBER PRIMARY KEY,
	pname VARCHAR2(30) NOT NULL,
	plocation VARCHAR2(30)
);

CREATE TABLE tbl_book (
	bookseq NUMBER PRIMARY KEY,
	bname VARCHAR2(100) NOT NULL,
	bcontent VARCHAR2(4000) NOT NULL,
	price NUMBER NOT NULL,
	bqty NUMBER NOT NULL,
	author VARCHAR2(30) NOT NULL,
	bimage VARCHAR2(100) NOT NULL,
	fk_publishseq NUMBER,
	fk_categoryseq NUMBER,
	binputdate DATE DEFAULT sysdate,
	snum NUMBER(8),
	CONSTRAINT FK_tbl_publish_TO_tbl_book FOREIGN KEY (fk_publishseq) REFERENCES tbl_publish(publishseq),
	CONSTRAINT FK_tbl_category_TO_tbl_book FOREIGN KEY (fk_categoryseq) REFERENCES tbl_category(categoryseq),
	CONSTRAINT FK_tbl_spec_TO_tbl_book FOREIGN KEY (snum) REFERENCES tbl_spec(snum)
);

CREATE TABLE tbl_image (
	imgfileno NUMBER PRIMARY KEY,
	fk_bookseq NUMBER NOT NULL,
	imgfilename VARCHAR2(100) NOT NULL,
	CONSTRAINT FK_tbl_book_TO_tbl_image FOREIGN KEY (fk_bookseq) REFERENCES tbl_book(bookseq)
);

CREATE TABLE tbl_review (
	reviewseq NUMBER PRIMARY KEY,
	comment VARCHAR2(4000) NOT NULL,
	rating NUMBER NOT NULL,
	writedate DATE DEFAULT sysdate,
	fk_bookseq NUMBER,
	fk_userid VARCHAR2(30),
	CONSTRAINT FK_tbl_book_TO_tbl_review FOREIGN KEY (fk_bookseq) REFERENCES tbl_book(bookseq),
	CONSTRAINT FK_tbl_member_TO_tbl_review FOREIGN KEY (fk_userid) REFERENCES tbl_member(userid)
);

CREATE TABLE tbl_cart (
	cartseq NUMBER PRIMARY KEY,
	fk_bookseq NUMBER NOT NULL,
	cqty NUMBER,
	fk_userid VARCHAR2(30),
	CONSTRAINT FK_tbl_book_TO_tbl_cart FOREIGN KEY (fk_bookseq) REFERENCES tbl_book(bookseq),
	CONSTRAINT FK_tbl_member_TO_tbl_cart FOREIGN KEY (fk_userid) REFERENCES tbl_member(userid)
);

CREATE TABLE tbl_order (
	ordercode VARCHAR2(20) PRIMARY KEY,
	orderdate DATE DEFAULT sysdate NOT NULL,
	usepoint NUMBER,
	totalprice NUMBER,
	fk_userid VARCHAR2(30),
	CONSTRAINT FK_tbl_member_TO_tbl_order FOREIGN KEY (fk_userid) REFERENCES tbl_member(userid)
);

CREATE TABLE tbl_orderdetail (
	odrseq NUMBER PRIMARY KEY,
	fk_bookseq NUMBER NOT NULL,
	fk_ordercode VARCHAR2(20) NOT NULL,
	oqty NUMBER NOT NULL,
	odrprice NUMBER NOT NULL,
	deliverstatus NUMBER DEFAULT 1 NOT NULL CHECK (deliverstatus IN (0,1,2)),
	deliverdate DATE,
	CONSTRAINT FK_tbl_book_TO_tbl_orderdetail FOREIGN KEY (fk_bookseq) REFERENCES tbl_book(bookseq),
	CONSTRAINT FK_tbl_order_TO_tbl_orderdetail FOREIGN KEY (fk_ordercode) REFERENCES tbl_order(ordercode)
);

select *
from tbl_order;

select ordercode
from tbl_order
where fk_userid = 'parksy';

select odrseq, oqty, odrprice, deliverdate, deliverstatus
from tbl_orderdetail
where fk_ordercode = 20250703001;

select bookseq, bname, bcontent, price, bqty, author, bimage, fk_publishseq, fk_categoryseq, fk_snum
from tbl_book
where bookseq = 165;

INSERT INTO tbl_order (
    ordercode, orderdate, usepoint, totalprice, fk_userid,
    receiver_name, receiver_phone, postcode, address,
    detail_address, extra_address, imp_uid
) VALUES (
    '20250703009', SYSDATE, 0, 12000, 'jyc',
    '박서연', '01012345679', '01234', '서울시 강남구 역삼동',
    '테헤란로 123', '역삼', 'imp_20250703_dummy'
);
INSERT INTO tbl_orderdetail (
    odrseq, fk_bookseq, fk_ordercode, oqty, odrprice, deliverstatus, deliverdate
) VALUES (
    20, 156, '20250703009', 1, 12000, 1, SYSDATE
);

commit;

SELECT
    od.fk_ordercode AS order_code,                     -- 주문 코드
    od.deliverdate AS order_date,                       -- 주문 일자 (deliverdate)
    b.bookseq AS product_number,                        -- 제품 번호
    b.bname AS product_name,                           -- 제품명
    b.price AS sales_price,                            -- 판매가
    b.price * 0.1 AS points,                           -- 포인트 (가격의 10%로 계산한다고 가정)
    b.author AS author,                                -- 저자
    od.oqty AS order_quantity,                         -- 주문 수량
    od.oqty * b.price AS total_amount,                 -- 주문 총액 (수량 * 판매가)
    od.oqty * (b.price * 0.1) AS total_points,         -- 총 포인트 (수량 * 포인트)
    od.deliverstatus AS delivery_status,                -- 배송 상태
    b.bimage
FROM tbl_orderdetail od JOIN tbl_book b ON od.fk_bookseq = b.bookseq;           -- 책 정보와 조인


commit;

select *
from tbl_book;
