package kr.co.person.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import kr.co.person.pojo.AutoLoginId;

@Entity
@DynamicUpdate
@Table(name="auto_login")
@IdClass(AutoLoginId.class)
public class AutoLogin{
	@Id
	@Column(name="auto_login_id", nullable = false)
	private String loginId;
	@Id
	@Column(name="reg_date", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime regDate;
	@Column(name="auto_login_check", nullable = false)
	private String loginCheck;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_idx")
    private User user;

	public AutoLogin() {
	}
	public AutoLogin(String loginId, DateTime regDate, String loginCheck, User user) {
		this.loginId = loginId;
		this.regDate = regDate;
		this.loginCheck = loginCheck;
		this.user = user;
	}
	
	public String getLoginCheck() {
		return loginCheck;
	}
	public void setLoginCheck(String loginCheck) {
		this.loginCheck = loginCheck;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public DateTime getRegDate() {
		return regDate;
	}
	public void setRegDate(DateTime regDate) {
		this.regDate = regDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
