package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/reminder-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DatabaseProjectApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoadsOnListTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("List of reminders")));
    }

    @Test
    public void contextLoadsOnAddTest() throws Exception {
        this.mockMvc.perform(get("/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Add new reminder")));
    }

    @Test
    public void reminderListTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(xpath("//*[@id='row-reminder']").nodeCount(2));
    }

    @Test
    public void addMessageToListTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/add")
                .param("id", "4")
                .param("text", "Wash the floor")
                .param("date", "2021-01-11 00:00:00")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(xpath("//*[@id='row-reminder']").string("Wash the floor"))
                .andExpect(xpath("//*[@id='row-reminder']").string("2021-01-11 00:00:00"));
    }

    @Test
    public void filterMessageTest() throws Exception {
        this.mockMvc.perform(get("/").param("filter", "Wash up"))
                .andDo(print())
                .andExpect(xpath("//*[@id='row-reminder']").nodeCount(2))
                .andExpect(xpath("//*[@id='row-reminder']").string("Wash up"));
    }




}
