package kr.co.person.common;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.person.common.exception.EmptyStringException;

@Component
public class Encryption {
	@Value("${keyValue}") private String ENCRYPTION_KEY_OF_COOKIE;
	private final String ENCRPYTION_SALT = "cookiesAutoLogin";
	
	private Key AES256Util() throws Exception {
        byte[] keyBytes = new byte[16];
        byte[] b = ENCRYPTION_KEY_OF_COOKIE.getBytes("UTF-8");
        int len = b.length;
        if(len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
 
        return keySpec;
    }
 
    public String aesEncode(String str) throws EmptyStringException, Exception {
    	if(StringUtils.isEmpty(str)){
    		throw new EmptyStringException("빈 문자열은 안됩니다.");
		}
    	String iv = ENCRYPTION_KEY_OF_COOKIE.substring(0, 16);
    	try{
    		Key keySpec = AES256Util();
	        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
	 
	        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
	        return new String(Base64.encodeBase64(encrypted));
    	} catch(Exception e){
    		throw new Exception();
    	}
    }
 
    public String aesDecode(String str) throws EmptyStringException, Exception {
    	if(StringUtils.isEmpty(str)){
    		throw new EmptyStringException("빈 문자열은 안됩니다.");
		}
    	String iv = ENCRYPTION_KEY_OF_COOKIE.substring(0, 16);
    	Cipher c;
    	byte[] byteStr;
    	try{
	    	Key keySpec = AES256Util();
	    	c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
	 
	        byteStr = Base64.decodeBase64(str.getBytes());
	        return new String(c.doFinal(byteStr),"UTF-8");
    	} catch(Exception e){
    		throw new Exception();
    	}
    }
    
    public String oneWayEncryption(String str) throws EmptyStringException, NoSuchAlgorithmException {
    	return oneWayEncryptionExcution(str, null);
    }
    public String oneWayEncryption(String str, String salt) throws EmptyStringException, NoSuchAlgorithmException {
    	return oneWayEncryptionExcution(str, salt);
    }
    
    private String oneWayEncryptionExcution(String str, String salt) throws EmptyStringException, NoSuchAlgorithmException {
		if(StringUtils.isEmpty(str)){
			throw new EmptyStringException("빈 문자열은 안됩니다.");
		}
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			return sb.toString() + (salt==null?ENCRPYTION_SALT:salt);
		}catch(NoSuchAlgorithmException e){
			throw new NoSuchAlgorithmException();
		}
	}
}
