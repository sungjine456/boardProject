package kr.co.person.common;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
public class MessagesPropertiesTest {
	
	@Autowired private MessageSource messageSource;
	@Autowired private Message message;

	@Test
	public void testMessageSource() {
		String message = messageSource.getMessage("board.noBoard", null, "no surch", null);
		Assert.assertThat("존재하지 않는 글입니다.", is(message));
		message = messageSource.getMessage("hello.test1", null, "no surch", null);
		Assert.assertThat("no surch", is(message));
	}
	
	@Test
	public void testGetMessage() {
		Assert.assertThat(message.getMessageSource(), is(notNullValue()));
		Assert.assertThat(message.BOARD_NO_BOARD, is("존재하지 않는 글입니다."));
		Assert.assertThat(message.BOARD_NO_TITLE, is("제목을 입력해주세요."));
		Assert.assertThat(message.BOARD_NO_CONTENT, is("내용을 입력해주세요."));
		Assert.assertThat(message.BOARD_WRONG_BOARD, is("다시 입력해주세요."));
		Assert.assertThat(message.BOARD_SUCCESS_WRITE, is("글이 등록 되었습니다."));
		Assert.assertThat(message.BOARD_SUCCESS_UPDATE, is("수정에 성공하셨습니다."));
		Assert.assertThat(message.BOARD_FAIL_UPDATE, is("수정에 실패하셨습니다."));
		Assert.assertThat(message.BOARD_LIKE, is("좋아요"));
		Assert.assertThat(message.BOARD_LIKE_CANCLE, is("좋아요 취소"));
		
		Assert.assertThat(message.COMMENT_NO_COMMENT, is("존재하지 않는 댓글입니다."));
		Assert.assertThat(message.COMMENT_WRONG_COMMENT, is("다시 입력해주세요."));
		Assert.assertThat(message.COMMENT_RE_COMMENT, is("댓글을 입력해주세요."));
		Assert.assertThat(message.COMMENT_NO_REPLY, is("답을 입력해주세요."));
		
		Assert.assertThat(message.USER_LOGOUT, is("로그아웃 하셨습니다."));
		Assert.assertThat(message.USER_NO_USER, is("존재하지 않는 회원입니다."));
		Assert.assertThat(message.USER_FAIL_JOIN, is("회원가입에 실패하셨습니다."));
		Assert.assertThat(message.USER_FAIL_LOGIN, is("로그인에 실패하셨습니다."));
		Assert.assertThat(message.USER_FAIL_UPDATE, is("회원정보 수정에 실패 하셨습니다."));
		Assert.assertThat(message.USER_FAIL_REMOVE, is("탈퇴에 실패하셨습니다."));
		Assert.assertThat(message.USER_FAIL_TRANSlATE_PASSWORD, is("비밀번호 수정을 실패했습니다."));
		Assert.assertThat(message.USER_SUCCESS_JOIN, is("회원가입에 성공하셨습니다."));
		Assert.assertThat(message.USER_SUCCESS_UPDATE, is("회원정보를 수정 하셨습니다."));
		Assert.assertThat(message.USER_SUCCESS_REMOVE, is("탈퇴에 성공하셨습니다."));
		Assert.assertThat(message.USER_SUCCESS_TRANSlATE_PASSWORD, is("비밀번호 수정이 완료되었습니다."));
		Assert.assertThat(message.USER_NO_EMAIL_FORMAT, is("올바른 형식의 메일을 입력해주세요."));
		Assert.assertThat(message.USER_NO_LOGIN, is("로그인 후 이용해 주세요."));
		Assert.assertThat(message.USER_NO_ID, is("아이디를 입력해주세요."));
		Assert.assertThat(message.USER_NO_EMAIL, is("이메일을 입력해주세요."));
		Assert.assertThat(message.USER_NO_PASSWORD, is("패스워드를 입력해주세요."));
		Assert.assertThat(message.USER_NO_UPDATE_PASSWORD, is("수정할 패스워드를 입력해주세요."));
		Assert.assertThat(message.USER_WRONG_ID_OR_WRONG_PASSWORD, is("아이디 혹은 비밀번호가 틀렸습니다."));
		Assert.assertThat(message.USER_RE_PASSWORD, is("패스워드를 다시 입력해주세요."));
		Assert.assertThat(message.USER_RE_UPDATE_PASSWORD, is("수정할 패스워드를 다시 입력해주세요."));
		Assert.assertThat(message.USER_ALREADY_JOIN, is("이미 가입되어 있는 회원입니다."));
		Assert.assertThat(message.USER_ALREADY_JOIN_ID, is("이미 가입되어 있는 아이디입니다."));
		Assert.assertThat(message.USER_ALREADY_JOIN_EMAIL, is("이미 가입되어 있는 이메일입니다."));
		Assert.assertThat(message.USER_AVAILABLE_ID, is("가입 가능한 아이디입니다."));
		Assert.assertThat(message.USER_AVAILABLE_EMAIL, is("가입 가능한 이메일입니다."));
	}
}
