package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductServiceIt {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        countTotalProducts = 25L;
        existingId = 1L;
        nonExistingId = 1000L;
    }

    @Test
    public void findAllpagedShouldReturnPageWhenPage0Size10() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAllPaged(pageable);

        assertFalse(result.isEmpty());
        assertEquals(10, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllpagedShouldReturnPageWhenPageDoesNotExist() {

        Pageable pageable = PageRequest.of(50, 10);

        Page<ProductDTO> result = service.findAllPaged(pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllpagedShouldReturnSortedPageWhenSortedByName() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result = service.findAllPaged(pageable);

        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists(){

        service.delete(existingId);

        assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

}
