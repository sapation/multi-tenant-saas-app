package com.webtech.saas.controllers;

import com.webtech.saas.common.PageResponse;
import com.webtech.saas.requests.CategoryRequest;
import com.webtech.saas.responses.CategoryResponse;
import com.webtech.saas.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService service;

    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody final CategoryRequest request) {
        this.service.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> findAllcategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(this.service.findAll(page, size));
    }

    @PutMapping("/{category-id}")
    public ResponseEntity<Void> updateCategory(
            @Valid @RequestBody final CategoryRequest request,
            @PathVariable("category-id") final String id) {
        this.service.update(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{category-id}")
    public ResponseEntity<CategoryResponse> findById(
            @PathVariable("category-id") String id) {
        return ResponseEntity.ok(this.service.findById(id));
    }

    @DeleteMapping("/{category-id}")
    public ResponseEntity<Void> deleteCategory(
            @Valid @RequestBody final CategoryRequest request,
            @PathVariable("category-id") final String id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
