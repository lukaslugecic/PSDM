package com.masters.PSDMWebApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDetailsDTO {
    private Long id;
    private String problemSolvingMethod;
    private String decisionMakingMethod;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> attributes;
}
