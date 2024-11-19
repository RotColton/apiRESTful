package com.prueba_tecnica.API_RESTFul.servicie;

import com.prueba_tecnica.API_RESTFul.exception.*;
import com.prueba_tecnica.API_RESTFul.entity.Product;
import com.prueba_tecnica.API_RESTFul.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    //TODO: no permitir entradas duplicadas

    public Product save(Product product) {
        validateProductAttribute(product);
        return productRepository.save(product);
    }

    public Page<Product> findAll(Specification<Product> spec, Pageable pageable) {
        return productRepository.findAll(spec,pageable);
    }

    public Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product updateProduct(Integer id, Product productDetails) {
        Product existingProduct = findById(id);
        productDetails.setId(existingProduct.getId());
        return save(productDetails);
    }

    public void deleteProduct(Integer id) {
        productRepository.findById(id).ifPresentOrElse(
                productRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Product not found with id: " + id);
                });
    }

    private void validateProductAttribute(Product product) {
        // Validar nombre del producto
        Optional.ofNullable(product)
                .map(Product::getName)
                .filter(name -> !name.trim().isEmpty())
                .filter(name -> name.length() <= 255)
                .orElseThrow(() -> new InvalidNameException("Product name cannot be null, empty or longer than 255 characters"));

        // Validar categoría del producto
        Optional.of(product)
                .map(Product::getCategory)
                .filter(category -> !category.trim().isEmpty())
                .orElseThrow(() -> new InvalidCategoryException("Product category cannot be null or empty"));

        // Validar precio del producto
        Optional.of(product)
                .map(Product::getPrice)
                .filter(price -> price > 0)
                .orElseThrow(() -> new InvalidPriceException("The price must be greater than 0"));
    }


}
