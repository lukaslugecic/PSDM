package com.masters.PSDMWebApi.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Slf4j
public final class AttributeConverter {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Decodes a URL-encoded JSON array of strings into a List<String>.
     * If anything goes wrong (null, malformed, etc.) returns an empty list.
     */
    public static List<String> decodeAttributes(String encodedAttributes) {
        if (encodedAttributes == null || encodedAttributes.isBlank()) {
            return Collections.emptyList();
        }
        try {
            String json = URLDecoder.decode(encodedAttributes, StandardCharsets.UTF_8);
            return mapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.warn("Failed to decode attributes", e);
            return Collections.emptyList();
        }
    }
}
