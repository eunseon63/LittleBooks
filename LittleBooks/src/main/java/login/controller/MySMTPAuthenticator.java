package login.controller;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

// Gmail SMTP 서버 인증을 위한 클래스
public class MySMTPAuthenticator extends Authenticator {

	@Override
    public PasswordAuthentication getPasswordAuthentication() {
   
      // @gmail.com 을 제외한 아이디만 입력
      return new PasswordAuthentication("fydwns12345","eekyqwvufzdoyper"); 
      // "nwqtrakwohbxgnna" 은 Google에 로그인 하기 위한 앱비밀번호
    }	
	
}
