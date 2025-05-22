package com.masters.PSDMWebApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String keycloakId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private Long roleId;
}
