package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRoles();

    Optional<Role> getRoleById(Long id);

    Role createRole(Role session);

    Role updateRole(Long id, Role session);

    void deleteRole(Long id);
}

