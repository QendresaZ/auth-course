package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserOrder userOrder;

    @Test
    public void testSubmitOrder() throws Exception {
        User user = new User(1L, "Qendresa", "test12345");
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(Arrays.asList(new Item(1L, "Test", new BigDecimal(10.0), "Description")));
        cart.setUser(user);
        cart.setTotal(new BigDecimal(10.0));
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        UserOrder fromCart = UserOrder.createFromCart(cart);

        when(orderRepository.save(any())).thenReturn(fromCart);

        mockMvc.perform(
                post("/api/order/submit/{username}", "Qendresa")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


}
