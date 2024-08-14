package com.pets.petsgarden.repository;

import com.pets.petsgarden.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
