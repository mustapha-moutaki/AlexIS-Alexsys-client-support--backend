package com.alexsysSolutions.alexsis.dto.response.Category;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class CategoryDtoResponse {
    private Long id;
    private String name;
    private String description;
}
