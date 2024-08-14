package com.pets.petsgarden.repository;

import com.pets.petsgarden.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
