package com.example.projekan.controller;

import java.util.List;

// PembayaranController.java
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.projekan.model.CartItem;
import com.example.projekan.model.Menu;
import com.example.projekan.model.Pembayaran;
import com.example.projekan.repository.CartRepository;
import com.example.projekan.repository.MenuRepository;
import com.example.projekan.repository.PembayaranRepository;

@Controller
public class PembayaranController {

    private final PembayaranRepository pembayaranRepository;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository; 


    public PembayaranController(PembayaranRepository pembayaranRepository, CartRepository cartRepository, MenuRepository menuRepository) {
        this.pembayaranRepository = pembayaranRepository;
        this.cartRepository = cartRepository;
        this.menuRepository = menuRepository; // Pastikan ini telah diinisialisasi

    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        List<CartItem> cartItems = cartRepository.findAll();

        // Hitung total pembayaran dari entitas keranjang
        int totalPembayaran = cartItems.stream()
                .mapToInt(CartItem::getTotalPayment)
                .sum();

        Pembayaran pembayaran = new Pembayaran();
        pembayaran.setCartItems(cartItems);
        pembayaran.setTotalPembayaran(totalPembayaran);

        model.addAttribute("pembayaran", pembayaran);
        model.addAttribute("totalPembayaran", totalPembayaran);
        if(totalPembayaran==0) {
            return "gacheck";
        }

        return "pembayaran";
    }

    @PostMapping("/process-payment")
    public String processPayment(Pembayaran pembayaran) {
        for (CartItem cartItem : cartRepository.findAll()) {
            for (Menu menu : menuRepository.findAll()) {
                if (cartItem.getItemName().equals(menu.getItemName())) {
                    Menu menu2 = menu;
                    menu2.setStock(menu.getStock()-cartItem.getPortioncount());
                    menuRepository.save(menu2);
                    pembayaran.setTotalPembayaran(cartItem.getTotalPayment());
                }
            }
        }
    pembayaranRepository.save(pembayaran);

    cartRepository.deleteAll();

    return "konfirmasi-pembayaran";
    }


}
