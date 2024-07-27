package com.teamviewer.ecommerce.unittests.controllers;

import com.ecommerce.model.ProductApi;
import com.ecommerce.model.ProductApiResponse;
import com.teamviewer.ecommerce.TestUtils;
import com.teamviewer.ecommerce.controllers.ProductController;
import com.teamviewer.ecommerce.domain.Product;
import com.teamviewer.ecommerce.mappers.ProductMapper;
import com.teamviewer.ecommerce.services.ProductService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    private Product createdProductMock;
    private ProductApi createdProductApiMock;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        createdProductMock = buildMockProduct();
        createdProductApiMock = buildMockProductApi();
    }

    @Test
    public void whenPostProduct_thenReturnCreatedProduct() throws Exception {

        when(productService.createProduct(any(Product.class))).thenReturn(createdProductMock);
        when(productMapper.fromApiToDomain(any(ProductApi.class))).thenReturn(createdProductMock);
        when(productMapper.fromDomainToApi(any(Product.class))).thenReturn(createdProductApiMock);

        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Product\",\"description\":\"Description\",\"price\":10.0}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductApi productResponse = TestUtils.readValue(response, ProductApi.class);

        assertEquals(createdProductMock.getId(), productResponse.getId());
        assertEquals(createdProductMock.getName(), productResponse.getName());
        assertEquals(createdProductMock.getDescription(), productResponse.getDescription());
        assertEquals(createdProductMock.getPrice(), productResponse.getPrice());
    }

    @Test
    public void whenGetProducts_thenReturnProducts() throws Exception {

        when(productService.findAllProducts()).thenReturn(List.of(createdProductMock));
        when(productMapper.fromDomainToApi(createdProductMock)).thenReturn(createdProductApiMock);

        String response = mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductApiResponse productList = TestUtils.readValue(response, ProductApiResponse.class);

        assertEquals(createdProductMock.getId(), productList.getProducts().getFirst().getId());
        assertEquals(createdProductMock.getName(), productList.getProducts().getFirst().getName());
        assertEquals(createdProductMock.getDescription(), productList.getProducts().getFirst().getDescription());
        assertEquals(createdProductMock.getPrice(), productList.getProducts().getFirst().getPrice());
    }

    private static @NotNull ProductApi buildMockProductApi() {
        ProductApi productApi = new ProductApi();
        productApi.setId("1");
        productApi.setName("Test Product");
        productApi.setDescription("Description");
        productApi.setPrice(10.0);
        return productApi;
    }

    private static @NotNull Product buildMockProduct() {
        Product product = new Product();
        product.setId("1");
        product.setName("Test Product");
        product.setDescription("Description");
        product.setPrice(10.0);
        return product;
    }
}
