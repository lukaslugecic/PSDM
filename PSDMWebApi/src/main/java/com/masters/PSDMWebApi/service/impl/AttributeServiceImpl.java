package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Attribute;
import com.masters.PSDMWebApi.repository.AttributeRepository;
import com.masters.PSDMWebApi.service.AttributeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository sessionRepository;

    public AttributeServiceImpl(AttributeRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<Attribute> getAllAttributes() {
        return sessionRepository.findAll();
    }

    @Override
    public Optional<Attribute> getAttributeById(Long id) {
        return sessionRepository.findById(id);
    }

    @Override
    public Attribute createAttribute(Attribute session) {
        return sessionRepository.save(session);
    }

    @Override
    public Attribute updateAttribute(Long id, Attribute session) {
        session.setId(id);
        return sessionRepository.save(session);
    }

    @Override
    public void deleteAttribute(Long id) {
        sessionRepository.deleteById(id);
    }
}
