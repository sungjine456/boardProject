package kr.co.person.common;

import java.io.File;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.person.pojo.OkCheck;

@Component
public class Common {
	@Value("${keyValue}") private String ENCRYPTION_KEY_OF_COOKIE;
	@Autowired kr.co.person.common.Message message;
	
	private final String EMAIL_FROM = "sungjine456@gmail.com";
	
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
	
	public OkCheck isEmail(String email) {
		if(StringUtils.isEmpty(email)){
			return new OkCheck(message.USER_NO_EMAIL, false);
		}
		if(Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$").matcher(email).matches()){
			return new OkCheck("", true);
		} else {
			return new OkCheck(message.USER_NO_EMAIL_FORMAT, false);
		}
    }
    
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
 
    public String cookieAesEncode(String str){
    	String iv = ENCRYPTION_KEY_OF_COOKIE.substring(0, 16);
    	try{
    		Key keySpec = AES256Util();
	        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
	 
	        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
	        return new String(Base64.encodeBase64(encrypted));
    	} catch(Exception e){
    		return "";
    	}
    }
 
    public String cookieAesDecode(String str){
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
    		return "";
    	}
    }
    
    public String cleanXss(String str){
    	if(StringUtils.isEmpty(str)){
    		return "";
    	}
    	str = str.replaceAll("&", "&amp;");
    	str = str.replaceAll("%2F", "");
    	str = str.replaceAll("\"","&#34;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("%", "&#37;");
        str = str.replaceAll("..\\\\", "");
        str = str.replaceAll("\0", " ");
        
        return str;
    }
    
    public String enter(String content){
    	content = content.replace("\r\n","<br/>");
    	
    	return content;
    }
    
    public Cookie addCookie(String key, String value){
    	DateTime date1 = DateTime.now();
    	DateTime date2 = date1.plusYears(1);
    	int expiredate = (int)(date2.getMillis()-date1.getMillis())/1000;
    	Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(expiredate);
	    return cookie;
    }

    public Cookie removeCookie(String key){
    	Cookie cookie = new Cookie(key, null);
    	cookie.setMaxAge(0);
    	return cookie;
    }
    
    public String createImg(MultipartFile file, String id, String kind) {
    	String[] strArray = file.getOriginalFilename().split("\\.");
		String ext = "";
		if(strArray.length == 2){
			ext = strArray[1];
		}
		String imgPath = "";
		String se = File.separator;
		if(StringUtils.equalsIgnoreCase(ext, "gif") || StringUtils.equalsIgnoreCase(ext, "jpg") || StringUtils.equalsIgnoreCase(ext, "jpeg") || StringUtils.equalsIgnoreCase(ext, "png")){
			Date date = new Date();
			String fileName = id + "_" + date.getTime() + "." + ext;
			String filePath = "C:"+se+"boardProject"+se+"img"+se+kind;
			imgPath = "img"+se+kind+se+fileName;
			File dayFile = new File(filePath);
			if(!dayFile.exists()){
			   dayFile.mkdirs();
			}
			try {
				file.transferTo(new File(filePath + se + fileName));
			} catch(Exception e) {
				e.printStackTrace(); 
			}
		}
		return imgPath;
	}
    
    public boolean sendMail(String toEmail, String subject, String message){
    	Properties props = new Properties();
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465"); 

        Authenticator auth = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() { 
                return new PasswordAuthentication(EMAIL_FROM, "fhrmdlswnd1!@"); 
            }
        };
        
        Session session = Session.getDefaultInstance(props, auth);
        MimeMessage mimeMessage = new MimeMessage(session); 
        try{
	        mimeMessage.setSender(new InternetAddress(EMAIL_FROM)); 
	        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail)); 
	        mimeMessage.setSubject(subject); 
	        mimeMessage.setContent(message, "text/plain;charset=UTF-8");

	        Transport.send(mimeMessage);
        } catch (MessagingException e){
        	return false;
        }
    	return true;
    }
}
