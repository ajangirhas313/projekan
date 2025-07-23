package com.example.projekan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

// Impor yang diperlukan
import com.example.projekan.model.User;
import com.example.projekan.repository.UserRepository;
import com.example.projekan.service.UserService;
import jakarta.servlet.http.HttpSession; // <-- Impor HttpSession

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // Metode logout sekarang menggunakan HttpSession
    @GetMapping("/")
    public String logout(HttpSession session) {
        session.invalidate(); // <-- Hapus semua data dari sesi
        MenuController.isLogin = false;
        return "LoginChoice";
    }
    
    // ... (metode lainnya seperti showLoginChoice, signInForm, signUpForm) ...

    @PostMapping("/sign-in")
    public String signIn(@ModelAttribute("user") User user, Model model, HttpSession session) { // <-- Tambahkan HttpSession
        
        boolean pengecekan = userService.pengecekanuser(user.getUsername(), user.getPassword());
        
        if (pengecekan) {
            // Temukan data lengkap user dari database
            User loggedInUser = userRepository.findByUsername(user.getUsername());
            
            // Simpan seluruh objek user ke dalam sesi
            session.setAttribute("loggedInUser", loggedInUser);
            
            MenuController.isLogin = true;
            return "redirect:/home";

        } else {
            model.addAttribute("error", "Username atau password salah");
            return "redirect:/sign-in?error=true";   
        }
    }
    
    // ... (sisa metode di UserController tetap sama) ...
    @GetMapping("/sign-in")
    public String signInForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("user", new User());
        return "daftar";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("user") User user, Model model) {
        if (!userService.isUsernameAvailable(user.getUsername())) {
            model.addAttribute("error", "Username sudah digunakan");
            return "redirect:/sign-up";
        }
        userService.registerUser(user);
        return "redirect:/sign-in";
    }

    @GetMapping("/datauser")
    public String showMenu(Model model) {
        model.addAttribute("userList", userRepository.findAll());
        return "datauser";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable(value = "id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/datauser";
    }
}