package com.masters.PSDMWebApi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolutionRequestDTO {
    private String title;
    private Long userId;
    private Long problemId;
    private Long sessionId;
    private List<AttributeRequestDTO> attributes;
}
