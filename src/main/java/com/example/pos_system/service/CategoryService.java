package com.example.pos_system.service;

import com.example.pos_system.dto.CategoryRequest;
import com.example.pos_system.model.Category;
import com.example.pos_system.model.User;
import com.example.pos_system.repository.CategoryRepository;
import com.example.pos_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Category createCategory(Category category, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        category.setCreatedBy(user);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
    }

    public Category updateCategory(Long id, CategoryRequest request, String username) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setCreatedBy(user);

        return categoryRepository.save(category);
    }

    // Delete
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
