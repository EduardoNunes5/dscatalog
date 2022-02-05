package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.services.CategoryService;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product p = new Product(1L,
                "Phone",
                "Good Phone",
                800.0,
                "https://img.com/img.png",
                Instant.parse("2022-01-22T22:21:00Z"));
        p.addCategory(createCategory());
        return p;
    }

    public static ProductDTO createProductDTO(){
        Product prod = createProduct();
        return new ProductDTO(prod, prod.getCategories());
    }

    public static Category createCategory() {
        return new Category(1L, "Electronics");
    }
}
