package kr.co.person.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue
	private int idx;
	@Column(name="id", nullable=false)
	private String id;
	@Column(name="password", nullable=false, length = 100)
	private String password;
	@Column(name="name", nullable=false)
	private String name;
	@Column(name="email", nullable=false)
	private String email;
	@Column(name="reg_date", nullable=false)
	private Date regDate;
	@Column(name="up_date", nullable=false)
	private Date upDate;
	
	public User(){
	}
	
	public User(String id, String email, String password, String name, Date regDate, Date upDate){
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.regDate = regDate;
		this.upDate = upDate;
	}
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public Date getUpDate() {
		return upDate;
	}
	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}
}
