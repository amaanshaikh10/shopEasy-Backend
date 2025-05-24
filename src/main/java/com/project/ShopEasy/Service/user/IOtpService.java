package com.project.ShopEasy.Service.user;

public interface IOtpService {

	String generateOtp(String email);

	boolean verifyOtp(String email, String otp);

	void sendOtptoEmail(String email, String otp);

	void sendOrderConformation(String email, Long orderId);

}
