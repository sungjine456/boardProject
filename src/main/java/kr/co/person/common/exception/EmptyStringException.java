package kr.co.person.common.exception;

public class EmptyStringException extends Exception {

	private static final long serialVersionUID = -3476390194767131010L;
	
	public EmptyStringException() {
		super();
	}
	public EmptyStringException(String message) {
		super(message);
	}
}
