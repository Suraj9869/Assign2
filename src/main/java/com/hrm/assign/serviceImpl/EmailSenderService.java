package com.hrm.assign.serviceImpl;

import java.util.Properties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderService {
	
	Logger log = LoggerFactory.getLogger(EmailSenderService.class);
	
	public boolean sendEmail(String to, String subject, String body) {
		
		String from = "support@anzz.com";
		
		String host = "sandbox.smtp.mailtrap.io";
		
		String username = "f29d5a9d70091d";
		String password = "728d610a32c86b";
		
		Properties properties = System.getProperties();
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", 465);
		properties.put("mail.smtp.ssl.enable", "false");
		properties.put("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(properties, new Authenticator() {
		
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		try {
			Message message = new MimeMessage(session);
			
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setFrom(new InternetAddress(from));
			message.setSubject(subject);
			message.setText(body);
			message.setHeader("Content-Type", "text/html");
			Transport.send(message);
			
			return true;
			
		}
		catch (Exception e) {
			log.error("Error in sending a mail {}" , e.getMessage());
			return false;
		}
		
		
		
	}
}
