package com.pets.petsgarden.repository;

import com.pets.petsgarden.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
