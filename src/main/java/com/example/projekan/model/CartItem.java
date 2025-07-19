package com.example.projekan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private Integer totalPayment; // Kolom untuk menyimpan total pembayaran dari item ini
    private String itemName;
    private Integer price;
    private Integer portioncount;
    @ManyToOne
    @JoinColumn(name = "pembayaran_id")
    private Pembayaran pembayaran;

    public void setPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
    }

    public Menu getMenu() {
        return menu;
    }

}