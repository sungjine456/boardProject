package kr.co.person.common;

import org.springframework.context.MessageSource;

public class Message {
	private MessageSource messageSource;
	public final String BOARD_NO_BOARD;
	public final String BOARD_NO_TITLE;
	public final String BOARD_NO_CONTENT;
	public final String BOARD_WRONG_BOARD;
	public final String BOARD_SUCCESS_WRITE;
	public final String BOARD_SUCCESS_UPDATE;
	public final String BOARD_FAIL_UPDATE;
	public final String BOARD_LIKE;
	public final String BOARD_LIKE_CANCLE;
	
	public final String COMMENT_NO_COMMENT;
	public final String COMMENT_WRONG_COMMENT;
	public final String COMMENT_RE_COMMENT;
	public final String COMMENT_NO_REPLY;
	
	public final String USER_LOGOUT;
	public final String USER_NO_USER;
	public final String USER_FAIL_JOIN;
	public final String USER_FAIL_LOGIN;
	public final String USER_FAIL_UPDATE;
	public final String USER_FAIL_REMOVE;
	public final String USER_FAIL_TRANSlATE_PASSWORD;
	public final String USER_SUCCESS_JOIN;
	public final String USER_SUCCESS_UPDATE;
	public final String USER_SUCCESS_REMOVE;
	public final String USER_SUCCESS_TRANSlATE_PASSWORD;
	public final String USER_NO_EMAIL_FORMAT;
	public final String USER_NO_LOGIN;
	public final String USER_NO_ID;
	public final String USER_NO_EMAIL;
	public final String USER_NO_PASSWORD;
	public final String USER_NO_UPDATE_PASSWORD;
	public final String USER_WRONG_ID_OR_WRONG_PASSWORD;
	public final String USER_RE_PASSWORD;
	public final String USER_RE_UPDATE_PASSWORD;
	public final String USER_ALREADY_JOIN;
	public final String USER_ALREADY_JOIN_ID;
	public final String USER_ALREADY_JOIN_EMAIL;
	public final String USER_AVAILABLE_ID;
	public final String USER_AVAILABLE_EMAIL;
	
	public Message(MessageSource messageSource){
		this.messageSource = messageSource;
		
		BOARD_NO_BOARD = messageSource.getMessage("board.noBoard", null, null);
		BOARD_NO_TITLE = messageSource.getMessage("board.noTitle", null, null);
		BOARD_NO_CONTENT = messageSource.getMessage("board.noContent", null, null);
		BOARD_WRONG_BOARD = messageSource.getMessage("board.wrongContent", null, null);
		BOARD_SUCCESS_WRITE = messageSource.getMessage("board.successWrite", null, null);
		BOARD_SUCCESS_UPDATE = messageSource.getMessage("board.successUpdate", null, null);
		BOARD_FAIL_UPDATE = messageSource.getMessage("board.failUpdate", null, null);
		BOARD_LIKE = messageSource.getMessage("board.like", null, null);
		BOARD_LIKE_CANCLE = messageSource.getMessage("board.likeCancle", null, null);
		
		COMMENT_NO_COMMENT = messageSource.getMessage("comment.noComment", null, null);
		COMMENT_WRONG_COMMENT = messageSource.getMessage("comment.wrongComment", null, null);
		COMMENT_RE_COMMENT = messageSource.getMessage("comment.reComment", null, null);
		COMMENT_NO_REPLY = messageSource.getMessage("comment.noReply", null, null);
		
		USER_LOGOUT = messageSource.getMessage("user.logout", null, null);
		USER_NO_USER = messageSource.getMessage("user.noUser", null, null);
		USER_FAIL_JOIN = messageSource.getMessage("user.failJoin", null, null);
		USER_FAIL_LOGIN = messageSource.getMessage("user.failLogin", null, null);
		USER_FAIL_UPDATE = messageSource.getMessage("user.failUpdate", null, null);
		USER_FAIL_REMOVE = messageSource.getMessage("user.failRemove", null, null);
		USER_FAIL_TRANSlATE_PASSWORD = messageSource.getMessage("user.failTranslatePassword", null, null);
		USER_SUCCESS_JOIN = messageSource.getMessage("user.successJoin", null, null);
		USER_SUCCESS_UPDATE = messageSource.getMessage("user.successUpdate", null, null);
		USER_SUCCESS_REMOVE = messageSource.getMessage("user.successRemove", null, null);
		USER_SUCCESS_TRANSlATE_PASSWORD = messageSource.getMessage("user.successTranslatePassword", null, null);
		USER_NO_EMAIL_FORMAT = messageSource.getMessage("user.noEmailFormat", null, null);
		USER_NO_LOGIN = messageSource.getMessage("user.noLogin", null, null);
		USER_NO_ID = messageSource.getMessage("user.noId", null, null);
		USER_NO_EMAIL = messageSource.getMessage("user.noEmail", null, null);
		USER_NO_PASSWORD = messageSource.getMessage("user.noPassword", null, null);
		USER_NO_UPDATE_PASSWORD = messageSource.getMessage("user.noUpdatePassword", null, null);
		USER_WRONG_ID_OR_WRONG_PASSWORD = messageSource.getMessage("user.wrongIdOrWrongPassword", null, null);
		USER_RE_PASSWORD = messageSource.getMessage("user.rePassword", null, null);
		USER_RE_UPDATE_PASSWORD = messageSource.getMessage("user.reUpdatePassword", null, null);
		USER_ALREADY_JOIN = messageSource.getMessage("user.alreadyJoin", null, null);
		USER_ALREADY_JOIN_ID = messageSource.getMessage("user.alreadyJoinId", null, null);
		USER_ALREADY_JOIN_EMAIL = messageSource.getMessage("user.alreadyJoinEmail", null, null);
		USER_AVAILABLE_ID = messageSource.getMessage("user.availableId", null, null);
		USER_AVAILABLE_EMAIL = messageSource.getMessage("user.availableEmail", null, null);
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}
}