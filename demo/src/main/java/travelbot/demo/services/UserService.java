package travelbot.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelbot.demo.entities.User;
import travelbot.demo.exceptions.BadRequestException;
import travelbot.demo.exceptions.NotFoundException;
import travelbot.demo.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
    }

    public User create(String username, String email, String passwordHash) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username already in use");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already in use");
        }
        return userRepository.save(new User(username, email, passwordHash));
    }
}
