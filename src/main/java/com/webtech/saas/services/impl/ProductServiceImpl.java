package com.webtech.saas.services.impl;

import com.webtech.saas.common.PageResponse;
import com.webtech.saas.entities.Category;
import com.webtech.saas.entities.Product;
import com.webtech.saas.mappers.ProductMapper;
import com.webtech.saas.repositories.CategoryRepository;
import com.webtech.saas.repositories.ProductRepository;
import com.webtech.saas.requests.ProductRequest;
import com.webtech.saas.responses.ProductResponse;
import com.webtech.saas.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public void create(ProductRequest request) {
        //check if product already exist
        checkIfProductAlreadyExistsByReference(request.getReference());

        //check if category exist
        checkIfCategoryExistById(request.getCategoryId());

        final Product product = this.productMapper.toEntity(request);
        this.productRepository.save(product);
    }


    @Override
    public void update(String Id, ProductRequest request) {
        //find the existing product
        Optional<Product> existingProduct = this.productRepository.findById(Id);
        if (existingProduct.isEmpty()) {
            log.debug("Product Does not exist");
            throw new EntityNotFoundException("Product not found");
        }
        //check if product already
        checkIfProductAlreadyExistsByReference(request.getReference());

        //check if category exist
        checkIfCategoryExistById(request.getCategoryId());

        final Product productToUpdate = this.productMapper.toEntity(request);
        productToUpdate.setId(Id);
        this.productRepository.save(productToUpdate);

    }

    @Override
    public PageResponse<ProductResponse> findAll(int page, int size) {
        // create page request
        final PageRequest pageRequest = PageRequest.of(page, size);
        // fetch all product
        final Page<Product> products = this.productRepository.findAll(pageRequest);
        //map the product response
        final Page<ProductResponse> productResponse = products.map(this.productMapper::toResponse);

        return PageResponse.of(productResponse);
    }

    @Override
    public ProductResponse findById(String Id) {
        return this.productRepository.findById(Id)
                .map(this.productMapper::toResponse)
                .orElseThrow(()-> new EntityNotFoundException("Product with this" + Id + "does not exist"));
    }

    @Override
    public void delete(String id) {
        final Product product = this.productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product Does not exist"));
        this.productRepository.delete(product);
    }

    private void checkIfProductAlreadyExistsByReference(String reference) {
        Optional<Product> product = this.productRepository.findByReferenceIgnoreCase(reference);
        if(product.isPresent()) {
            log.debug("Product Already Exist");
            throw new RuntimeException("Product Already Exist");
        }
    }

    private void checkIfCategoryExistById(String categoryId) {
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if(category.isEmpty()) {
            log.debug("Category does not exist");
            throw new RuntimeException("Category does not Exist");
        }
    }
}
