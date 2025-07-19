package com.example.projekan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projekan.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Tambahkan metode kustom jika diperlukan
}
