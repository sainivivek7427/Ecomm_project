package com.ecom.serviceimpl;

import com.ecom.entity.CartItem;
import com.ecom.entity.CartItemRequestDTO;
import com.ecom.entity.Product;
import com.ecom.repository.CartItemRepository;
import com.ecom.repository.ProductRepository;
import com.ecom.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public CartItem addToCart(String userId, CartItemRequestDTO request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = new CartItem();
        item.setId(UUID.randomUUID().toString());
        item.setUserId(userId);
        item.setProduct(product);
        item.setQuantity(request.getQuantity());
        item.setCreatedDate(System.currentTimeMillis());

        return cartItemRepository.save(item);
    }

    @Override
    public List<CartItem> getUserCartItems(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public void removeCartItem(String cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
