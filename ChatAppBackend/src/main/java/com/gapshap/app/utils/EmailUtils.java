package com.gapshap.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender emailSender;
	
	@Value("${company.email}")
	private String from;

	public Boolean sendEmail(String to,String name,String otp) {

		String subject="Email Verification";
		
		// Load the HTML template
        ClassPathResource htmlResource = new ClassPathResource("templates/VerificationEmail.html");
        InputStream inputStream;
		try {
			inputStream = htmlResource.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String htmlContent = scanner.hasNext() ? scanner.next() : "";
		
        htmlContent = htmlContent.replace("[UserName]", name);
        htmlContent = htmlContent.replace("[verification_link]", "http://localhost:4200/login?email="+to+"&&value="+otp);
		htmlContent = htmlContent.replace("[OTP]", otp);
        
        
		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlContent,true);
			helper.setFrom(from);
			emailSender.send(message);
		} catch (MessagingException  | MailSendException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			return false;
		}

		

//		FileSystemResource file = new FileSystemResource(new File("C:/Users/Hp/Downloads/New-file.gif"));
//		helper.addAttachment("Invoice", file);	
		return true;
	}

//	
public Boolean sendEmails(String to,String name,String otp) {
	String subject="Email Verification";
		
		String host = "smtp.gmail.com";
		
		// setting system properties
		Properties properties = System.getProperties();
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth","true");
		
		//step 1 : to get session object
		Session session=Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("javatesting406@gmail.com","tcukadgxyxyimnvo");
			}
			
		});
		session.setDebug(true);
		
		//Step 2 : compose message[text,multimedia]
		MimeMessage mime = new MimeMessage(session);
		
		// Load the HTML template
        ClassPathResource htmlResource = new ClassPathResource("templates/VerificationEmail.html");
		InputStream inputStream;
		try {
			inputStream = htmlResource.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
		    String htmlContent = scanner.hasNext() ? scanner.next() : "";
			
		    htmlContent = htmlContent.replace("[UserName]", name);
		    htmlContent = htmlContent.replace("[verification_link]", "http://localhost:4200/login?email="+to+"&&value="+otp);
			htmlContent = htmlContent.replace("[OTP]", otp);
		    
		
		// send
		

		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mime, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlContent,true);
			helper.setFrom(from);
			Transport.send(mime);
		} catch (MessagingException  | MailSendException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			return false;
		}
		
		return false;		
		
	}
}
