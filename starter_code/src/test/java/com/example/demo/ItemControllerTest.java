package com.example.demo;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemRepository itemRepository;

    @Test
    public void testGetItemById() throws Exception {


        Optional<Item> item = Optional.of(new Item(1L, "itemTest", BigDecimal.valueOf(200), "item description"));
        when(itemRepository.findById(1L)).thenReturn(item);

        mockMvc.perform(MockMvcRequestBuilders.
                get("/api/item/{id}", 1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void testGetItemByName() throws Exception {

        List<Item> item = new ArrayList<>();
        item.add(new Item(1L, "itemTest", BigDecimal.valueOf(200), "item description"));
        when(itemRepository.findByName("itemTest")).thenReturn(item);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/item/name/{name}", "itemTest"))
                .andExpect(status().isOk());
    }
}
