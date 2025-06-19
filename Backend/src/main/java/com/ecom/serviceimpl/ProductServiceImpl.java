package com.ecom.serviceimpl;

import com.ecom.entity.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        long now = System.currentTimeMillis(); // or Instant.now().getEpochSecond()
        product.setCreatedDate(now);
        product.setUpdatedDate(now);
        product.setId(UUID.randomUUID().toString());
        System.out.println(product);
        return productRepository.save(product);
    }


}
