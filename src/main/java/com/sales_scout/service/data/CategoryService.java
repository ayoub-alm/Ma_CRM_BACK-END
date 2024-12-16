package com.sales_scout.service.data;

import com.sales_scout.entity.data.Category;
import com.sales_scout.repository.data.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves all categories from the repository.
     *
     * @return a list of all categories.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category to retrieve.
     * @return an Optional containing the category if found, or empty if not found.
     */
    public Optional<Category> getCategoryById(Long id) {
        return this.categoryRepository.findById(id);
    }

    /**
     * Adds a new category to the repository.
     *
     * @param category the category to add.
     * @return the added category.
     */
    public Category addCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    /**
     * Updates an existing category by its ID.
     *
     * @param id       the ID of the category to update.
     * @param category the category data to update.
     * @return the updated category.
     * @throws EntityNotFoundException if no category with the specified ID exists.
     */
    public Category updateCategory(Long id, Category category) {
        return this.categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(category.getName());
                    return this.categoryRepository.save(existingCategory);
                })
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found."));
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete.
     * @throws EntityNotFoundException if no category with the specified ID exists.
     * @throws RuntimeException if there is a failure in deleting the category.
     */
    public void deleteCategory(Long id) {
        try {
            this.categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Category with id " + id + " not found.", e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to delete category with id " + id, e);
        }
    }
}
