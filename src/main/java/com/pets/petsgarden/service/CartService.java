package com.pets.petsgarden.service;

import com.pets.petsgarden.dto.CartDto;
import com.pets.petsgarden.entity.Cart;
import com.pets.petsgarden.entity.Product;
import com.pets.petsgarden.entity.User;
import com.pets.petsgarden.repository.CartRepository;
import com.pets.petsgarden.repository.ProductRepository;
import com.pets.petsgarden.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @PreAuthorize("hasAuthority('USER')")
    public CartDto addCart(CartDto cartDto) {
        Cart cart = new Cart();

        Optional<User> user = userRepository.findById(cartDto.getUserId());
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        cart.setUser(user.get());

        Map<Product, Integer> products = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : cartDto.getProductQuantities().entrySet()) {
            Optional<Product> product = productRepository.findById(entry.getKey());
            if (!product.isPresent()) {
                throw new RuntimeException("Product not found: " + entry.getKey());
            }
            products.put(product.get(), entry.getValue());
        }
        cart.setProducts(products);

        calculateCartTotals(cart);

        cart = cartRepository.save(cart);
        return convertToDto(cart);
    }

    @PreAuthorize("hasAuthority('USER')")
    public CartDto updateCart(Long id, CartDto cartDto) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (!cartOptional.isPresent()) {
            throw new RuntimeException("Cart not found");
        }
        Cart cart = cartOptional.get();

        Optional<User> user = userRepository.findById(cartDto.getUserId());
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        cart.setUser(user.get());

        Map<Product, Integer> products = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : cartDto.getProductQuantities().entrySet()) {
            Optional<Product> product = productRepository.findById(entry.getKey());
            if (!product.isPresent()) {
                throw new RuntimeException("Product not found: " + entry.getKey());
            }
            products.put(product.get(), entry.getValue());
        }
        cart.setProducts(products);

        calculateCartTotals(cart);

        cart = cartRepository.save(cart);
        return convertToDto(cart);
    }

    @PreAuthorize("hasAuthority('USER')")
    public CartDto getCartById(Long id) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (!cartOptional.isPresent()) {
            throw new RuntimeException("Cart not found");
        }
        return convertToDto(cartOptional.get());
    }

    @PreAuthorize("hasAuthority('USER')")
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    public Set<CartDto> getAllCarts() {
        return cartRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    private void calculateCartTotals(Cart cart) {
        double totalPrice = 0;
        int totalItem = 0;
        for (Map.Entry<Product, Integer> entry : cart.getProducts().entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
            totalItem += entry.getValue();
        }
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
    }

    public CartDto convertToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUser().getId());
        cartDto.setProductQuantities(cart.getProducts().entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getId(), Map.Entry::getValue)));
        cartDto.setTotalItem(cart.getTotalItem());
        cartDto.setTotalPrice(cart.getTotalPrice());
        return cartDto;
    }
}
