package com.pets.petsgarden.testing.service;

import com.pets.petsgarden.dto.ProductDto;
import com.pets.petsgarden.entity.Category;
import com.pets.petsgarden.entity.Product;
import com.pets.petsgarden.repository.CategoryRepository;
import com.pets.petsgarden.repository.ProductRepository;
import com.pets.petsgarden.service.ProductService;
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

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private Product product;

    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setProductName("Dog Food");
        product.setDescription("Food for dogs");
        product.setPrice(25.99);
        product.setQuantity(10);
        product.setAvailability("In Stock");

        Category category = new Category();
        category.setId(1L);
        product.setCategory(category);

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setProductName("Dog Food");
        productDto.setDescription("Food for dogs");
        productDto.setPrice(25.99);
        productDto.setQuantity(10);
        productDto.setAvailability("In Stock");
        productDto.setCategoryId(1L);
    }

    @Test
    void addProduct() {
        when(categoryRepository.findById(productDto.getCategoryId())).thenReturn(Optional.of(new Category()));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.addProduct(productDto);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<ProductDto> productDtos = productService.getAllProducts();

        assertEquals(1, productDtos.size());
        assertEquals("Dog Food", productDtos.get(0).getProductName());
    }

    @Test
    void getAllProducts_NoProducts() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProductDto> productDtos = productService.getAllProducts();

        assertTrue(productDtos.isEmpty());
    }

    @Test
    void getProductById_ProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto result = productService.getProductById(1L);

        assertEquals("Dog Food", result.getProductName());
    }

    @Test
    void getProductById_ProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(1L));
    }

    @Test
    void updateProduct_ProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(productDto.getCategoryId())).thenReturn(Optional.of(new Category()));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.updateProduct(1L, productDto);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_ProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.updateProduct(1L, productDto));
    }

    @Test
    void deleteProduct_ProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(any(Product.class));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    void deleteProduct_ProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.deleteProduct(1L));
    }
    @Test
    void deleteProduct_InvalidId() {
        doThrow(new RuntimeException("Product not found with id 0")).when(productRepository).deleteById(0L);

        assertThrows(RuntimeException.class, () -> productService.deleteProduct(0L));
    }
}
