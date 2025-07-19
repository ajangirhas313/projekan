package com.example.projekan.controller;
// package com.example.projekan.controller;

// package com.ahtcoffee.crudcoffee.controllers;

// package com.ahtcoffe.crudcofee.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.projekan.model.Admin;

import com.example.projekan.service.AdminService;

import org.springframework.ui.Model;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/sign-in/admin")
    public String signInForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "Loginadmin";
    }

    @PostMapping("/sign-in/admin")
    public String signIn(@ModelAttribute("admin") Admin admin, Model model) {

        boolean pengecekan = adminService.pengecekanuser(admin.getUsername(), admin.getPassword());

        if (pengecekan) {
            return "redirect:/datamenu";
        } else {
            model.addAttribute("error", "Username atau password salah");
            return "redirect:/sign-in/admin";
        }
    }

}
