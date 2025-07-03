package member.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
				 
				 member.setUserid(rs.getString("userid"));
				 member.setName(rs.getString("name"));
				 member.setPoint(rs.getInt("point"));
				 member.setRegisterday(rs.getString("registerday"));
				 
				 if(rs.getInt("pwdchangegap") >= 3) {
					 // 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지났으면 true
					 // 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지나지 않았으면 false
					 
					 member.setRequirePwdChange(true); // 로그인시 암호를 변경해라는 alert 를 띄우도록 할 때 사용한다.
				 }
				 member.setIdle(rs.getInt("idle"));
				 member.setEmail(aes.decrypt(rs.getString("email")));
				 member.setMobile(aes.decrypt(rs.getString("mobile")));
				 member.setPostcode(rs.getString("postcode"));
				 member.setAddress(rs.getString("address"));
				 member.setDetailaddress(rs.getString("detailaddress"));
				 member.setExtraaddress(rs.getString("extraaddress"));
				 
				 
				 // ==== 휴면이 아닌 회원만 tbl_loginhistory(로그인기록) 테이블에 insert 하기 시작 ==== // 
				 if( rs.getInt("lastlogingap") < 12 ) {
					 sql = " insert into tbl_loginhistory(logindate, fk_userid) "
					 	 + " values(sysdate, ?) ";
					 
					 pstmt = conn.prepareStatement(sql);
					 pstmt.setString(1, paraMap.get("userid"));
					 
					 pstmt.executeUpdate();
				 }
				// ==== 휴면이 아닌 회원만 tbl_loginhistory(로그인기록) 테이블에 insert 하기 끝 ==== //
				 
				 else {
					 // 마지막으로 로그인 한 날짜시간이 현재시각으로 부터 1년이 지났으면 휴면으로 지정 
					 member.setIdle(1);
					 
					 if(rs.getInt("idle") == 0) {
					     // === tbl_member 테이블의 idle 컬럼의 값을 1로 변경하기 === //
						 sql = " update tbl_member set idle = 1 "
						 	 + " where userid = ? ";
						 
						 pstmt = conn.prepareStatement(sql);
						 pstmt.setString(1, paraMap.get("userid"));
						 
						 pstmt.executeUpdate();
					 }
					 
				 }
				 
			 }// end of if(rs.next())---------------------------

			 
			
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
	
	

	// 총 회원 페이지 수 구하기
	@Override
	public int getTotalPage(Map<String, String> paraMap) throws SQLException {

		int totalPage = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT ceil(count(*)/?) "
					   + " FROM tbl_member "
					   + " WHERE userid != 'admin' AND status = 1 ";
			
			String colname = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			
			// 검색대상이 email인 경우 암호화
			if("email".equals(colname) && !"".equals(searchWord)) {
				searchWord = aes.encrypt(searchWord);
			}
			
			if(!"".equals(colname) && !"".equals(searchWord) ) {
				sql +=  " AND " + colname + " LIKE '%' || ? || '%' ";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(paraMap.get("sizePerPage")));
			
			
			if(!"".equals(colname) && !"".equals(searchWord) ) { // 검색이 있는 경우
				pstmt.setString(2, searchWord);
			}
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			totalPage = rs.getInt(1);
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return totalPage;
		
	}

	// 회원 페이징 
	@Override
	public List<MemberVO> select_Member_paging(Map<String, String> paraMap) throws SQLException {

		List<MemberVO> memberList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT userid, name, email "
					   + " FROM tbl_member "
					   + " WHERE userid != 'admin' AND status = 1 ";
			
			String colname = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			
			// 검색대상이 email인 경우 암호화
			if("email".equals(colname) && !"".equals(searchWord)) {
				searchWord = aes.encrypt(searchWord);
			}
			
			if(!"".equals(colname) && !"".equals(searchWord) ) {
				sql +=  " AND " + colname + " LIKE '%' || ? || '%' ";
			}
				       
			sql += " ORDER BY registerday desc "
				 + " OFFSET (?-1)*? ROW "
				 + " FETCH NEXT ? ROW ONLY ";
			
			pstmt = conn.prepareStatement(sql);
			
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			
			
			if(!"".equals(colname) && !"".equals(searchWord) ) { // 검색이 있는 경우
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, currentShowPageNo);
				pstmt.setInt(3, sizePerPage);
				pstmt.setInt(4, sizePerPage);
			}
			else { // 검색이 없는 경우
				pstmt.setInt(1, currentShowPageNo);
				pstmt.setInt(2, sizePerPage);
				pstmt.setInt(3, sizePerPage);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				 MemberVO mvo = new MemberVO(); // userid, name, email
				
				 mvo.setUserid(rs.getString("userid"));
				 mvo.setName(rs.getString("name"));
				 mvo.setEmail(aes.decrypt(rs.getString("email"))); // 복호화 
				 
				 memberList.add(mvo);
				
			}// end of while(rs.next())------------------------
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return memberList;
		
	}

	// 총 회원 수 구하기
	@Override
	public int getTotalMemberCount(Map<String, String> paraMap) throws SQLException {

		int totalMemberCount = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT count(*) "
					   + " FROM tbl_member "
					   + " WHERE userid != 'admin' AND status = 1 ";
			
			String colname = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			
			// 검색대상이 email인 경우 암호화
			if("email".equals(colname) && !"".equals(searchWord)) {
				searchWord = aes.encrypt(searchWord);
			}
			
			if(!"".equals(colname) && !"".equals(searchWord) ) {
				sql +=  " AND " + colname + " LIKE '%' || ? || '%' ";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			if(!"".equals(colname) && !"".equals(searchWord) ) { // 검색이 있는 경우
				pstmt.setString(1, searchWord);
			}
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			totalMemberCount = rs.getInt(1);
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return totalMemberCount;
	}

	// 한 명의 회원 정보 
	@Override
	public MemberVO selectOneMember(String userid) throws SQLException {
		
		MemberVO member = new MemberVO();
		
		try {
			conn = ds.getConnection();
			
			String sql = "  SELECT userid, name, point, "
					   + " 		   to_char(registerday, 'yyyy-mm-dd') AS registerday, "
					   + "         idle, email, mobile, postcode, address, detailaddress, extraaddress, "
					   + "         birthday"
					   + "  FROM tbl_member "
					   + "  WHERE status = 1 AND userid = ? " ;
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				member = new MemberVO();
				
				member.setUserid(rs.getString("userid"));
				member.setName(rs.getString("name"));
				member.setPoint(rs.getInt("point"));
				member.setRegisterday(rs.getString("registerday"));
				
				member.setIdle(rs.getInt("idle"));
				member.setEmail( aes.decrypt(rs.getString("email")) );
				member.setMobile( aes.decrypt(rs.getString("mobile")) );
				member.setPostcode( rs.getString("postcode") );
				member.setAddress( rs.getString("address") );
				member.setDetailaddress( rs.getString("detailaddress") );
				member.setExtraaddress( rs.getString("extraaddress") );
				member.setBirthday( rs.getString("birthday") );
				
			} // end of if(rs.next()) ----------
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
	         e.printStackTrace();
	    } finally {
			close();
		}
		
		return member;
		
	}

	@Override
	public List<MemberVO> getOrderListByUserid(String userid) throws SQLException {


	    return null;  // 리스트 반환
	}



 
	// 회원의 개인정보 변경하기
	@Override
	public int updateMember(MemberVO member) throws SQLException {
		int result = 0;

		try {
			 conn = ds.getConnection();
			 
			 String sql = " update tbl_member set name = ? "
					    + "                     , passwd = ? "
					    + "                     , email = ? "
					    + "                     , mobile = ? "
					    + "                     , postcode = ? " 
					    + "                     , address = ? "
					    + "                     , detailaddress = ? "
					    + "                     , extraaddress = ? "
					    + "                     , lastpwdchangedate = sysdate "
					    + " where userid = ? ";
			 
			 pstmt = conn.prepareStatement(sql);
				
			 pstmt.setString(1, member.getName());
			 pstmt.setString(2, Sha256.encrypt(member.getPwd()) ); // 암호를 SHA256 알고리즘으로 단방향 암호화 시킨다.
			 pstmt.setString(3, aes.encrypt(member.getEmail()) );  // 이메일을 AES256 알고리즘으로 양방향 암호화 시킨다. 
			 pstmt.setString(4, aes.encrypt(member.getMobile()) ); // 휴대폰번호를 AES256 알고리즘으로 양방향 암호화 시킨다. 
			 pstmt.setString(5, member.getPostcode());
			 pstmt.setString(6, member.getAddress());
			 pstmt.setString(7, member.getDetailaddress());
			 pstmt.setString(8, member.getExtraaddress());
			 pstmt.setString(9, member.getUserid());
			 
			 result = pstmt.executeUpdate();
			 
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}

	// 회원정보 수정시 변경하고자 하는 암호가 현재 사용자가 사용중인지 아닌지 여부 알아오기
	// 암호 중복검사 (현재 암호와 동일하면 true 를 리턴해주고, 현재 암호와 동일하지 않으면 false 를 리턴한다)
	@Override
	public boolean duplicatePwdCheck(Map<String, String> paraMap) throws SQLException{
		
		boolean isExists = false;
		
		try {
			  conn = ds.getConnection();
			  
			  String sql = " select * "
			  		     + " from tbl_member "
			  		     + " where userid = ? and passwd = ? ";
			  
			  pstmt = conn.prepareStatement(sql);
			  pstmt.setString(1, paraMap.get("userid") );
			  pstmt.setString(2, Sha256.encrypt(paraMap.get("new_pwd")) );
			  
			  rs = pstmt.executeQuery();
			  
			  isExists = rs.next(); // 행이 있으면 true  (새암호가 현재 사용중인 암호와 같은 경우) 
			                        // 행이 없으면 false (새암호가 현재 사용중인 암호와 다른 경우) 
			
		} finally {
			close();
		}
		
		return isExists;
	}
	
	// 회원 상태 확인 (status) 
	
	@Override
	public boolean isValidLogin(String userid) throws SQLException {
	    boolean isValid = false;

	    try {
	        conn = ds.getConnection();

	        String sql = " SELECT status "
	        		+ " FROM tbl_member "
	        		+ " WHERE userid = ? ";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            int status = rs.getInt("status");
	            isValid = (status == 1);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();  // 예외 발생 시 로그 출력
	    } finally {
	        close();
	    }

	    return isValid;
	}
	
	
	//회원 탈퇴 
	@Override
	public boolean deleteMember(String userid) throws SQLException {
	    boolean isDeleted = false;

	    try {
	        conn = ds.getConnection();

	        String sql = " UPDATE tbl_member "
	        		+ "	SET status = 0 "
	        		+ " WHERE userid = ? ";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);

	        int affectedRows = pstmt.executeUpdate();
	        if (affectedRows > 0) {
	            isDeleted = true;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();  
	    } finally {
	        close();
	    }

	    return isDeleted;
	}

	// 로그인 날짜 구하기
	@Override
	public String selectLoginDate(String userid) throws SQLException {

		String loginDate = "";
		
	    try {
	        conn = ds.getConnection();

	        String sql = "  SELECT logindate "
	        		+ "  FROM "
	        		+ "  ( "
	        		+ "    SELECT logindate "
	        		+ "    FROM tbl_loginhistory "
	        		+ "    WHERE fk_userid = ? "
	        		+ "    ORDER BY logindate DESC "
	        		+ "  ) "
	        		+ "  WHERE ROWNUM = 1 ";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);

	        rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	loginDate = rs.getString("logindate");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();  
	    } finally {
	        close();
	    }
		
		return loginDate;
	} // end of public String selectLoginDate(String userid) throws SQLException {}------------

	// 휴먼계정 해제하기
	@Override
	public int updateIdle(String userid) throws SQLException {
		
		int n = 0;
		
	    try {
	        conn = ds.getConnection();

	        String sql = " update tbl_member "
	        		+ " set idle = 0 "
	        		+ " where userid = ? ";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);

	        n = pstmt.executeUpdate();
	        
	    } finally {
	        close();
	    }
		return n;
	} // end of public int updateIdle(String userid) throws SQLException {}-----------------

	// 유저 이름 찾기
	@Override
	public String selectName(String userid) throws SQLException {
		String name = "";
		
	    try {
	        conn = ds.getConnection();

	        String sql = " select name "
	        		+ " from tbl_member "
	        		+ " where userid = ? ";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);

	        rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	name = rs.getString("name");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();  
	    } finally {
	        close();
	    }
		
		return name;

	} // end of public String selectName(String userid) throws SQLException {}-----------------

	// 로그인 데이터 업데이트
	@Override
	public void updateLoginDate(String userid) throws SQLException {
		try {
			  conn = ds.getConnection();
			 
			  String sql = " insert into tbl_loginhistory(logindate, fk_userid) "
					 	 + " values(sysdate, ?) ";
					 
			  pstmt = conn.prepareStatement(sql);
			  pstmt.setString(1, userid);
			 
			  pstmt.executeUpdate();

		} finally {
			  close();
		}
	} // end of public void updateLoginDate(String userid) throws SQLException {}----------------
	
}
