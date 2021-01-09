package com.example.demo;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/reminder-list-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DatabaseProjectApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderServiseImpl reminderService;

    @Test
    public void checkAndAddTest() {
        long millis = new Date().getTime();
        Reminder reminder1 = new Reminder("test", new Date(millis));
        boolean check1 = reminderService.checkAndAdd(reminder1);
        Assert.assertEquals(true, check1);
        Reminder reminder2 = new Reminder("test", new Date(millis));
        boolean check2 = reminderService.checkAndAdd(reminder2);
        Assert.assertEquals(false, check2);

    }

    @Test
    public void getFilterTest() {
        long millis = new Date().getTime();

        Reminder reminder1 = new Reminder("test1", new Date(millis));
        reminderService.checkAndAdd(reminder1);

        Reminder reminder2 = new Reminder("test2", new Date(millis));
        reminderService.checkAndAdd(reminder2);

        List<Reminder> reminders = reminderService.getFilter("test1");
        Assert.assertEquals(1, reminders.size());
    }

    @Test
    public void contextLoadsOnListTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Reminders for today")))
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
    @Sql(value = {"/reminder-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void reminderListTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(xpath("//*[@id='row-reminder']").nodeCount(3));
    }

    @Test
    @Sql(value = {"/delete-from-reminder.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addMessageToListTest() throws Exception {
        this.mockMvc.perform(post("/add")
                .param("text", "Wash the floor")
                .param("date", "2021-01-11 00:00:00"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
        Assert.assertEquals("10", String.valueOf(reminderService.getByText("Wash the floor").get(0).getId()));
    }



    @Test
    @Sql(value = {"/reminder-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void filterMessageTest() throws Exception {
        this.mockMvc.perform(get("/").param("filter", "Wash up"))
                .andDo(print())
                .andExpect(xpath("//*[@id='row-reminder']").nodeCount(2))
                .andExpect(xpath("//*[@id='row-reminder']").string("Wash up"));
    }


}
