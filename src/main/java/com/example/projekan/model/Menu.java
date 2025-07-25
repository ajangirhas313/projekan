package com.example.projekan.model;

import jakarta.persistence.Column; // <-- Impor ini
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String itemName;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    private Integer price;

    private Integer stock;

    private String imagePath; 
    
    // --> TAMBAHKAN KOLOM INI UNTUK DESKRIPSI <--
    @Column(columnDefinition = "TEXT") // Menggunakan TEXT agar bisa menampung deskripsi yang panjang
    private String description;
}