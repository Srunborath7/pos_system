package com.example.pos_system.controller;

import com.example.pos_system.dto.CategoryRequest;
import com.example.pos_system.model.Category;
import com.example.pos_system.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category,
                                                   Authentication authentication) {
        String username = authentication.getName();
        Category savedCategory = categoryService.createCategory(category, username);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id,
                                                   @RequestBody CategoryRequest request,
                                                   Authentication authentication) {
        String username = authentication.getName();
        Category updatedCategory = categoryService.updateCategory(id, request, username);
        return ResponseEntity.ok(updatedCategory);
    }

    // Delete Category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
