package com.alexsysSolutions.alexsis.dto.response.dashboard.graphs;

import java.time.LocalDateTime;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WeeklyTicketsStatsDtoResponse {

    private String day;
    private Long created;
    private Long resolved;
}
