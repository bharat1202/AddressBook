package org.example.addressbook;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // BCrypt instance for password encoding
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Registers a new user by checking for duplicate username, encoding the password, and saving the user.
    public String registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }

    // Authenticates a user and generates a JWT token if the credentials are valid.
    public UserDTO loginUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        if (!passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return new UserDTO(user.getUsername(), token);
    }

    // Retrieves all users from the database and caches the result in a cache named "users"
    @Cacheable("users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
