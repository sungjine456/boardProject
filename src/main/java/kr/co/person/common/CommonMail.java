package kr.co.person.common;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CommonMail {
	@Autowired private JavaMailSender mailSender;
	
	@Value("${emailId}") private String EMAIL_ID;
	
    @Async
    public void sendMail(String toEmail, String title, String content) throws MessagingException{
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			messageHelper.setFrom(EMAIL_ID);
			messageHelper.setTo(toEmail);
			messageHelper.setSubject(title);
			messageHelper.setText("", content);
			
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new MessagingException();
   	    }
    }
}
