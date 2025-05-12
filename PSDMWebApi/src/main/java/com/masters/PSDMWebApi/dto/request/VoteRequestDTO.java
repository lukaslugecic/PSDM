package com.masters.PSDMWebApi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequestDTO {
    private Long userId;
    private Long solutionId;
    private Double value;
}