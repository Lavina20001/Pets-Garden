package com.pets.petsgarden.testing.service;

import com.pets.petsgarden.dto.CategoryDto;
import com.pets.petsgarden.entity.Category;
import com.pets.petsgarden.exception.UserNotFoundException;
import com.pets.petsgarden.repository.CategoryRepository;
import com.pets.petsgarden.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;

    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setCategoryname("Dog Food");
        category.setDescription("Food for dogs");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setCategoryname("Dog Food");
        categoryDto.setDescription("Food for dogs");
    }

    @Test
    void addCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        categoryService.addCategory(categoryDto);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void getAllCategory() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));

        List<CategoryDto> categoryDtos = categoryService.getAllCategory();

        assertEquals(1, categoryDtos.size());
        assertEquals("Dog Food", categoryDtos.get(0).getCategoryname());
    }

    @Test
    void getAllCategory_NoCategories() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<CategoryDto> categoryDtos = categoryService.getAllCategory();

        assertTrue(categoryDtos.isEmpty());
    }

    @Test
    void getCategoryById_CategoryExists() throws UserNotFoundException {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDto foundCategoryDto = categoryService.getCategoryById(1L);

        assertEquals("Dog Food", foundCategoryDto.getCategoryname());
    }

    @Test
    void getCategoryById_CategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void updateCategory_CategoryExists() throws UserNotFoundException {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryDto.setCategoryname("Cat Food");
        categoryDto.setDescription("Food for cats");

        categoryService.updateCategory(1L, categoryDto);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_CategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> categoryService.updateCategory(1L, categoryDto));
    }

    @Test
    void deleteCategory_CategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCategory_CategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }


    @Test
    void deleteCategory_InvalidId() {
        doThrow(new EntityNotFoundException("Category not found with id 0")).when(categoryRepository).deleteById(0L);

        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(0L));
    }
}
