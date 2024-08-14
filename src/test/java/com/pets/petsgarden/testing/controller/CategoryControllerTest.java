package com.pets.petsgarden.testing.controller;

import com.pets.petsgarden.controller.CategoryController;
import com.pets.petsgarden.dto.CategoryDto;
import com.pets.petsgarden.exception.UserNotFoundException;
import com.pets.petsgarden.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setCategoryname("Dog Food");
        categoryDto.setDescription("Food for dogs");
    }

    @Test
    void getAllCategory() throws Exception {
        List<CategoryDto> categoryList = Arrays.asList(categoryDto);
        when(categoryService.getAllCategory()).thenReturn(categoryList);

        mockMvc.perform(get("/categories/getAllCategory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryname").value("Dog Food"));
    }

    @Test
    void getAllCategory_NoCategories() throws Exception {
        when(categoryService.getAllCategory()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/categories/getAllCategory"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getCategoryById_CategoryExists() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryDto);

        mockMvc.perform(get("/categories/getCategoryById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryname").value("Dog Food"));
    }

    @Test
    void getCategoryById_CategoryDoesNotExist() throws Exception {
        when(categoryService.getCategoryById(2L)).thenThrow(new UserNotFoundException("Category not found"));

        mockMvc.perform(get("/categories/getCategoryById/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Category not found"));
    }

    @Test
    void addCategory() throws Exception {
        doNothing().when(categoryService).addCategory(any(CategoryDto.class));

        mockMvc.perform(post("/categories/addCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryname\":\"Dog Food\", \"description\":\"Food for dogs\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category added successfully!"));
    }

    @Test
    void updateCategory_CategoryExists() throws Exception {
        doNothing().when(categoryService).updateCategory(anyLong(), any(CategoryDto.class));

        mockMvc.perform(put("/categories/updateCategory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryname\":\"Dog Food\", \"description\":\"Food for dogs\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category updated successfully!"));
    }

    @Test
    void updateCategory_CategoryDoesNotExist() throws Exception {
        doThrow(new UserNotFoundException("Category not found")).when(categoryService).updateCategory(anyLong(), any(CategoryDto.class));

        mockMvc.perform(put("/categories/updateCategory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryname\":\"Dog Food\", \"description\":\"Food for dogs\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_CategoryExists() throws Exception {
        doNothing().when(categoryService).deleteCategory(anyLong());

        mockMvc.perform(delete("/categories/deleteCategory/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category deleted successfully!"));
    }

    @Test
    void deleteCategory_CategoryDoesNotExist() throws Exception {
        doThrow(new UserNotFoundException("Category not found")).when(categoryService).deleteCategory(anyLong());

        mockMvc.perform(delete("/categories/deleteCategory/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_InvalidIDFormat() throws Exception {
        mockMvc.perform(delete("/categories/deleteCategory/invalid"))
                .andExpect(status().isBadRequest());
    }
}
