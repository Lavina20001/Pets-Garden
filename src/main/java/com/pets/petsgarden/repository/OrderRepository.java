package com.pets.petsgarden.repository;

import com.pets.petsgarden.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
