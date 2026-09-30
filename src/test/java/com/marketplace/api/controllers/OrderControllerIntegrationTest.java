package com.marketplace.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.application.dtos.OrderItemRequestDto;
import com.marketplace.application.dtos.OrderRequestDto;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long productId;

    @BeforeEach
    void setUp() throws Exception {
        ProductRequestDto product = new ProductRequestDto();
        product.setName("Notebook");
        product.setDescription("High-end notebook");
        product.setPrice(new BigDecimal("2500.00"));
        product.setQuantity(10);
        product.setSku("SKU-001");

        MvcResult result = mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        productId = objectMapper.readTree(responseBody).get("id").asLong();
    }

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {
        List<OrderItemRequestDto> items = new ArrayList<>();
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setProductId(productId);
        item.setQuantity(2);
        items.add(item);

        OrderRequestDto order = new OrderRequestDto();
        order.setOrderNumber("ORD-001");
        order.setItems(items);

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber", is("ORD-001")))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    void shouldGetOrderById() throws Exception {
        List<OrderItemRequestDto> items = new ArrayList<>();
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setProductId(productId);
        item.setQuantity(1);
        items.add(item);

        OrderRequestDto order = new OrderRequestDto();
        order.setOrderNumber("ORD-002");
        order.setItems(items);

        MvcResult createResult = mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        long orderId = objectMapper.readTree(responseBody).get("id").asLong();

        mockMvc.perform(get("/api/pedidos/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber", is("ORD-002")))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    void shouldListOrders() throws Exception {
        List<OrderItemRequestDto> items = new ArrayList<>();
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setProductId(productId);
        item.setQuantity(1);
        items.add(item);

        OrderRequestDto order = new OrderRequestDto();
        order.setOrderNumber("ORD-003");
        order.setItems(items);

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is("ORD-003")));
    }

    @Test
    void shouldConfirmOrderSuccessfully() throws Exception {
        List<OrderItemRequestDto> items = new ArrayList<>();
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setProductId(productId);
        item.setQuantity(1);
        items.add(item);

        OrderRequestDto order = new OrderRequestDto();
        order.setOrderNumber("ORD-004");
        order.setItems(items);

        MvcResult createResult = mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        long orderId = objectMapper.readTree(responseBody).get("id").asLong();

        mockMvc.perform(post("/api/pedidos/" + orderId + "/confirmar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CONFIRMED")));
    }

    @Test
    void shouldReturnErrorWhenConfirmingEmptyOrder() throws Exception {
        OrderRequestDto order = new OrderRequestDto();
        order.setOrderNumber("ORD-005");
        order.setItems(new ArrayList<>());

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated());

        MvcResult getResult = mockMvc.perform(get("/api/pedidos"))
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();
        long orderId = objectMapper.readTree(responseBody).get("0").get("id").asLong();

        mockMvc.perform(post("/api/pedidos/" + orderId + "/confirmar"))
                .andExpect(status().isBadRequest());
    }
}
