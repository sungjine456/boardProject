package kr.co.person.common;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
public class IsValidTest {

	@Test
	public void testIsValidInt() {
		Assert.assertThat(IsValid.isValid(0), is(false));
		Assert.assertThat(IsValid.isValid(1), is(true));
	}

	@Test
	public void testIsValidIntInt() {
		Assert.assertThat(IsValid.isValid(0, 0), is(false));
		Assert.assertThat(IsValid.isValid(1, 0), is(false));
		Assert.assertThat(IsValid.isValid(0, 1), is(false));
		Assert.assertThat(IsValid.isValid(1, 1), is(true));
	}
	
	@Test
	public void testIsValidIntIntInt() {
		Assert.assertThat(IsValid.isValid(0, 0, 0), is(false));
		Assert.assertThat(IsValid.isValid(0, 0, 1), is(false));
		Assert.assertThat(IsValid.isValid(0, 1, 0), is(false));
		Assert.assertThat(IsValid.isValid(1, 0, 0), is(false));
		Assert.assertThat(IsValid.isValid(0, 1, 1), is(false));
		Assert.assertThat(IsValid.isValid(1, 0, 1), is(false));
		Assert.assertThat(IsValid.isValid(1, 1, 0), is(false));
		Assert.assertThat(IsValid.isValid(1, 1, 1), is(true));
	}
	
	@Test
	public void testIsNotValidInt() {
		Assert.assertThat(IsValid.isNotValid(0), is(true));
		Assert.assertThat(IsValid.isNotValid(1), is(false));
	}

	@Test
	public void testIsNotValidIntInt() {
		Assert.assertThat(IsValid.isNotValid(0, 0), is(true));
		Assert.assertThat(IsValid.isNotValid(1, 0), is(true));
		Assert.assertThat(IsValid.isNotValid(0, 1), is(true));
		Assert.assertThat(IsValid.isNotValid(1, 1), is(false));
	}
	
	@Test
	public void testIsNotValidIntIntInt() {
		Assert.assertThat(IsValid.isNotValid(0, 0, 0), is(true));
		Assert.assertThat(IsValid.isNotValid(0, 0, 1), is(true));
		Assert.assertThat(IsValid.isNotValid(0, 1, 0), is(true));
		Assert.assertThat(IsValid.isNotValid(1, 0, 0), is(true));
		Assert.assertThat(IsValid.isNotValid(0, 1, 1), is(true));
		Assert.assertThat(IsValid.isNotValid(1, 0, 1), is(true));
		Assert.assertThat(IsValid.isNotValid(1, 1, 0), is(true));
		Assert.assertThat(IsValid.isNotValid(1, 1, 1), is(false));
	}
	
	@Test
	public void testIsValidObject() {
		Assert.assertThat(IsValid.isValid(null), is(false));
		Assert.assertThat(IsValid.isValid(new Object()), is(true));
	}

	@Test
	public void testIsValidObjectObject() {
		Assert.assertThat(IsValid.isValid(null, null), is(false));
		Assert.assertThat(IsValid.isValid(new Object(), null), is(false));
		Assert.assertThat(IsValid.isValid(null, new Object()), is(false));
		Assert.assertThat(IsValid.isValid(new Object(), new Object()), is(true));
	}
	
	@Test
	public void testIsValidObjectObjectObject() {
		Assert.assertThat(IsValid.isValid(null, null, null), is(false));
		Assert.assertThat(IsValid.isValid(null, null, new Object()), is(false));
		Assert.assertThat(IsValid.isValid(null, new Object(), null), is(false));
		Assert.assertThat(IsValid.isValid(new Object(), null, null), is(false));
		Assert.assertThat(IsValid.isValid(null, new Object(), new Object()), is(false));
		Assert.assertThat(IsValid.isValid(new Object(), null, new Object()), is(false));
		Assert.assertThat(IsValid.isValid(new Object(), new Object(), null), is(false));
		Assert.assertThat(IsValid.isValid(new Object(), new Object(), new Object()), is(true));
	}
	
	@Test
	public void testIsNotValidObject() {
		Assert.assertThat(IsValid.isNotValid(null), is(true));
		Assert.assertThat(IsValid.isNotValid(new Object()), is(false));
	}

	@Test
	public void testIsNotValidObjectObject() {
		Assert.assertThat(IsValid.isNotValid(null, null), is(true));
		Assert.assertThat(IsValid.isNotValid(new Object(), null), is(true));
		Assert.assertThat(IsValid.isNotValid(null, new Object()), is(true));
		Assert.assertThat(IsValid.isNotValid(new Object(), new Object()), is(false));
	}
	
	@Test
	public void testIsNotValidObjectObjectObject() {
		Assert.assertThat(IsValid.isNotValid(null, null, null), is(true));
		Assert.assertThat(IsValid.isNotValid(null, null, new Object()), is(true));
		Assert.assertThat(IsValid.isNotValid(null, new Object(), null), is(true));
		Assert.assertThat(IsValid.isNotValid(new Object(), null, null), is(true));
		Assert.assertThat(IsValid.isNotValid(null, new Object(), new Object()), is(true));
		Assert.assertThat(IsValid.isNotValid(new Object(), null, new Object()), is(true));
		Assert.assertThat(IsValid.isNotValid(new Object(), new Object(), null), is(true));
		Assert.assertThat(IsValid.isNotValid(new Object(), new Object(), new Object()), is(false));
	}
}
