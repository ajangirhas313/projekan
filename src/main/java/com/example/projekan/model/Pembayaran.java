package com.example.projekan.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Pembayaran {
    @Id     
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alamat;
    private String nama;
    private int totalPembayaran;
    
    // --> TAMBAHKAN FIELD BARU <--
    private String metodePembayaran;
    private String nomorRekening; // Untuk menyimpan No. Rek/E-wallet

    @OneToMany(mappedBy = "pembayaran", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
}