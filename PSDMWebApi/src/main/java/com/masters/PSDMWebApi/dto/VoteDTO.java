package com.masters.PSDMWebApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {
    private Long userId;
    private Long solutionId;
    private Double value;
    private LocalDateTime votingTime;
}