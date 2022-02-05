package com.devsuperior.dscatalog.services;


import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exception.DatabaseException;
import com.devsuperior.dscatalog.services.exception.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryService categoryService;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;
        product = Factory.createProduct();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(product));
        productDTO = Factory.createProductDTO();

        when(repository.findAll((Pageable)any()))
                .thenReturn(page);

        when(repository.save(any()))
                .thenReturn(product);

        when(repository.findById(existingId))
                .thenReturn(Optional.of(product));

        when(repository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        doNothing()
                .when(repository).deleteById(existingId);

        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(nonExistingId);

        doThrow(DataIntegrityViolationException.class)
                .when(repository)
                .deleteById(dependentId);

        when(repository.getOne(existingId))
                .thenReturn(product);

        when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(categoryService.getOne(existingId))
                .thenReturn(category);

        when(categoryService.getOne(nonExistingId))
                .thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });

        verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        assertThrows(ResourceNotFoundException.class, () -> {
           productService.delete(nonExistingId);
        });

        verify(repository, Mockito.times(1)).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){

        assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });

        verify(repository, Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    public void findAllPagedShouldReturnPage(){

        Pageable pageable = PageRequest.of(0, 1);
        Page<ProductDTO> result = productService.findAllPaged(pageable);

        assertNotNull(result);
        verify(repository, times(1)).findAll(pageable);

    }

    @Test
    void findByIdShouldReturnProductDTOWhenIdExists(){

        ProductDTO result = productService.findById(existingId);

        assertNotNull(result);
        verify(repository).findById(existingId);
    }


    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(nonExistingId);
        });
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists(){

        ProductDTO dto = productService.update(existingId, productDTO);
        assertNotNull(dto);
        verify(repository, times(1)).save(product);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(nonExistingId, productDTO);
        });
    }
}
