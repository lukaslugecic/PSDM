package com.masters.PSDMWebApi.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class GroupSolutionRequestDTO {
    List<Long> solutionIds;
    SolutionRequestDTO solution;
}
