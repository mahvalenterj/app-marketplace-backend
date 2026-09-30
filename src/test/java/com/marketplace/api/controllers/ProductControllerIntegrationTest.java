package com.marketplace.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.application.dtos.ProductRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequestDto validProduct;

    @BeforeEach
    void setUp() {
        validProduct = new ProductRequestDto();
        validProduct.setName("Notebook");
        validProduct.setDescription("High-end notebook");
        validProduct.setPrice(new BigDecimal("2500.00"));
        validProduct.setQuantity(10);
        validProduct.setSku("SKU-001");
    }

    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Notebook")))
                .andExpect(jsonPath("$.price", is(2500.00)))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void shouldListProductsAfterCreation() throws Exception {
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validProduct)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Notebook")));
    }

    @Test
    void shouldGetProductById() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validProduct)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        long productId = objectMapper.readTree(responseBody).get("id").asLong();

        mockMvc.perform(get("/api/produtos/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Notebook")))
                .andExpect(jsonPath("$.sku", is("SKU-001")));
    }

    @Test
    void shouldReturnErrorForInvalidProduct() throws Exception {
        ProductRequestDto invalidProduct = new ProductRequestDto();
        invalidProduct.setName("");
        invalidProduct.setDescription("Desc");
        invalidProduct.setPrice(new BigDecimal("0"));
        invalidProduct.setQuantity(10);
        invalidProduct.setSku("SKU-002");

        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldPreventDuplicateSku() throws Exception {
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validProduct)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validProduct)))
                .andExpect(status().isBadRequest());
    }
}
