package com.masters.PSDMWebApi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionRequestDTO {
    private Long problemId;
    private Long problemSolvingMethodId;
    private Long decisionMakingMethodId;
    private Long duration;
    private Long parentSessionId;
}
