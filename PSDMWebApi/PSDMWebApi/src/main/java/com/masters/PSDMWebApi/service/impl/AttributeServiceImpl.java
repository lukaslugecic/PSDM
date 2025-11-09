package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.repository.AttributeRepository;
import com.masters.PSDMWebApi.service.AttributeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;

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
