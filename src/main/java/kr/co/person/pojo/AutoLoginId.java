package kr.co.person.pojo;

import java.io.Serializable;

import org.joda.time.DateTime;

public class AutoLoginId implements Serializable {
	/**
	 * 객체를 파일에 쓰거나 전송하기 위해 클레스에
	 * Serializable 인터페이스를 implements 하게 되면 Warning이 발생하는데
	 * SerialVersionUID를 선언하지 않아도 default로 생성하지만 
	 * JAVA에서는 SerialVersionUID를 선언해주는 것을 권장하기 위해 Warning이 발생한다.
	 */
	private static final long serialVersionUID = -3221335607180479314L;
	private String loginCheck;
	private DateTime regDate;

	public AutoLoginId(){
	}

	public String getLoginCheck() {
		return loginCheck;
	}
	public void setLoginCheck(String loginCheck) {
		this.loginCheck = loginCheck;
	}
	public DateTime getRegDate() {
		return regDate;
	}
	public void setRegDate(DateTime regDate) {
		this.regDate = regDate;
	}
}
