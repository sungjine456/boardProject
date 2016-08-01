package kr.co.person.common;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;

import org.apache.tomcat.util.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
public class Common {
	public String passwordEncryption(String str){
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			return sb.toString() + "personProject";
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			return ""; 
		}
	}
	
	public String cookieValueEncryption(String str){
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			return sb.toString() + "cookiesAutoLogin";
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			return ""; 
		}
	}
	
	public boolean isEmail(String email) {
		return Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$").matcher(email).matches();
    }
    
	private Key AES256Util(String key) throws Exception {
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if(len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
 
        return keySpec;
    }
 
    public String cookieAesEncode(String key, String str){
    	String iv = key.substring(0, 16);
    	try{
    		Key keySpec = AES256Util(key);
	        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
	 
	        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
	        return new String(Base64.encodeBase64(encrypted));
    	} catch(Exception e){
    		return "";
    	}
    }
 
    public String cookieAesDecode(String key, String str){
    	String iv = key.substring(0, 16);
    	Cipher c;
    	byte[] byteStr;
    	try{
	    	Key keySpec = AES256Util(key);
	    	c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
	 
	        byteStr = Base64.decodeBase64(str.getBytes());
	        return new String(c.doFinal(byteStr),"UTF-8");
    	} catch(Exception e){
    		return "";
    	}
    }
    
    public String cleanXss(String str){
    	str = str.replaceAll("\"","&gt;");
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("%", "&#37;");    
        str = str.replaceAll("..\\\\", "");
        str = str.replaceAll("%2F", "");
        
        return str;
    }
    
    public String enter(String content){
    	content = content.replace("\r\n","<br/>");
    	
    	return content;
    }
    
    public Cookie addCookie(String key, String value){
    	DateTime date1 = DateTime.now();
    	DateTime date2 = date1.plusYears(1);
    	Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge((int)((date2.getMillis()-date1.getMillis())/1000));
	    return cookie;
    }

    public Cookie removeCookie(String key){
    	Cookie cookie = new Cookie(key, null);
    	cookie.setMaxAge(0);
    	return cookie;
    }
}
