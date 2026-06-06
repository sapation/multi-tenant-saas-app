package com.webtech.saas.services.impl;

import com.webtech.saas.entities.Category;
import com.webtech.saas.mappers.CategoryMapper;
import com.webtech.saas.repositories.CategoryRepository;
import com.webtech.saas.requests.CategoryRequest;
import com.webtech.saas.responses.CategoryResponse;
import com.webtech.saas.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public void create(CategoryRequest request) {
        //check if category already exit
        checkfCategoryExistsByName(request.getName());

        final Category category = categoryMapper.toEntity(request);
        this.categoryRepository.save(category);
    }

    @Override
    public void update(String Id, CategoryRequest request) {
        final Optional<Category> existingCategory = this.categoryRepository.findById(Id);
        if(existingCategory.isEmpty()) {
            log.debug("Category Not found");
            throw new EntityNotFoundException("Category Not found");
        }

        final  Category category = existingCategory.get();
        if(!category.getName().equalsIgnoreCase(request.getName())) {
            checkfCategoryExistsByName(request.getName());
        }

        final Category updatedCategory = categoryMapper.toEntity(request);
        updatedCategory.setId(Id);
        this.categoryRepository.save(updatedCategory);

    }

    @Override
    public List<CategoryResponse> findAll() {
        return this.categoryRepository.findAll()
                .stream()
                .map(this.categoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse findById(String Id) {
        return this.categoryRepository.findById(Id)
                .map(this.categoryMapper::toResponse)
                .orElseThrow(()-> new EntityNotFoundException("Category Not found"));
    }

    @Override
    public void delete(String id) {
        final Category category = this.categoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category not found"));

        this.categoryRepository.delete(category);
    }

    private void checkfCategoryExistsByName(final String name) {
        final Optional<Category> category = this.categoryRepository.findByNameIgnoreCase(name);
        if (category.isPresent()) {
            log.debug("Category already exist");
            throw new RuntimeException("Category already exist");
        }
    }
}
