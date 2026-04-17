package com.alexsysSolutions.alexsis.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter


public class CategoryDtoRequest {

    @NotBlank(message = "name is required")
    private String name;

    private String description;

}
