import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetOrderById() throws Exception {
        mockMvc.perform(get("/orders/1")).andExpect(status().isOk());
    }

    @Test
    public void testGetOrders() throws Exception {
        mockMvc.perform(get("/orders")).andExpect(status().isOk());
    }

    @Test
    public void testGetOrdersWithMinPrice() throws Exception {
        mockMvc.perform(get("/orders").param("minPrice", "10.0")).andExpect(status().isOk());
    }

    @Test
    public void testGetOrdersWithPagination() throws Exception {
        mockMvc.perform(get("/orders").param("page", "1").param("size", "5")).andExpect(status().isOk());
    }
}