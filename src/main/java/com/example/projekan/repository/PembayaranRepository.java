package com.example.projekan.repository;

// PembayaranRepository.java
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projekan.model.Pembayaran;

public interface PembayaranRepository extends JpaRepository<Pembayaran, Long> {
}
