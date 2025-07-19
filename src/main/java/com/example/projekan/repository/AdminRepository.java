package com.example.projekan.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projekan.model.Admin;
// import com.example.projekan.model.User;

public interface AdminRepository extends JpaRepository<Admin, Long> {
        Admin findByUsername(String username);

}


