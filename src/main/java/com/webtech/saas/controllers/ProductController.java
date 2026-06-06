package com.webtech.saas.controllers;

import com.webtech.saas.common.PageResponse;
import com.webtech.saas.requests.ProductRequest;
import com.webtech.saas.responses.ProductResponse;
import com.webtech.saas.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/producties")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid  @RequestBody final ProductRequest request) {
        this.productService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> findAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(this.productService.findAll(page, size));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> findById(
            @RequestParam("product-id") String productId) {
        return ResponseEntity.ok(this.productService.findById(productId));
    }

    @PutMapping("/{product-id}")
    public ResponseEntity<Void> updateProduct(
            @Valid @RequestBody ProductRequest request,
            @RequestParam("product-id") String productId) {
        this.productService.update(productId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<Void> deleteProduct(
            @RequestParam("product-id") String productId) {
        this.productService.delete(productId);
        return ResponseEntity.ok().build();
    }
}
