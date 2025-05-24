package com.project.ShopEasy.Service.email;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendInvoiceEmail(String to, String subject, String text, String pdfPath) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);

			FileSystemResource resource = new FileSystemResource(new File(pdfPath));
			helper.addAttachment("Invoice.pdf", resource);

			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
