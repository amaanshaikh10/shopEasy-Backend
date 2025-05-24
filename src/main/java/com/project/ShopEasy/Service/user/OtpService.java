package com.project.ShopEasy.Service.user;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class OtpService implements IOtpService {

	private ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public String generateOtp(String email) {
		// TODO Auto-generated method stub
		String otp = String.valueOf(new Random().nextInt(900000) + 100000);
		otpStorage.put(email, otp);
		sendOtptoEmail(email, otp);
		return otp;
	}

	@Override
	public boolean verifyOtp(String email, String otp) {
		// TODO Auto-generated method stub
		return otpStorage.containsKey(email) && otpStorage.get(email).equals(otp);
	}

	@Override
	public void sendOtptoEmail(String email, String otp) {
		// TODO Auto-generated method stub

		try {
			MimeMessage massage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(massage, true);

			helper.setTo(email);
			helper.setSubject("Your OTP Code");
			helper.setText("Your OTP is :" + otp, true);

			mailSender.send(massage);
		} catch (MessagingException e) {
			// TODO: handle exception
			throw new RuntimeException("Failed to send OTP", e);
		}
	}

	@Override
	public void sendOrderConformation(String email, Long orderId) {
		// TODO Auto-generated method stub

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(email);
		message.setSubject("Order Conformation :" + orderId);
		message.setText("Thank you for order! Your order ID is " + orderId);

		mailSender.send(message);

	}

}
