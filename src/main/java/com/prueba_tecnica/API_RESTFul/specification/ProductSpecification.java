package com.prueba_tecnica.API_RESTFul.specification;

import com.prueba_tecnica.API_RESTFul.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasName(String name) {
        return (root, query, builder) -> name == null ? null : builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasCategory(String category) {
        return (root, query, builder) -> category == null ? null : builder.equal(root.get("category"), category);
    }
}
