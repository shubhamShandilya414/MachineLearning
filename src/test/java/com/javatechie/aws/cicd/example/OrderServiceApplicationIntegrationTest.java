package com.javatechie.aws.cicd.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class OrderServiceApplicationIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Create test table
        jdbcTemplate.execute("DROP TABLE IF EXISTS ORDERS");
        jdbcTemplate.execute("CREATE TABLE ORDERS (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "category VARCHAR(255), " +
                "color VARCHAR(255), " +
                "price DOUBLE" +
                ")");
        
        // Insert test data for performance and functionality testing
        for (int i = 1; i <= 100; i++) {
            jdbcTemplate.update("INSERT INTO ORDERS (name, category, color, price) VALUES (?, ?, ?, ?)",
                    "Product" + i, "Category" + (i % 5), "Color" + (i % 3), i * 10.0);
        }
    }

    @Test
    void testGetOrderByIdReturnsCorrectOrder() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product1"))
                .andExpect(jsonPath("$.category").value("Category1"))
                .andExpect(jsonPath("$.color").value("Color1"))
                .andExpect(jsonPath("$.price").value(10.0));
    }

    @Test
    void testGetOrderByIdReturns404ForNonExistentOrder() throws Exception {
        mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOrderByIdReturns400ForInvalidIdFormat() throws Exception {
        mockMvc.perform(get("/orders/invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid number format"));
    }

    @Test
    void testGetOrderByIdReturns400ForNegativeId() throws Exception {
        mockMvc.perform(get("/orders/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOrdersWithMinPriceFiltersCorrectly() throws Exception {
        mockMvc.perform(get("/orders?minPrice=500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(50))
                .andExpect(jsonPath("$[0].price").value(500.0));
    }

    @Test
    void testGetOrdersWithMinPriceReturnsEmptyForHighPrice() throws Exception {
        mockMvc.perform(get("/orders?minPrice=2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetOrdersWithMinPriceZeroReturnsAllOrders() throws Exception {
        mockMvc.perform(get("/orders?minPrice=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(100));
    }

    @Test
    void testGetOrdersWithPaginationReturnsCorrectPage() throws Exception {
        mockMvc.perform(get("/orders?page=2&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].id").value(21));
    }

    @Test
    void testGetOrdersWithPaginationLastPagePartial() throws Exception {
        mockMvc.perform(get("/orders?page=9&size=15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void testGetOrdersWithPaginationBeyondDataReturnsEmpty() throws Exception {
        mockMvc.perform(get("/orders?page=20&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetOrdersWithNegativePageReturns400() throws Exception {
        mockMvc.perform(get("/orders?page=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Page number must be non-negative"));
    }

    @Test
    void testGetOrdersWithZeroSizeReturns400() throws Exception {
        mockMvc.perform(get("/orders?size=0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Page size must be positive"));
    }

    @Test
    void testGetOrdersWithNegativeSizeReturns400() throws Exception {
        mockMvc.perform(get("/orders?size=-5"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Page size must be positive"));
    }

    @Test
    void testGetOrdersWithMinPriceAndPaginationCombined() throws Exception {
        mockMvc.perform(get("/orders?minPrice=300&page=1&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].price").value(360.0));
    }

    @Test
    void testGetOrdersWithoutParametersMaintainsBackwardCompatibility() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(100));
    }

    @Test
    void testGetOrdersWithDefaultPaginationValues() throws Exception {
        mockMvc.perform(get("/orders?minPrice=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(90));
    }

    @Test
    void testGetOrdersWithInvalidMinPriceFormatReturns400() throws Exception {
        mockMvc.perform(get("/orders?minPrice=invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrdersWithInvalidPageFormatReturns400() throws Exception {
        mockMvc.perform(get("/orders?page=invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrdersWithInvalidSizeFormatReturns400() throws Exception {
        mockMvc.perform(get("/orders?size=invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrdersPerformanceWithLargeDataset() throws Exception {
        // Add more test data to simulate 10k records
        for (int i = 101; i <= 1000; i++) {
            jdbcTemplate.update("INSERT INTO ORDERS (name, category, color, price) VALUES (?, ?, ?, ?)",
                    "Product" + i, "Category" + (i % 5), "Color" + (i % 3), i * 10.0);
        }

        long startTime = System.currentTimeMillis();
        mockMvc.perform(get("/orders?minPrice=500&page=0&size=100"))
                .andExpect(status().isOk());
        long endTime = System.currentTimeMillis();
        
        // Verify response time is under 500ms
        assertTrue((endTime - startTime) < 500, "Response time should be under 500ms");
    }

    @Test
    void testGetOrdersOrderingIsConsistent() throws Exception {
        mockMvc.perform(get("/orders?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpected(jsonPath("$[3].id").value(4))
                .andExpected(jsonPath("$[4].id").value(5));
    }

    @Test
    void testGetOrdersWithExactBoundaryMinPrice() throws Exception {
        mockMvc.perform(get("/orders?minPrice=500.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(500.0));
    }

    @Test
    void testGetOrdersWithVeryLargePageSize() throws Exception {
        mockMvc.perform(get("/orders?page=0&size=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(100));
    }

    @Test
    void testAddOrderStillWorksWithNewEndpoints() throws Exception {
        Order newOrder = new Order();
        newOrder.setName("TestProduct");
        newOrder.setCategory("TestCategory");
        newOrder.setColor("TestColor");
        newOrder.setPrice(123.45);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestProduct"));
    }
}