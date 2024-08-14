package com.pets.petsgarden.repository;

import com.pets.petsgarden.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
