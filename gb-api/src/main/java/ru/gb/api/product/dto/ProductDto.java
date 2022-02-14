package ru.gb.api.product.dto;

import ru.gb.api.category.dto.CategoryDto;
import ru.gb.api.common.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    @NotBlank
    private String title;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=6, fraction=2)
    private BigDecimal cost;

    @PastOrPresent
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate manufactureDate;

    @NotNull
    private Status status;

    private String manufacturer;

    private Set<CategoryDto> categories;

}
