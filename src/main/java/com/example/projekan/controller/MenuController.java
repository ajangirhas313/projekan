package com.example.projekan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.projekan.model.Category;
import com.example.projekan.model.Menu;
import com.example.projekan.repository.CategoryRepository;
import com.example.projekan.repository.MenuRepository;

// import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
// import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Controller
public class MenuController {
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    public static boolean isLogin = false;

    public MenuController(MenuRepository menuRepository, CategoryRepository categoryRepository) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/menu/{categoryId}")
    public String getMenuByCategory(@PathVariable Long categoryId, Model model) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Menu> menuList = menuRepository.findByCategory(category);

        model.addAttribute("category", category);
        model.addAttribute("menuList", menuList);
        model.addAttribute("activeCategoryId", categoryId); // <-- TAMBAHKAN BARIS INI

        return "menu1";
    }

    @GetMapping("/datamenu")
    public String showMenu(Model model) {
        model.addAttribute("menuList", menuRepository.findAll());
        return "datamenu";
        // return "menu1";
    }

    @GetMapping("/add-menu")
    public String showAddMenuForm(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("menu", new Menu());
        return "addMenu";
    }

    @PostMapping("/save-menu")
    public String saveMenu(@ModelAttribute("menu") Menu menu, Model model) {
        try {
            // Karena imagePath sudah diisi dari form, kita bisa langsung menyimpannya.
            menuRepository.save(menu);
            model.addAttribute("message", "Berhasil menyimpan menu");
            return "uploadstatus";
        } catch (Exception e) {
            model.addAttribute("error", "Gagal menyimpan menu: " + e.getMessage());
            return "uploadstatus";
        }
    }

    @GetMapping("/update-menu/{id}")
    public String updateMenu(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        Optional<Menu> optionalMenu = menuRepository.findById(id);

        if (optionalMenu.isPresent()) {
            Menu menu = optionalMenu.get();
            model.addAttribute("menu", menu);
            return "updateMenu";
        } else {
            // Handle jika menu dengan ID tertentu tidak ditemukan
            return "redirect:/menu?error";
        }
    }

    @GetMapping("/delete-menu/{id}")
    public String deleteMenu(@PathVariable("id") Long id) {
        try {
            menuRepository.deleteById(id);
            return "redirect:/datamenu";
        } catch (Exception e) {
            // Handle exception sesuai kebutuhan
            return "redirect:/datamenu?error";
        }
    }

    @PostMapping("/update-menu/{id}")
    public String updateMenu(@PathVariable("id") Long id,
            @ModelAttribute("menu") Menu updatedMenu,
            Model model) {
        try {
            Optional<Menu> optionalMenu = menuRepository.findById(id);
            if (optionalMenu.isPresent()) {
                Menu menu = optionalMenu.get();
                // Salin semua properti yang diupdate dari form
                menu.setItemName(updatedMenu.getItemName());
                menu.setCategory(updatedMenu.getCategory());
                menu.setPrice(updatedMenu.getPrice());
                menu.setStock(updatedMenu.getStock());
                // Langsung update dengan imagePath (URL) dari form
                menu.setImagePath(updatedMenu.getImagePath());

                menuRepository.save(menu);
                model.addAttribute("message", "Berhasil memperbarui menu");
                return "uploadstatus";
            } else {
                model.addAttribute("error", "Menu tidak ditemukan");
                return "uploadstatus";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Gagal memperbarui menu: " + e.getMessage());
            return "uploadstatus";
        }
    }

    @GetMapping("/restoku")
    public String show(Model model) {
        model.addAttribute("menuList", menuRepository.findAll());
        return "restoku";
        // return "menu1";
    }

    @GetMapping("/home")
    public String showhome(Model model) {
        if (MenuController.isLogin) {
            model.addAttribute("menuList", menuRepository.findAll());
            return "home";
        }
        model.addAttribute("error", "Username atau password salah");
        return "redirect:/sign-in";
        // return "menu1";
    }

    @GetMapping("/menu1")
    public String showMenuUser(Model model) {
        model.addAttribute("menuList", menuRepository.findAll());
        model.addAttribute("activeCategoryId", 0L); // <-- TAMBAHKAN BARIS INI
        return "menu1";
    }

    @GetMapping("/about")
    public String About(Model model) {
        return "about";
    }

    // Tambahkan metode ini di dalam kelas MenuController

    @GetMapping("/menu/detail/{id}")
    public String showMenuDetail(@PathVariable("id") Integer id, Model model) {
        // Cari menu berdasarkan ID, jika tidak ada akan melempar error
        Menu menu = menuRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu Id:" + id));

        // Kirim data menu ke halaman
        model.addAttribute("menu", menu);

        // Arahkan ke template detail-menu.html
        return "detail-menu";
    }
}
