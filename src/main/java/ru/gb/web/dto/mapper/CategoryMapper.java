package ru.gb.web.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gb.api.category.dto.CategoryDto;
import ru.gb.entity.Category;

@Mapper
public interface CategoryMapper {

    @Mapping(source = "categoryId", target = "id")
    Category toCategory(CategoryDto categoryDto);

    @Mapping(source = "id", target = "categoryId")
    CategoryDto toCategoryDto(Category category);

}