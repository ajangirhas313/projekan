package com.example.projekan.controller;

import java.util.List;

// package com.ahtcoffee.crudcoffee.controllers;

// package com.ahtcoffe.crudcofee.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.projekan.model.Menu;
// import com.example.projekan.model.Admin;
import com.example.projekan.model.User;
import com.example.projekan.repository.UserRepository;
import com.example.projekan.service.UserService;

import org.springframework.ui.Model;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String Home() {
        MenuController.isLogin=false;
        return "LoginChoice";
    }

    public class LoginChoiceController {
        @GetMapping("/login-choice")
        public String showLoginChoice() {
            return "LoginChoice"; // Sesuaikan dengan nama HTML template Anda
        }
    }

    @GetMapping("/sign-in")
    public String signInForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/sign-in")
    public String signIn(@ModelAttribute("user") User user, Model model) {
        
        boolean pengecekan = userService.pengecekanuser(user.getUsername(), user.getPassword());
        if (pengecekan) {
            // return "redirect:/home";
            
            // List<User> users = userRepository.findAll();
        // boolean isLogin = false;
        for (User user2 : userRepository.findAll()) {
            if(user2.getUsername().equals(user.getUsername()) && user2.getPassword().equals(user.getPassword())) {
                MenuController.isLogin = true;
                user.setId(user2.getId());
                break;
            }
        }
        }

        if(MenuController.isLogin) {
            userRepository.deleteAll();
            userRepository.save(user);
            return "redirect:/home";
        } 
        model.addAttribute("error", "Username atau password salah");
        return "redirect:/sign-in";   
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
        List<User> users = userRepository.findAll();
        model.addAttribute("userList", users);
        return "datauser";
        // return "menu1";
    }

    @GetMapping("/add-User")
    public String showAddUserForm(Model model) {
        model.addAttribute("menu", new Menu());
        return "addMenu";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable(value = "id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/datauser";
    }
}
