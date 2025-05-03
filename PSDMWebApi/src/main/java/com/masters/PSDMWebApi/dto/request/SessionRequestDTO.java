package com.masters.PSDMWebApi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionRequestDTO {
    private Long problemId;
    private Long problemSolvingMethodId;
    private Long decisionMakingMethodId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long parentSessionId;
}
