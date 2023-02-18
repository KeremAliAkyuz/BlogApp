package dev.aliak.blogapp.services.serviceInterfaces;

import dev.aliak.blogapp.payload.CategoryDTO;

import java.util.List;

public interface CategoryServiceInterface {
    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO getCategory(Long categoryId);
    List<CategoryDTO> getAllCategories();
    CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId);
    void deleteCategory(Long categoryId);
}
