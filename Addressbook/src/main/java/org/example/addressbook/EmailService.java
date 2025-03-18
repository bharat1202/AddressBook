package org.example.addressbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Sends a registration confirmation email.
    public void sendRegistrationEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to Addressbook!");
        message.setText("Hi " + username + ",\n\nThank you for registering with Addressbook.\n\nBest regards,\nAddressbook Team");
        try {
            mailSender.send(message);
            log.info("Registration email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send registration email to {}: {}", toEmail, e.getMessage(), e);
        }
    }

    // Sends a password reset email with the reset token.
    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetUrl = "http://localhost:9090/api/auth/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Dear user,\n\nYou have requested to reset your password. " +
                "Please click on the following link to reset your password:\n" + resetUrl +
                "\n\nIf you did not request this, please ignore this email.\n\nRegards,\nAddressbook Team");
        try {
            mailSender.send(message);
            log.info("Password reset email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage(), e);
        }
    }
}
