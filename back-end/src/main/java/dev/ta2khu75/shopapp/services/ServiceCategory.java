package dev.ta2khu75.shopapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.ta2khu75.shopapp.dtos.DtoCategory;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.models.Category;
import dev.ta2khu75.shopapp.repositories.RepositoryCategory;
import dev.ta2khu75.shopapp.services.iservices.IServiceCategory;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceCategory implements IServiceCategory {
    private final RepositoryCategory repositoryCategory;

    @Override
    public Category createCategory(DtoCategory category) {
        Category newCategory = Category.builder().name(category.getName()).build();
        return repositoryCategory.save(newCategory);
        // TODO Auto-generated method stub
    }

    @Override
    public Category getCategoryById(long id) throws DataNotFoundException {
        // TODO Auto-generated method stub
        Category category= repositoryCategory.findById(id).orElseThrow(() -> new DataNotFoundException("Category not found"));
        return category;
    }

    @Override
    public List<Category> getAllCategories() {
        // TODO Auto-generated method stub
        return repositoryCategory.findAll();
    }

    @Override
    public Category updateCategory(long id, DtoCategory category) throws DataNotFoundException {
        // TODO Auto-generated method stub
        Category newCategory = getCategoryById(id);
        newCategory.setName(category.getName());
        return repositoryCategory.save(newCategory);
    }

    @Override
    public void deleteCategory(long id) {
        // TODO Auto-generated method stub
        repositoryCategory.deleteById(id);
    }

}
/**
 * InnerServiceCategory
 */
