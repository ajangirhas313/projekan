package com.example.projekan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projekan.model.User;
import com.example.projekan.repository.UserRepository;
// import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public boolean pengecekanuser(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public boolean isUsernameAvailable(String username) {
        User user = userRepository.findByUsername(username);
        return user == null;
    }
    public void registerUser(User user) {
        try {
            userRepository.save(user);
            // Log jika registrasi berhasil
            logger.info("User registered successfully: {}", user.getUsername());
        } catch (Exception e) {
            // Log jika terjadi kesalahan saat menyimpan pengguna ke database
            logger.error("Error registering user {}: {}", user.getUsername(), e.getMessage());
        }
    }
    public boolean check(User user){
        User user2 = userRepository.findByUsername(user.getUsername());
        return user.getUsername().equals(user2.getUsername());
    }
}

