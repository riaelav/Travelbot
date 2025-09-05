package travelbot.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import travelbot.demo.entities.User;
import travelbot.demo.exceptions.BadRequestException;
import travelbot.demo.exceptions.NotFoundException;
import travelbot.demo.payloads.UserRegistrationDTO;
import travelbot.demo.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Registrazione nuovo utente
    public User register(UserRegistrationDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new BadRequestException("Username already in use");
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Email already in use");
        }

        User newUser = new User(
                dto.username(),
                dto.email(),
                passwordEncoder.encode(dto.password()) // password cifrata
        );

        return userRepository.save(newUser);
    }

    //Cerca per username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
    }

    //Cerca per email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    //Cerca per id
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }
}
