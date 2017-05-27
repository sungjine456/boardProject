package kr.co.person.common;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotEquals;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.exception.EmptyStringException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
public class EncryptionTest {
	
	@Autowired Encryption encryption;
	
	@Test
	public void encodeAndDecodeTest() throws Exception{
		String testStr = "sungjin";
		String en = encryption.aesEncode(testStr);
		Assert.assertThat(en, is(not(testStr)));
		String de = encryption.aesDecode(en);
		Assert.assertThat(de, is(testStr));
	}
	
	@Test
	public void oneWayEncryptionTest() throws Exception{
		String testStr = "sungjin";
		String encryptionStr = encryption.oneWayEncryption(testStr);
		
		assertNotEquals(testStr, encryptionStr);
	}

	@Test
	public void oneWayEncryptionSaltTest() throws Exception{
		String testStr = "sungjin";
		String saltEncryptionStr = encryption.oneWayEncryption(testStr, "salt");
		
		assertNotEquals(testStr, saltEncryptionStr);
	}
	
	@Test(expected=EmptyStringException.class)
	public void oneWayEncryptionParamNullTest() throws Exception{
		encryption.oneWayEncryption(null);
	}
	
	@Test(expected=EmptyStringException.class)
	public void oneWayEncryptionParamEmptyTest() throws Exception{
		encryption.oneWayEncryption("");
	}
}
