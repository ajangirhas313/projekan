package com.example.projekan.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projekan.model.CartItem;
import com.example.projekan.model.User;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    // Mengambil semua item di keranjang milik user tertentu
    List<CartItem> findByUser(User user);

    // Mencari item spesifik di keranjang milik user tertentu berdasarkan ID menu
    Optional<CartItem> findByUserAndMenu_Id(User user, Long menuId);
}