package com.example.projekan.repository;

// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projekan.model.CartItem;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    // Optional<CartItem> findByMenu_Id(Long menuId);

}
