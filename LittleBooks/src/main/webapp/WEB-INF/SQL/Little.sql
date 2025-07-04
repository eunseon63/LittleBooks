 
 select *
 from TBL_BOOK
 
  select *
 from TBL_Member
 
 desc  TBL_BOOK;
 
 select *
 from tbl_order;
 
 select *
 from tbl_orderdetail;
 
 
 INSERT INTO tbl_order (
    ordercode, orderdate, usepoint, totalprice, fk_userid,
    receiver_name, receiver_phone, postcode, address,
    detail_address, extra_address, imp_uid
) VALUES (
    '20250703003', SYSDATE, 0, 12000, 'calendar0205',
    '김준형', '01045816645', '06307', '경기 의정부시 용민로 10',
    '1107-303', '용현동, 탑석센트럴자이', 'imp_20250716_dummy'
);

INSERT INTO tbl_orderdetail (
    odrseq, fk_bookseq, fk_ordercode, oqty, odrprice, deliverstatus, deliverdate
) VALUES (
    3, 183, '20250703003', 1, 12000, 1, 25/07/04
);














