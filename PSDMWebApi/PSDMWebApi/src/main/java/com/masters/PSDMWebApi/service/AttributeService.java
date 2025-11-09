package com.masters.PSDMWebApi.service;

import java.util.List;

public interface AttributeService {

    List<Double> getAllWeightsBySessionId(Long id);

    Double getWeightBySolutionId(Long id);
}
