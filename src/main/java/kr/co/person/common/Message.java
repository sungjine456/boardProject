package kr.co.person.common;

import org.springframework.context.MessageSource;

public class Message {
	private MessageSource messageSource;
	public final String BOARD_NO_BOARD;
	public final String BOARD_NO_TITLE;
	public final String BOARD_NO_CONTENT;
	public final String BOARD_SUCCESS_WRITE;
	public final String BOARD_SUCCESS_UPDATE;
	public final String BOARD_FAIL_UPDATE;
	public final String BOARD_LIKE;
	public final String BOARD_LIKE_CANCLE;
	
	public final String COMMENT_NO_COMMENT;
	public final String COMMENT_RE_COMMENT;
	public final String COMMENT_NO_REPLY;
	
	public final String USER_LOGOUT;
	public final String USER_FAIL_JOIN;
	public final String USER_FAIL_LOGIN;
	public final String USER_FAIL_LOGOUT;
	public final String USER_FAIL_UPDATE;
	public final String USER_FAIL_LEAVE;
	public final String USER_FAIL_TRANSLATE_PASSWORD;
	public final String USER_SUCCESS_JOIN;
	public final String USER_SUCCESS_UPDATE;
	public final String USER_SUCCESS_LEAVE;
	public final String USER_SUCCESS_TRANSlATE_PASSWORD;
	public final String USER_NO_EMAIL_FORMAT;
	public final String USER_NO_LOGIN;
	public final String USER_NO_ID;
	public final String USER_NO_NAME;
	public final String USER_NO_EMAIL;
	public final String USER_NO_PASSWORD;
	public final String USER_NO_UPDATE_PASSWORD;
	public final String USER_PASSWORD_SAME_UPDATE_PASSWORD;
	public final String USER_WRONG_ID_OR_WRONG_PASSWORD;
	public final String USER_WRONG_EMAIL;
	public final String USER_WRONG_USER;
	public final String USER_RE_PASSWORD;
	public final String USER_RE_UPDATE_PASSWORD;
	public final String USER_ALREADY_JOIN;
	public final String USER_ALREADY_JOIN_ID;
	public final String USER_ALREADY_JOIN_EMAIL;
	public final String USER_AVAILABLE_ID;
	public final String USER_AVAILABLE_EMAIL;
	
	public final String FILE_FAIL_UPLOAD;
	
	public final String MAIL_TRANSLATE_PASSWORD_TITLE;
	public final String MAIL_SUCCESS_TRANSLATE_PASSWORD;
	
	public Message(MessageSource messageSource){
		this.messageSource = messageSource;
		
		BOARD_NO_BOARD = messageSource.getMessage("board.noBoard", null, null);
		BOARD_NO_TITLE = messageSource.getMessage("board.noTitle", null, null);
		BOARD_NO_CONTENT = messageSource.getMessage("board.noContent", null, null);
		BOARD_SUCCESS_WRITE = messageSource.getMessage("board.successWrite", null, null);
		BOARD_SUCCESS_UPDATE = messageSource.getMessage("board.successUpdate", null, null);
		BOARD_FAIL_UPDATE = messageSource.getMessage("board.failUpdate", null, null);
		BOARD_LIKE = messageSource.getMessage("board.like", null, null);
		BOARD_LIKE_CANCLE = messageSource.getMessage("board.likeCancle", null, null);
		
		COMMENT_NO_COMMENT = messageSource.getMessage("comment.noComment", null, null);
		COMMENT_RE_COMMENT = messageSource.getMessage("comment.reComment", null, null);
		COMMENT_NO_REPLY = messageSource.getMessage("comment.noReply", null, null);
		
		USER_LOGOUT = messageSource.getMessage("user.logout", null, null);
		USER_FAIL_JOIN = messageSource.getMessage("user.failJoin", null, null);
		USER_FAIL_LOGIN = messageSource.getMessage("user.failLogin", null, null);
		USER_FAIL_LOGOUT = messageSource.getMessage("user.failLogout", null, null);
		USER_FAIL_UPDATE = messageSource.getMessage("user.failUpdate", null, null);
		USER_FAIL_LEAVE = messageSource.getMessage("user.failLeave", null, null);
		USER_FAIL_TRANSLATE_PASSWORD = messageSource.getMessage("user.failTranslatePassword", null, null);
		USER_SUCCESS_JOIN = messageSource.getMessage("user.successJoin", null, null);
		USER_SUCCESS_UPDATE = messageSource.getMessage("user.successUpdate", null, null);
		USER_SUCCESS_LEAVE = messageSource.getMessage("user.successLeave", null, null);
		USER_SUCCESS_TRANSlATE_PASSWORD = messageSource.getMessage("user.successTranslatePassword", null, null);
		USER_NO_EMAIL_FORMAT = messageSource.getMessage("user.noEmailFormat", null, null);
		USER_NO_LOGIN = messageSource.getMessage("user.noLogin", null, null);
		USER_NO_ID = messageSource.getMessage("user.noId", null, null);
		USER_NO_NAME = messageSource.getMessage("user.noName", null, null);
		USER_NO_EMAIL = messageSource.getMessage("user.noEmail", null, null);
		USER_NO_PASSWORD = messageSource.getMessage("user.noPassword", null, null);
		USER_NO_UPDATE_PASSWORD = messageSource.getMessage("user.noUpdatePassword", null, null);
		USER_PASSWORD_SAME_UPDATE_PASSWORD = messageSource.getMessage("user.passwordSameUpdatePassword", null, null);
		USER_WRONG_ID_OR_WRONG_PASSWORD = messageSource.getMessage("user.wrongIdOrWrongPassword", null, null);
		USER_WRONG_EMAIL = messageSource.getMessage("user.wrongEmail", null, null);
		USER_WRONG_USER = messageSource.getMessage("user.wrongUser", null, null);
		USER_RE_PASSWORD = messageSource.getMessage("user.rePassword", null, null);
		USER_RE_UPDATE_PASSWORD = messageSource.getMessage("user.reUpdatePassword", null, null);
		USER_ALREADY_JOIN = messageSource.getMessage("user.alreadyJoin", null, null);
		USER_ALREADY_JOIN_ID = messageSource.getMessage("user.alreadyJoinId", null, null);
		USER_ALREADY_JOIN_EMAIL = messageSource.getMessage("user.alreadyJoinEmail", null, null);
		USER_AVAILABLE_ID = messageSource.getMessage("user.availableId", null, null);
		USER_AVAILABLE_EMAIL = messageSource.getMessage("user.availableEmail", null, null);

		FILE_FAIL_UPLOAD = messageSource.getMessage("file.failUpload", null, null);

		MAIL_TRANSLATE_PASSWORD_TITLE = messageSource.getMessage("mail.translatePasswordTitle", null, null);
		MAIL_SUCCESS_TRANSLATE_PASSWORD = messageSource.getMessage("mail.successTranslatePassword", null, null);
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}
}
