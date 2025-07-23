package com.example.projekan.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projekan.model.CartItem;
import com.example.projekan.model.Menu;
import com.example.projekan.model.Pembayaran;
import com.example.projekan.model.User;
import com.example.projekan.repository.CartRepository;
import com.example.projekan.repository.MenuRepository;
import com.example.projekan.repository.PembayaranRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class PembayaranController {

    private final PembayaranRepository pembayaranRepository;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository; 

    public PembayaranController(PembayaranRepository pembayaranRepository, CartRepository cartRepository, MenuRepository menuRepository) {
        this.pembayaranRepository = pembayaranRepository;
        this.cartRepository = cartRepository;
        this.menuRepository = menuRepository;
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/sign-in";
        }

        List<CartItem> cartItems = cartRepository.findByUser(currentUser);

        if (cartItems.isEmpty()) {
            return "redirect:/gacheck"; // Arahkan ke halaman jika keranjang kosong
        }

        int totalPembayaran = cartItems.stream()
                .mapToInt(CartItem::getTotalPayment)
                .sum();

        Pembayaran pembayaran = new Pembayaran();
        pembayaran.setTotalPembayaran(totalPembayaran);

        model.addAttribute("pembayaran", pembayaran);
        model.addAttribute("cartItems", cartItems); // <-- Kirim item keranjang ke view
        model.addAttribute("totalPayment", totalPembayaran);

        return "pembayaran";
    }

    @PostMapping("/process-payment")
    public String processPayment(@ModelAttribute Pembayaran pembayaran, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/sign-in";
        }

        List<CartItem> cartItems = cartRepository.findByUser(currentUser);
        
        // Proses pengurangan stok
        for (CartItem cartItem : cartItems) {
            Menu menu = cartItem.getMenu();
            int newStock = menu.getStock() - cartItem.getPortioncount();
            if (newStock < 0) {
                // Handle jika stok tiba-tiba tidak cukup
                return "redirect:/cart?error=stock_insufficient";
            }
            menu.setStock(newStock);
            menuRepository.save(menu);
        }
        
        // Simpan data pembayaran
        pembayaran.setCartItems(cartItems);
        Pembayaran savedPembayaran = pembayaranRepository.save(pembayaran);
        
        // Kaitkan setiap item keranjang dengan pembayaran yang sudah disimpan
        for (CartItem item : cartItems) {
            item.setPembayaran(savedPembayaran);
            cartRepository.save(item);
        }

        // Kosongkan keranjang pengguna setelah pembayaran berhasil
        // (Kita hapus satu per satu karena cartItems sekarang terikat ke pembayaran)
        // Sebenarnya, lebih baik membuat entitas baru "OrderItem" daripada mengikat CartItem
        // Tapi untuk saat ini, kita akan hapus saja dari tampilan keranjang
        // Untuk implementasi riil, data ini akan menjadi "Order History"
        cartRepository.deleteAll(cartItems);

        // Kirim data pembayaran ke halaman konfirmasi menggunakan Flash Attributes
        redirectAttributes.addFlashAttribute("pembayaran", savedPembayaran);

        return "redirect:/konfirmasi-pembayaran";
    }
    
    // Halaman untuk menampilkan konfirmasi
    @GetMapping("/konfirmasi-pembayaran")
    public String showConfirmationPage() {
        return "konfirmasi-pembayaran";
    }
    
    // Halaman jika checkout tapi keranjang kosong
    @GetMapping("/gacheck")
    public String showEmptyCheckoutPage() {
        return "gacheck";
    }
}