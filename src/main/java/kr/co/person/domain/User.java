package kr.co.person.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;

@Entity
@Table(name = "user")
@AttributeOverrides({
	@AttributeOverride(name = "regDate", column = @Column(name = "reg_date", nullable = false)),
	@AttributeOverride(name = "updateDate", column = @Column(name = "up_date", nullable = false))
})
public class User extends CommonEntity {
	@Id
	@Column(name = "user_idx")
	@GeneratedValue
	private int idx;
	@Column(name="id", nullable = false)
	private String id;
	@Column(name="password", nullable = false, length = 100)
	private String password;
	@Column(name="name", nullable = false)
	private String name;
	@Column(name="email", nullable = false)
	private String email;
	@Column(name="img")
	private String img;
	@Column(name="access")
	private String access = "N";
	@Column(name="admin_yn")
	private String admin = "N";
	
	public User(){
	}
	
	public User(String id, String email, String password, String name, String img, DateTime regDate, DateTime updateDate){
		super(regDate, updateDate);
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.img = img;
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
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
}
