package com.prueba_tecnica.API_RESTFul.service;

import com.prueba_tecnica.API_RESTFul.exception.ResourceNotFoundException;
import com.prueba_tecnica.API_RESTFul.entity.Product;
import com.prueba_tecnica.API_RESTFul.repository.ProductRepository;
import com.prueba_tecnica.API_RESTFul.servicie.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        openMocks(this);
        product = new Product();
        product.setId(1);
        product.setName("Dell Latitud");
        product.setCategory("Laptop");
        product.setPrice(2344.66f);

    }

    @Test
    void saveProduct_WithDescription_Succeeds(){
        product.setDescription("Razen 9");
        when(productRepository.save(product)).thenReturn(product);
        Product savedProduct = productService.save(product);
        assertEquals(savedProduct, product);
    }

    @Test
    void saveProduct_WithMissingDescription_Succeeds(){
        when(productRepository.save(product)).thenReturn(product);
        Product savedProduct = productService.save(product);
        assertEquals(savedProduct, product);
    }

    @Test
    void saveProduct_WithMissingName_ThrowsException(){
        product.setName(null);
        when(productRepository.save(product)).thenReturn(product);
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.save(product);
        });
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
    void testFindAll(){

        Pageable pageable = Pageable.unpaged();
        Product product2 = new Product(2,"Asus","Intel Core 8", 2345.45f, "Laptop" );
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product, product2));

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        //Page<Product> result = productService.findAll(pageable);
        //assertEquals(2, result.getTotalElements());
        
    }


}
