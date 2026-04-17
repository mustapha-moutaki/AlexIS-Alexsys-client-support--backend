package com.alexsysSolutions.alexsis.dto.request.category;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class CategoryDtoRequest {

    private String name;
    private String description;

}
