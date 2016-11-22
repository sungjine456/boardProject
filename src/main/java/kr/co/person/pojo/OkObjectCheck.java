package kr.co.person.pojo;

public class OkObjectCheck<T> {
	private T t;
	private String message;
	private boolean bool;
	
	public OkObjectCheck(){
	}
	public OkObjectCheck(T t, String message, boolean bool){
		this.t = t;
		this.message = message;
		this.bool = bool;
	}

	public T getObject() {
		return t;
	}
	public String getMessage() {
		return message;
	}
	public boolean isBool() {
		return bool;
	}
}
