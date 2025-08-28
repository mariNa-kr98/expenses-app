package gr.aueb.cf.expensesApp.repository;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
import gr.aueb.cf.expensesApp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByType(CategoryType type);
}
