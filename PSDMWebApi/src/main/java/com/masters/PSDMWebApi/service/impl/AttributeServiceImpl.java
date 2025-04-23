package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Attribute;
import com.masters.PSDMWebApi.repository.AttributeRepository;
import com.masters.PSDMWebApi.service.AttributeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;

    public AttributeServiceImpl(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    @Override
    public List<Attribute> getAllAttributes() {
        return attributeRepository.findAll();
    }

    @Override
    public Optional<Attribute> getAttributeById(Long id) {
        return attributeRepository.findById(id);
    }

    @Override
    public Attribute createAttribute(Attribute attribute) {
        return attributeRepository.save(attribute);
    }

    @Override
    public Attribute updateAttribute(Long id, Attribute attribute) {
        attribute.setId(id);
        return attributeRepository.save(attribute);
    }

    @Override
    public void deleteAttribute(Long id) {
        attributeRepository.deleteById(id);
    }

    @Override
    public Double getWeightBySolutionId(Long id) {
        return attributeRepository.findWeightAttributeBySolutionId(id)
                .flatMap(this::safeParseDouble)
                .orElseThrow(() -> new RuntimeException("No weight found for attribute id: " + id));
    }

    @Override
    public List<Double> getAllWeightsBySessionId(Long id) {
        return attributeRepository.findWeightAttributesBySessionId(id)
                .stream()
                .map(this::safeParseDouble)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<Double> safeParseDouble(String value) {
        try {
            return Optional.of(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse value to Double: " + value);
            return Optional.empty();
        }
    }

}
