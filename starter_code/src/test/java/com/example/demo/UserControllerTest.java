package com.example.demo;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CartRepository cartRepository;

    @Test
    public void testCreateUser() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest("Qendresa", "test1234", "test1234");
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(createUserRequest.getPassword());
        user.setId(1L);

        when(userRepository.save(any())).thenReturn(user);

        mockMvc.perform(post("/api/user/create")
                .content(objectMapper.writeValueAsString(createUserRequest))
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindUserById() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest("Qendresa", "test1234", "test1234");
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(createUserRequest.getPassword());
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/id/{id}", 1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void testFindUserByIdReturnsNotFoundIfUserIsNotFound() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/id/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
