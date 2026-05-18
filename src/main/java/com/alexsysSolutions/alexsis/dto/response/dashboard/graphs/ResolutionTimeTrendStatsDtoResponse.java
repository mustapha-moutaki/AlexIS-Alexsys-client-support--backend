package com.alexsysSolutions.alexsis.dto.response.dashboard.graphs;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ResolutionTimeTrendStatsDtoResponse {

    private String day;
    private Long avgMinutes;

}
