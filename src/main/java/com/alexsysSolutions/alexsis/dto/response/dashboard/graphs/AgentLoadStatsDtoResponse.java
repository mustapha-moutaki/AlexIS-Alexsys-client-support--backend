package com.alexsysSolutions.alexsis.dto.response.dashboard.graphs;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AgentLoadStatsDtoResponse {
    private String fullName;
    private Long load;
    private Long resolved;
}
