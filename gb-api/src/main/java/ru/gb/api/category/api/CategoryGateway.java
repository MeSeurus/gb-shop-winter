package ru.gb.api.category.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gb.api.category.dto.CategoryDto;

import java.util.List;

@FeignClient(url = "http://127.0.0.1/internal/api/v1/category", name = "CategoryGateway")
public interface CategoryGateway {

    @GetMapping
    List<CategoryDto> getCategoryList();

    @GetMapping("/{categoryId}")
    ResponseEntity<?> getCategory(@PathVariable("categoryId") Long id);

    @PostMapping
    ResponseEntity<?> handlePost(@Validated @RequestBody CategoryDto categoryDto);

    @PutMapping("/{categoryId}")
    ResponseEntity<?> handleUpdate(@PathVariable("categoryId") Long id,
                                          @Validated @RequestBody CategoryDto categoryDto);

    @DeleteMapping("/{categoryId}")
    public void deleteById(@PathVariable("categoryId") Long id);
}