package com.masters.PSDMWebApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolutionDetailsDTO {
    private Long id;
    private String problemTitle;
    private String title;
    private String firstName;
    private String lastName;
    private List<AttributeDTO> attributes;
}
