package kr.co.person.common;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CommonMail {
	private static final Logger log = LoggerFactory.getLogger(Common.class);
	
	@Autowired private JavaMailSender mailSender;
	
	@Value("${emailId}") private String EMAIL_ID;
	
    @Async
    public void sendMail(String toEmail, String title, String content){
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			messageHelper.setFrom(EMAIL_ID);
			messageHelper.setTo(toEmail);
			messageHelper.setSubject(title);
			messageHelper.setText("", content);
			
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			log.error(e.getMessage());
   	    }
    }
}
