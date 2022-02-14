package ru.gb.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.gb.api.category.dto.CategoryDto;
import ru.gb.dao.CategoryDao;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class CategoryControllerSpringBootTest {

    final private static String CATEGORY_NAME = "Tech";
    final private static String UPDATED_CATEGORY_NAME = "Finance";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private CategoryDao categoryDao;

    @Test
    @Order(1)
    public void categoryPostTest() throws Exception {
        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(CategoryDto.builder()
                                        .title(CATEGORY_NAME)
                                        .build()
                                )
                        ))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    public void getCategoryListTest() throws Exception {
        mockMvc.perform(get("/api/v1/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].title").value(CATEGORY_NAME));
    }

    @Test
    @Order(3)
    public void categoryUpdateTest() throws Exception {
        mockMvc.perform(put("/api/v1/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(CategoryDto.builder()
                                        .categoryId(1L)
                                        .title(UPDATED_CATEGORY_NAME)
                                        .build()
                                )
                        ))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(4)
    public void getCategoryTest() throws Exception {
        mockMvc.perform(get("/api/v1/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("title").value(UPDATED_CATEGORY_NAME));
    }

    @Test
    @Order(5)
    public void deleteByIdTest() throws Exception {
        mockMvc.perform(delete("/api/v1/category/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    public void getZeroCategoryListTest() throws Exception {
        mockMvc.perform(get("/api/v1/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }
}