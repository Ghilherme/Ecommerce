package com.teamviewer.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {
    public static final String PRODUCT_ENDPOINT = "/api/products";
    public static final String ORDER_ENDPOINT = "/api/orders";
    public static final String PRODUCT_ID = "2a2fbac2-0f95-4cfa-abd2-a50cb0a77227";
    public static final String ORDER_ID = "a10dfcae-053f-4b0b-97c7-64baafaa3db3";
    public static final String PRODUCT_ID_ENDPOINT = PRODUCT_ENDPOINT + "/" + PRODUCT_ID;
    public static final String ORDER_ID_ENDPOINT = ORDER_ENDPOINT + "/" + ORDER_ID;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readJsonFromFile(String filePath, Class<T> valueType) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        return objectMapper.readValue(json, valueType);
    }

    public static <T> T readValue(String jsonString, Class<T> valueType) throws IOException {
        return objectMapper.readValue(jsonString, valueType);
    }
}
