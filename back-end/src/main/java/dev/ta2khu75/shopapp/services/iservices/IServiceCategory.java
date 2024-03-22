
package dev.ta2khu75.shopapp.services.iservices;

import java.util.List;

import dev.ta2khu75.shopapp.dtos.DtoCategory;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.models.Category;

public interface IServiceCategory {
    Category createCategory(DtoCategory category);

    Category getCategoryById(long id) throws DataNotFoundException;

    List<Category> getAllCategories();

    Category updateCategory(long id, DtoCategory category) throws DataNotFoundException;

    void deleteCategory(long id);
}