package org.example.addressbook;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResetPasswordService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isPresent()) {
            PasswordResetToken resetToken = tokenOpt.get();

            if (resetToken.isExpired()) {
                return false;
            }

            User user = resetToken.getUser();
            user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));

            userRepository.save(user);
            tokenRepository.delete(resetToken); // Delete token after reset

            return true;
        }

        return false;
    }
}
