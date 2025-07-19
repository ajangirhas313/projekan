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
    public static boolean isLogin=false;

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
    
        return "menu1"; // Sesuaikan dengan nama HTML template Anda
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
    public String saveMenu(@ModelAttribute("menu") Menu menu,
            @RequestParam("file") MultipartFile file, Model model) {
        try {
            Path targetPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", "uploads",
                    file.getOriginalFilename());

            file.transferTo(targetPath.toFile());

            // Set URL atau path di dalam database
            String url = "http://localhost:8080/uploads/" + file.getOriginalFilename();

            // Set properti imagePath pada entitas Menu
            menu.setImagePath(url);

            // Simpan ke entitas Menu
            menuRepository.save(menu);

            model.addAttribute("message", "Berhasil menyimpan menu");
            return "uploadstatus";
        } catch (Exception e) {
            model.addAttribute("error", "Gagal menyimpan menu");
            e.printStackTrace(); // Handle exception sesuai kebutuhan
        }

        // return "uploadStatus";
        return "uploadstatus";
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
            @RequestParam("file") MultipartFile file,
            Model model) {
        try {
            Optional<Menu> optionalMenu = menuRepository.findById(id);
            if (optionalMenu.isPresent()) {
                Menu menu = optionalMenu.get();
                menu.setItemName(updatedMenu.getItemName());
                menu.setCategory(updatedMenu.getCategory());
                menu.setPrice(updatedMenu.getPrice());
                menu.setStock(updatedMenu.getStock());

                // Check if a new file is uploaded
                if (!file.isEmpty()) {
                    // Set tempat menyimpan file dalam proyek
                    Path targetPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static",
                            file.getOriginalFilename());

                    // Simpan file di dalam proyek
                    file.transferTo(targetPath.toFile());

                    // Set URL atau path di dalam database
                    String url = "http://localhost:8080/" + file.getOriginalFilename();
                    menu.setImagePath(url);
                }

                menuRepository.save(menu);
                model.addAttribute("message", "Berhasil menyimpan menu");

                return "uploadstatus";
            } else {
                return "redirect:/menu?error";
            }
        } catch (Exception e) {
            return "redirect:/edit-menu/" + id + "?error";
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
        if(MenuController.isLogin) {
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
        // return "home";
        return "menu1";
    }

    @GetMapping("/about")
    public String About(Model model) {
        return "about";
    }
}
