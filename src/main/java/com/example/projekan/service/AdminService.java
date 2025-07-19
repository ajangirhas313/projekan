package com.example.projekan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projekan.model.Admin;
import com.example.projekan.model.User;
import com.example.projekan.repository.AdminRepository;
// import com.example.projekan.repository.UserRepository;
// import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public boolean pengecekanuser(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        return admin != null && admin.getPassword().equals(password);
    }

    public boolean isUsernameAvailable(String username) {
        Admin admin = adminRepository.findByUsername(username);
        return admin == null;
    }
    public void registerAdmin(Admin admin) {
        try {
            adminRepository.save(admin);
            // Log jika registrasi berhasil
            logger.info("User registered successfully: {}", admin.getUsername());
        } catch (Exception e) {
            // Log jika terjadi kesalahan saat menyimpan pengguna ke database
            logger.error("Error registering user {}: {}", admin.getUsername(), e.getMessage());
        }
    }
    public boolean check(User admin){
        Admin admin2 = adminRepository.findByUsername(admin.getUsername());
        return admin.getUsername().equals(admin2.getUsername());
    }
}

