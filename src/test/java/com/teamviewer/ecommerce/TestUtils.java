package com.teamviewer.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {
    public static final String PRODUCT_ENDPOINT = "/api/products";
    public static final String PRODUCT_ID = "2a2fbac2-0f95-4cfa-abd2-a50cb0a77227";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readJsonFromFile(String filePath, Class<T> valueType) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        return objectMapper.readValue(json, valueType);
    }
}
