package gr.aueb.cf.expensesApp.service;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
import gr.aueb.cf.expensesApp.core.exceptions.AppException;
import gr.aueb.cf.expensesApp.model.Category;

import java.util.List;

public interface ICategoryService {

    //Category findByDescription(String description);
    List<Category> findByType(CategoryType type);
    List<Category> findAll();

    void deleteById(Long id) throws AppException;
}
