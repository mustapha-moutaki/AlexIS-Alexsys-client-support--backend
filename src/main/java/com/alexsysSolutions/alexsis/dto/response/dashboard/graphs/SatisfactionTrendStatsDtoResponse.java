package com.alexsysSolutions.alexsis.dto.response.dashboard.graphs;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SatisfactionTrendStatsDtoResponse {

    private String day;
    private String score;

}
