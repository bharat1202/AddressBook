package org.example.addressbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    // Generates a reset token for the user associated with the given email.
    public void generateResetToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            long deletedCount = tokenRepository.deleteByUser(user);
            System.out.println("Deleted " + deletedCount + " previous tokens for user " + user.getUsername());

            PasswordResetToken resetToken = new PasswordResetToken(user);
            tokenRepository.save(resetToken);
            emailService.sendPasswordResetEmail(user.getEmail(), resetToken.getToken());
        } else {
            System.out.println("No user found with email: " + email);
        }
    }
}
