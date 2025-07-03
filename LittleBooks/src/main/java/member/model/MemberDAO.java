package member.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import member.domain.MemberVO;

public interface MemberDAO {
	
	// ID 중복검사 (tbl_member 테이블에서 userid 가 존재하면 true 를 리턴해주고, userid 가 존재하지 않으면 false 를 리턴한다) 
	boolean idDuplicateCheck(String userid) throws SQLException;
	
	boolean emailDuplicateCheck(String email) throws SQLException;

	// 회원가입을해주는 메소드(tbl_member 테이블에 insert)
	int registerMember(MemberVO member) throws SQLException;

	//로그인 처리
	MemberVO login(Map<String,String>paraMap)throws SQLException;

	//아이디 찾기(성명, 이메일을 입력 받아서 해당 사용자의 아이디를 알려준다)
	String findUserid(Map<String, String> paraMap)throws SQLException;

	//비밀번호 찾기(아이디, 이메일을 입력 받아서 해당 사용자의 아이디를 알려준다)
	boolean isUserExist(Map<String, String> paraMap)throws SQLException;
	
	//이메일 중복확인
	boolean emailDuplicateCheck(Map<String, String> paraMap)throws SQLException;

	// 비밀번호 변경
	int pwdUpdate(Map<String, String> paraMap) throws SQLException;

	// 총 회원 페이지 수 구하기
	int getTotalPage(Map<String, String> paraMap) throws SQLException;

	// 회원 페이징 
	List<MemberVO> select_Member_paging(Map<String, String> paraMap) throws SQLException;

	// 총 회원 수 구하기
	int getTotalMemberCount(Map<String, String> paraMap) throws SQLException;

	// 한 명의 회원 정보 
	MemberVO selectOneMember(String userid) throws SQLException;


	List<MemberVO> getOrderListByUserid(String userid) throws SQLException;

	

	// 회원의 개인정보 변경하기
	int updateMember(MemberVO member) throws SQLException;

	// 회원정보 수정시 변경하고자 하는 암호가 현재 사용자가 사용중인지 아닌지 여부 알아오기
	// 암호 중복검사 (현재 암호와 동일하면 true 를 리턴해주고, 현재 암호와 동일하지 않으면 false 를 리턴한다) 
	boolean duplicatePwdCheck(Map<String, String> paraMap) throws SQLException;

	// 회원 로그인 상태
	public boolean isValidLogin(String userid) throws SQLException;
	
	//회원 탈퇴 
	boolean deleteMember(String userid) throws SQLException;

	// 로그인 날짜 구하기
	String selectLoginDate(String userid) throws SQLException;

	// 휴먼계정 해제하기
	int updateIdle(String userid) throws SQLException;

	// 유저 이름 찾기
	String selectName(String userid) throws SQLException;

	// 로그인 데이터 업데이트
	void updateLoginDate(String userid)throws SQLException;
	
}








