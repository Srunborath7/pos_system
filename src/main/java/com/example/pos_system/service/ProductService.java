package com.example.pos_system.service;

import com.example.pos_system.model.Product;
import com.example.pos_system.model.User;
import com.example.pos_system.repository.ProductRepository;
import com.example.pos_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final String uploadDir = System.getProperty("user.dir") + "/uploads/product-images";

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product, MultipartFile imageFile, String username) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        product.setCreatedBy(user);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImage(imageFile);
            product.setImageUrl(fileName);
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct, MultipartFile imageFile) throws IOException {
        Product existing = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setStock(updatedProduct.getStock());
        existing.setCategory(updatedProduct.getCategory());

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImage(imageFile);
            existing.setImageUrl(fileName);
        }

        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String originalFileName = imageFile.getOriginalFilename();
        String newFileName = System.currentTimeMillis() + "_" + originalFileName;
        File dest = new File(uploadFolder, newFileName);

        imageFile.transferTo(dest);

        return newFileName;
    }
}
