package com.masters.PSDMWebApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {
    private Long id;
    private Long problemId;
    private Long problemSolvingMethodId;
    private Long decisionMakingMethodId;
    private Long duration;
    private String attributes;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long parentSessionId;
}
