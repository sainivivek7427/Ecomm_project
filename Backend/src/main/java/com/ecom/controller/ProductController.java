package com.ecom.controller;

import com.ecom.entity.Category;
import com.ecom.entity.Product;
import com.ecom.repository.CategoryRepository;
import com.ecom.repository.ProductRepository;
import com.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;


    //add Data in db bulk using the CSV file
    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<Product> products = new ArrayList<>();
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length < 12) continue; // ✅ Fix 1: Now correct

                String categoryName = data[9].trim();
                // ✅ Fix 2
                System.out.println("category name "+categoryName);
                Category category = categoryRepository.findByName(categoryName).orElseThrow(()-> new NullPointerException("Ddata not found"));
                if (category == null) {
                    System.out.println("Category not found: " + categoryName);
                    continue;
                }

                Product product = new Product();
                product.setId(UUID.randomUUID().toString());
                product.setName(data[0].trim());
                product.setPrice(Double.parseDouble(data[1].trim()));
                product.setDiscountPercent(Double.parseDouble(data[2].trim()));
                product.setDiscountPrice(Double.parseDouble(data[3].trim()));
                product.setDescription(data[4].trim());
                product.setBrand(data[5].trim());
                product.setStockQuantity(Integer.parseInt(data[6].trim()));
                product.setImageUrl(data[7].trim());
                product.setIsActive(Boolean.parseBoolean(data[8].trim()));
                product.setCategoryId(category.getId()); // ✅ categoryName → categoryId
                product.setCreatedDate(Long.parseLong(data[10].trim())); // ✅ Fix 3
                product.setUpdatedDate(Long.parseLong(data[11].trim())); // ✅ Fix 4

                products.add(product);
            }

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No valid products to upload.");
            }

            productRepository.saveAll(products);
            return ResponseEntity.ok("CSV data uploaded successfully. Total: " + products.size());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }


    // Get All Products
    @GetMapping("/get")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Get Product by ID
    @GetMapping("/get/{pid}")
    public ResponseEntity<?> getProductById(@PathVariable String pid) {
        try {
            Product product = productService.getProductById(pid);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Update Product by ID (only price & discount)
    @PutMapping("/update/{pid}")
    public ResponseEntity<?> updateProduct(@PathVariable String pid, @RequestBody Product updatedProduct) {
        try {
            Product updated = productService.updateProduct(pid, updatedProduct);
            System.out.println("Product Updated Successfully");
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Delete Product by ID
    @DeleteMapping("/delete/{pid}")
    public ResponseEntity<String> deleteProduct(@PathVariable String pid) {
        try {
            productService.deleteProduct(pid);
            return ResponseEntity.ok("Product deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}
