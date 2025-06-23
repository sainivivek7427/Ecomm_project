package com.ecom.service;

import com.ecom.entity.CartItem;
import com.ecom.entity.CartItemRequestDTO;

import java.util.List;

public interface CartItemService {
    CartItem addToCart(String userId, CartItemRequestDTO request);
    List<CartItem> getUserCartItems(String userId);
    void removeCartItem(String cartItemId);
}