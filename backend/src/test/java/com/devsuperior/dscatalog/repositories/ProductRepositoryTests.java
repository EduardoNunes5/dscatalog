package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;
    private long existingId;
    private long countTotalProducts;
    private long nonExistingId;

    @BeforeEach
    void setUp(){
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist(){

        Optional<Product> obj = productRepository.findById(nonExistingId);

        Assertions.assertFalse(obj.isPresent());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists(){

        Optional<Product> obj = productRepository.findById(existingId);

        Assertions.assertTrue(obj.isPresent());
    }


    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){

        Product product = Factory.createProduct();
        product.setId(null);

        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());

    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        productRepository.deleteById(existingId);

        Optional<Product> obj = productRepository.findById(existingId);
        Assertions.assertFalse(obj.isPresent());
    }

    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            productRepository.deleteById(nonExistingId);
        });
    }
}
