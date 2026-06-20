package com.base.basesetup.service;


public interface EmailService {

 void sendSimpleEmail(String toEmail, String subject, String body);

	

	/**
	 * Send an HTML formatted email
	 */
	void sendHtmlEmail(String fromEail, String toEmail, String subject, String htmlContent);



	void sendOtpEmail(String email, String employeeName, String otp);

}
