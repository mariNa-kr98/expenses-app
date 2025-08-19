package gr.aueb.cf.expensesApp.rest;

import gr.aueb.cf.expensesApp.dto.CategoryInsertDTO;
import gr.aueb.cf.expensesApp.dto.CategoryReadOnlyDTO;
import gr.aueb.cf.expensesApp.mapper.Mapper;
import gr.aueb.cf.expensesApp.model.Category;
import gr.aueb.cf.expensesApp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @GetMapping
    public ResponseEntity<List<CategoryReadOnlyDTO>> getAllCategories(){

        List<Category> categories = categoryRepository.findAll();

        List<CategoryReadOnlyDTO> categoryDTOS = categories.stream()
                .map(category -> {
                CategoryReadOnlyDTO dto = new CategoryReadOnlyDTO();
                dto.setId(category.getId());
                dto.setCategoryType(category.getType());
                dto.setLabel(category.getLabel());
                return dto;
        }).toList();

        return ResponseEntity.ok(categoryDTOS);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can insert
    public ResponseEntity<CategoryReadOnlyDTO> saveCategory(@RequestBody CategoryInsertDTO dto) {
        Category category = mapper.mapToCategory(dto);
        Category saved = categoryRepository.save(category);
        return ResponseEntity.ok(mapper.mapToCategoryReadOnlyDTO(saved));
    }
}
