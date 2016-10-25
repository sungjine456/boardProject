package kr.co.person.pojo;

import kr.co.person.domain.User;

public class OkUserCheck {
	private User user;
	private String message;
	private boolean bool;
	
	public OkUserCheck(){
	}
	public OkUserCheck(User user, String message, boolean bool){
		this.user = user;
		this.message = message;
		this.bool = bool;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isBool() {
		return bool;
	}
	public void setBool(boolean bool) {
		this.bool = bool;
	}
}
