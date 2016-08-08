package kr.co.person.pojo;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@MappedSuperclass
public class CommonEntity {
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime regDate;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime upDate;
	
	public CommonEntity(){
	}
	public CommonEntity(DateTime regDate, DateTime upDate){
		this.regDate = regDate;
		this.upDate = upDate;
	}
	
	public DateTime getRegDate() {
		return regDate;
	}
	public void setRegDate(DateTime regDate) {
		this.regDate = regDate;
	}
	public DateTime getUpDate() {
		return upDate;
	}
	public void setUpDate(DateTime upDate) {
		this.upDate = upDate;
	}
}
