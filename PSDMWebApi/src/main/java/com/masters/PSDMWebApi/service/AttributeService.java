package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.Attribute;
import java.util.List;
import java.util.Optional;

public interface AttributeService {
    List<Attribute> getAllAttributes();

    Optional<Attribute> getAttributeById(Long id);

    Attribute createAttribute(Attribute session);

    Attribute updateAttribute(Long id, Attribute session);

    void deleteAttribute(Long id);

    List<Double> getAllWeightsBySessionId(Long id);

    Double getWeightBySolutionId(Long id);
}
