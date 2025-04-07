package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Role;
import com.masters.PSDMWebApi.repository.RoleRepository;
import com.masters.PSDMWebApi.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository sessionRepository;

    public RoleServiceImpl(RoleRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return sessionRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        return sessionRepository.findById(id);
    }

    @Override
    public Role createRole(Role session) {
        return sessionRepository.save(session);
    }

    @Override
    public Role updateRole(Long id, Role session) {
        session.setId(id);
        return sessionRepository.save(session);
    }

    @Override
    public void deleteRole(Long id) {
        sessionRepository.deleteById(id);
    }
}
