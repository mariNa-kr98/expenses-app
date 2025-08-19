package gr.aueb.cf.expensesApp.service;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
import gr.aueb.cf.expensesApp.core.enums.ErrorCode;
import gr.aueb.cf.expensesApp.core.exceptions.AppException;
import gr.aueb.cf.expensesApp.model.Category;
import gr.aueb.cf.expensesApp.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

//    @Override
//    public Category findByDescription(String description) {
//        return categoryRepository.findByDescription(description)
//                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND_EXCEPTION, "Description not found."));
//    }

    @Override
    public List<Category> findByType(CategoryType type) {
        return categoryRepository.findByType(type);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
