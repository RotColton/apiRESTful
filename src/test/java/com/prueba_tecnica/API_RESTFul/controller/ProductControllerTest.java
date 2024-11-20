package com.prueba_tecnica.API_RESTFul.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba_tecnica.API_RESTFul.entity.Product;
import com.prueba_tecnica.API_RESTFul.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private String productJson;
    private Product product;

    @BeforeEach
    public void setUp() throws Exception {
        product = new Product(1, "Producto de prueba", "Descripción del producto de prueba", 100.0f,"Categoría de prueba" );

        productJson = objectMapper.writeValueAsString(product);

        when(productService.save(any(Product.class))).thenReturn(product);
        when(productService.findById(1)).thenReturn(product);
    }

    @Test
    void testAddProduct_WithDescription_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Producto de prueba"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.description").value("Descripción del producto de prueba"))
                .andExpect(jsonPath("$.category").value("Categoría de prueba"));
    }

    @Test
    void testGetProductById() throws Exception {
        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Producto de prueba"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.description").value("Descripción del producto de prueba"))
                .andExpect(jsonPath("$.category").value("Categoría de prueba"));
    }

    @Test
    void testUpdateProductById() throws Exception {
        Product updatedProduct = new Product(1,"Producto actualizado", "Descripción del producto actualizado", 150.0f, "Categoría actualizada");

        String updatedProductJson = objectMapper.writeValueAsString(updatedProduct);

        when(productService.updateProduct(eq(1), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Producto actualizado"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.description").value("Descripción del producto actualizado"))
                .andExpect(jsonPath("$.category").value("Categoría actualizada"));
    }

    @Test
    void testDeleteProductById() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllProducts() throws Exception {
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        when(productService.findAll(any(), any())).thenReturn(productPage);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Producto de prueba"))
                .andExpect(jsonPath("$.content[0].price").value(100.0))
                .andExpect(jsonPath("$.content[0].description").value("Descripción del producto de prueba"))
                .andExpect(jsonPath("$.content[0].category").value("Categoría de prueba"));
    }

}
