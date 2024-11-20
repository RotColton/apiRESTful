package com.prueba_tecnica.API_RESTFul.service;

import com.prueba_tecnica.API_RESTFul.exception.ResourceNotFoundException;
import com.prueba_tecnica.API_RESTFul.entity.Product;
import com.prueba_tecnica.API_RESTFul.repository.ProductRepository;
import com.prueba_tecnica.API_RESTFul.specification.ProductSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1, "Dell Latitud","", 2344.66f, "Laptop");

    }

    @Test
    void saveProduct_WithDescription(){
        product.setDescription("Razen 9");
        when(productRepository.save(product)).thenReturn(product);
        Product savedProduct = productService.save(product);
        assertEquals(savedProduct, product);
    }

    @Test
    void saveProduct_WithMissingDescription(){
        when(productRepository.save(product)).thenReturn(product);
        Product savedProduct = productService.save(product);
        assertEquals(savedProduct, product);
    }


    @Test
    void findById_found(){
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        Product result = productService.findById(1);
        assertEquals(result, product);
    }

    @Test
    void findProductById_NotFound_ThrowsException(){
        when(productRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(1);
        });
    }


    @Test
    void updateProduct(){

        product.setName("new name");
        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product updatedProduct = productService.updateProduct(1, product);
        assertEquals(updatedProduct, product);
    }


    @Test
    void testFindAll_WithCategorySpecification() {
        List<Product> productList = Arrays.asList(product);
        Page<Product> productPage = new PageImpl<>(productList);
        Pageable pageable = PageRequest.of(0, 10);

        Specification<Product> spec = ProductSpecification.hasCategory("Laptop");

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);

        Page<Product> result = productService.findAll(spec, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Dell Latitud", result.getContent().get(0).getName());
        assertEquals("Laptop", result.getContent().get(0).getCategory());

        verify(productRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testDeleteProduct_ProductDoesNotExist() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(1);
        });
        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository, never()).delete(any(Product.class));
    }




}
