package com.alexsysSolutions.alexsis.dto.response.Category;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CategoryLiteDto {
    private Long id;
    private String description;
    private String name;
}
