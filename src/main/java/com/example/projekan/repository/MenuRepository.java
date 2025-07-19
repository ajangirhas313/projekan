package com.example.projekan.repository;

import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;

// import com.example.projekan.model.Menu;

// public interface MenuRepository extends JpaRepository<Menu, Integer>{
    
// }

// MenuRepository.java
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projekan.model.Category;
import com.example.projekan.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByitemNameContainingIgnoreCase(String itemName);
    List<Menu> findByCategory(Category category);
}

