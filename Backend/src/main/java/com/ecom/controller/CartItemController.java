package com.ecom.controller;

import com.ecom.entity.CartItem;
import com.ecom.entity.CartItemRequestDTO;
import com.ecom.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestParam String userId,
                                              @RequestBody CartItemRequestDTO request) {
        CartItem cartItem = cartItemService.addToCart(userId, request);
        return ResponseEntity.ok(cartItem);
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> getCartItems(@RequestParam String userId) {
        List<CartItem> cartItems = cartItemService.getUserCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@PathVariable String cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.ok("Item removed from cart successfully");
    }
}
