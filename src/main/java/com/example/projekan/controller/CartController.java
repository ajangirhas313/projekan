package com.example.projekan.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.projekan.model.CartItem;
import com.example.projekan.model.Menu;
import com.example.projekan.model.User;
import com.example.projekan.repository.CartRepository;
import com.example.projekan.repository.MenuRepository;
import jakarta.servlet.http.HttpSession; // <-- Impor HttpSession

@Controller
public class CartController {

    private final MenuRepository menuRepository;
    private final CartRepository cartRepository;

    public CartController(MenuRepository menuRepository, CartRepository cartRepository) {
        this.menuRepository = menuRepository;
        this.cartRepository = cartRepository;
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long menuId, @RequestParam int portionCount, HttpSession session) {
        // Ambil user dari sesi
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/sign-in";
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        // ... (logika pengecekan stok tetap sama) ...

        Optional<CartItem> existingCartItemOpt = cartRepository.findByUserAndMenu_Id(currentUser, menuId);

        if (existingCartItemOpt.isPresent()) {
            CartItem existingCartItem = existingCartItemOpt.get();
            int newPortionCount = existingCartItem.getPortioncount() + portionCount;
            existingCartItem.setPortioncount(newPortionCount);
            existingCartItem.setTotalPayment(menu.getPrice() * newPortionCount);
            cartRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(currentUser); // <-- Hubungkan dengan user dari sesi
            cartItem.setItemName(menu.getItemName());
            cartItem.setPrice(menu.getPrice());
            cartItem.setPortioncount(portionCount);
            cartItem.setMenu(menu);
            cartItem.setTotalPayment(menu.getPrice() * portionCount);
            cartRepository.save(cartItem);
        }

        return "redirect:/menu1";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/sign-in";
        }

        List<CartItem> cartItems = cartRepository.findByUser(currentUser);
        model.addAttribute("cartItems", cartItems);

        int totalPayment = cartItems.stream()
                .mapToInt(CartItem::getTotalPayment)
                .sum();
        model.addAttribute("totalPayment", totalPayment);

        return "cart";
    }

    @GetMapping("/remove/{id}")
    public String removeCartItem(@PathVariable("id") Long id) {
        cartRepository.deleteById(id);
        return "redirect:/cart";
    }
}