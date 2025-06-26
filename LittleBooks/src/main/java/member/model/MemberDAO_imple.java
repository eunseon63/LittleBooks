package member.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import member.domain.MemberVO;
import util.security.AES256;
import util.security.SecretMyKey;
import util.security.Sha256;

public class MemberDAO_imple implements MemberDAO {
	
	private DataSource ds;  // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다. 
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private AES256 aes;
	
	// 생성자
	public MemberDAO_imple() {
		
		try {
			Context initContext = new InitialContext();
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");
		    ds = (DataSource)envContext.lookup("jdbc/semiproject");
		    
		    aes = new AES256(SecretMyKey.KEY);
		    // SecretMyKey.KEY 은 우리가 만든 암호화/복호화 키이다.
		    
		} catch(NamingException e) {
			e.printStackTrace();
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	// 사용한 자원을 반납하는 close() 메소드 생성하기
	private void close() {
		try {
			if(rs    != null) {rs.close();	  rs=null;}
			if(pstmt != null) {pstmt.close(); pstmt=null;}
			if(conn  != null) {conn.close();  conn=null;}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}// end of private void close()---------------

	
   // ID 중복검사 (tbl_member 테이블에서 userid 가 존재하면 true 를 리턴해주고, 
   // userid 가 존재하지 않으면 false 를 리턴한다) 
   @Override
   public boolean idDuplicateCheck(String userid) throws SQLException {
      
      boolean isExists = false; // 리턴타입 불린타입
      
      try {
         
         conn = ds.getConnection();
         
         String sql = " select * from tbl_member "
                  + " where userid = ? ";
         
         pstmt = conn.prepareStatement(sql); // sql을 전달 및 실행해야할 우편배달부 생성
         pstmt.setString(1, userid);
         rs = pstmt.executeQuery(); // 리턴타입은 result set;
         
         isExists = rs.next(); // 행이 있으면 true (중복된 userid)
                   // 행이 없으면 false (사영가능한 userid)
         
      } finally {
         close();
      }
      
      return isExists;
   } // end of public boolean idDuplicateCheck(String userid) throws SQLException


   // 이메일 중복검사 (tbl_member 테이블에서 email이 존재하면 true 를 리턴해주고, 
   // email이 존재하지 않으면 false 를 리턴한다) 
   @Override
   public boolean emailDuplicateCheck(String email) throws SQLException {
      
      boolean isExists = false; // 리턴타입 불린타입
      
      try {
         
         conn = ds.getConnection();
         
         String sql = " select * from tbl_member "
                  + " where email = ? ";
         
         pstmt = conn.prepareStatement(sql); // sql을 전달 및 실행해야할 우편배달부 생성
         pstmt.setString(1, aes.encrypt(email));
         rs = pstmt.executeQuery(); // 리턴타입은 result set;
         
         isExists = rs.next(); // 행이 있으면 true (중복된 email)
                          // 행이 없으면 false (사영가능한 email)
         
      } catch(GeneralSecurityException | UnsupportedEncodingException e) {
            e.printStackTrace();
      } finally {
         close();
      }
      
      return isExists;
      
   } // end of public boolean emailDuplicateCheck(String email) throws SQLException


   // 회원가입을 해주는 메소드 (tbl_member 테이블에 insert)
   public int registerMember(MemberVO member) throws SQLException {
      
      int result = 0;
      
      try {      
         conn = ds.getConnection();
         String sql = " insert into tbl_member(userid, passwd, name, email, mobile, postcode, address, detailaddress, extraaddress, birthday) " 
                       + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
         pstmt = conn.prepareStatement(sql);
         
	         pstmt.setString(1, member.getUserid());
	         pstmt.setString(2, Sha256.encrypt(member.getPwd())); // sha256(단방향)은 static 타입. 암호화가 되어서 String으로 나온다
	         pstmt.setString(3, member.getName());
             pstmt.setString(4, aes.encrypt(member.getEmail()));  // 이메일을 AES256 알고리즘으로 양방향 암호화 시킨다.
             pstmt.setString(5, aes.encrypt(member.getMobile())); // 휴대폰을 AES256 알고리즘으로 양방향 암호화 시킨다.
             pstmt.setString(6, member.getPostcode());
             pstmt.setString(7, member.getAddress());
             pstmt.setString(8, member.getDetailaddress());
             pstmt.setString(9, member.getExtraaddress());
             pstmt.setString(10, member.getBirthday());
           
           
           //DML문이라서
           result = pstmt.executeUpdate();
           
           //성공 시 1이 반환
           
      }   catch(GeneralSecurityException | UnsupportedEncodingException e) {
         e.printStackTrace();
      }   finally {
         close();
      }
         
      return result;
      
   } // end of  public int registerMember(MemberVO member) throws SQLException
	
   
	// 로그인 함수
	@Override
	public MemberVO login(Map<String, String> paraMap) throws SQLException {
		
		MemberVO member = null;
		
		try {
			 conn = ds.getConnection();
			 
			 String sql = " SELECT userid, name, point, registerday, "
				 		+ "        pwdchangegap, "
				 		+ "        idle, email, mobile, postcode, address, detailaddress, extraaddress, "
				 		+ "        lastlogingap "
				 		+ " FROM "
				 		+ " ( "
				 		+ "   SELECT userid, name, point, registerday, "
				 		+ "          TRUNC( months_between(sysdate, lastpwdchangedate) ) AS pwdchangegap, "
				 		+ "          idle, email, mobile, postcode, address, detailaddress, extraaddress "
				 		+ "   FROM tbl_member "
				 		+ "   WHERE status = 1 AND userid = ? and passwd = ? "  
				 		+ " ) M "
				 		+ " CROSS JOIN "
				 		+ " ( "
				 		+ "   SELECT TRUNC( months_between(sysdate, MAX(logindate)) ) AS lastlogingap "
				 		+ "   FROM tbl_loginhistory "
				 		+ "   WHERE fk_userid = ? "
				 		+ " ) H ";
			 
			 pstmt = conn.prepareStatement(sql);
			 
			 pstmt.setString(1, paraMap.get("userid"));
			 pstmt.setString(2, Sha256.encrypt(paraMap.get("passwd")));
			 pstmt.setString(3, paraMap.get("userid"));
			 
			 rs = pstmt.executeQuery();
			 
			 if(rs.next()) {
				    member = new MemberVO();
				    
				    // 먼저 ResultSet에서 모든 값 읽기
				    String userid = rs.getString("userid");
				    String name = rs.getString("name");
				    int point = rs.getInt("point");
				    String registerday = rs.getString("registerday");
				    int pwdchangegap = rs.getInt("pwdchangegap");
				    int lastlogingap = rs.getInt("lastlogingap");
				    int idle = rs.getInt("idle");

				    String email = aes.decrypt(rs.getString("email"));
				    String mobile = aes.decrypt(rs.getString("mobile"));
				    String postcode = rs.getString("postcode");
				    String address = rs.getString("address");
				    String detailaddress = rs.getString("detailaddress");
				    String extraaddress = rs.getString("extraaddress");

				    // member 세팅
				    member.setUserid(userid);
				    member.setName(name);
				    member.setPoint(point);
				    member.setRegisterday(registerday);
				    if(pwdchangegap >= 3) {
				        member.setRequirePwdChange(true);
				    }

				    member.setEmail(email);
				    member.setMobile(mobile);
				    member.setPostcode(postcode);
				    member.setAddress(address);
				    member.setDetailaddress(detailaddress);
				    member.setExtraaddress(extraaddress);

				    if(lastlogingap < 12) {
				        sql = "insert into tbl_loginhistory(fk_userid) values(?)";
				        pstmt = conn.prepareStatement(sql);
				        pstmt.setString(1, userid);
				        pstmt.executeUpdate();
				    }
				    else {
				        member.setIdle(1);

				        if(idle == 0) {
				            sql = "update tbl_member set idle = 1 where userid = ?";
				            pstmt = conn.prepareStatement(sql);
				            pstmt.setString(1, userid);
				            pstmt.executeUpdate();
				        }
				    }
				}

			 
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return member;
	} // end of public MemberVO login()--------------------------------
	
	

	@Override
	public boolean emailDuplicateCheck(Map<String, String> paraMap) throws SQLException {
		
		boolean isExists = false;
		
		try {
			  conn = ds.getConnection();
			  
			  String sql = " select email "
			  		     + " from tbl_member "
			  		     + " where userid != ? and email = ? ";
			  
			  pstmt = conn.prepareStatement(sql);
			  pstmt.setString(1, paraMap.get("userid") );
			  pstmt.setString(2, aes.encrypt(paraMap.get("email")));
			  
			  rs = pstmt.executeQuery();
			  
			  isExists = rs.next(); // 행이 있으면 true  (중복된 email) 
			                        // 행이 없으면 false (사용가능한 email) 
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			  e.printStackTrace();
		} finally {
			  close();
		}
		
		return isExists;
	}

	// 아이디 찾기 함수
	@Override
	public String findUserid(Map<String, String> paraMap) throws SQLException {
		String userid = null;
		
		try {
			  conn = ds.getConnection();
			  
			  String sql = " select userid "
			  		     + " from tbl_member "
			  		     + " where status = 1 and name = ? and email = ? ";
			  
			  pstmt = conn.prepareStatement(sql);
			  pstmt.setString(1, paraMap.get("name"));
			  pstmt.setString(2, aes.encrypt(paraMap.get("email")) );
			  
			  rs = pstmt.executeQuery();
			  
			  if(rs.next()) {
				  userid = rs.getString("userid");
			  }
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			  e.printStackTrace();
		} finally {
			  close();
		}
		
		return userid;	
	} // end of public String findUserid()--------------------------------

	// 비밀번호 찾기(아이디, 이메일을 입력 받아서 해당 사용자가 존재하는지 유무를 알려준다) 
	@Override
	public boolean isUserExist(Map<String, String> paraMap) throws SQLException {
		
		boolean isUserExist = false;
		
		try {
			  conn = ds.getConnection();
			  
			  String sql = " select userid "
			  		     + " from tbl_member "
			  		     + " where status = 1 and userid = ? and email = ? ";
			  
			  pstmt = conn.prepareStatement(sql);
			  pstmt.setString(1, paraMap.get("userid"));
			  pstmt.setString(2, aes.encrypt(paraMap.get("email")) );
			  
			  rs = pstmt.executeQuery();
			  
			  isUserExist = rs.next();
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			  e.printStackTrace();
		} finally {
			  close();
		}		
		
		return isUserExist;
	} // end of public boolean isUserExist()-----------------------------

	
	// 비밀번호 변경 
	@Override
	public int pwdUpdate(Map<String, String> paraMap) throws SQLException {
		
		int result = 0;
		
		try {
			  conn = ds.getConnection();
			 
			  String sql = " update tbl_member set passwd = ? "
			  		     + "                     , lastpwdchangedate = sysdate " 
			  		     + " where userid = ? ";
			  
			  pstmt = conn.prepareStatement(sql);
			  
			  pstmt.setString(1, Sha256.encrypt(paraMap.get("new_pwd")) ); // 암호를 SHA256 알고리즘으로 단방향 암호화 시킨다.  
			  pstmt.setString(2, paraMap.get("userid"));
			  			  
			  result = pstmt.executeUpdate();
			  
		} finally {
			  close();
		}
		
		return result;		
	}
	
	
	
}
