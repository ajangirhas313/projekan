package com.example.projekan.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.projekan.model.CartItem;
import com.example.projekan.model.Menu;
import com.example.projekan.repository.CartRepository;
import com.example.projekan.repository.MenuRepository;

@Controller
public class CartController {

    private final MenuRepository menuRepository;
    private final CartRepository cartRepository;

    public CartController(MenuRepository menuRepository, CartRepository cartRepository) {
        this.menuRepository = menuRepository;
        this.cartRepository = cartRepository;
    }


    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long menuId, @RequestParam int portionCount) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        if (menu.getStock()<0) {
            return "menu1";
        }
        if ( portionCount>menu.getStock()){
            return "redirect:/menu1";
        }
        CartItem cartItem = new CartItem();
        cartItem.setItemName(menu.getItemName());
        cartItem.setPrice(menu.getPrice());
        cartItem.setPortioncount(portionCount);
        cartItem.setMenu(menu);
    
        cartItem.setTotalPayment(menu.getPrice() * portionCount);
    
        cartRepository.save(cartItem);
    
        return "redirect:/menu1"; 
    }




    @GetMapping("/cart")
    public String viewCart(Model model) {
        List<CartItem> cartItems = cartRepository.findAll();
        model.addAttribute("cartItems", cartItems);

        int totalPayment = cartItems.stream()
                .mapToInt(cartItem -> cartItem.getPrice() * cartItem.getPortioncount())
                .sum();
        model.addAttribute("totalPayment", totalPayment);

        return "cart"; 
    }

    @GetMapping("/remove/{id}")
    public String removeCaString(@PathVariable("id")Long id){
        cartRepository.deleteById(id);
        return "redirect:/cart";
    }

}