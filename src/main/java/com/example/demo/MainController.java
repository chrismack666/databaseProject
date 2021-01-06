package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private ReminderRepository reminderRepository;

    @GetMapping("/")
    public String mainList(Map<String, Object> model) {
        Iterable<Reminder> reminders = reminderRepository.findAll();
        model.put("reminders", reminders);
        return "listReminders";
    }

    @GetMapping("/add")
    public String mainAdd(Map<String, Object> model) {
        Iterable<Reminder> reminders = reminderRepository.findAll();
        model.put("reminders", reminders);
        return "addReminder";
    }

    @PostMapping("/add")
    public String add(@Valid Reminder reminder, BindingResult bindingResult, @RequestParam String text, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")Date date, Map<String, Object> model) {
        if (bindingResult.hasErrors()) {
            return "addReminder";
        } else {
            reminder = new Reminder(text, date);

            reminderRepository.save(reminder);

            Iterable<Reminder> reminders = reminderRepository.findAll();

            model.put("reminders", reminders);
            return "listReminders";
        }
    }

    @PostMapping("/")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<Reminder> reminders;

        if (filter != null && !filter.isEmpty()) {
            reminders = reminderRepository.findByText(filter);
        } else {
            reminders = reminderRepository.findAll();
        }
        model.put("reminders", reminders);
        return "listReminders";
    }
}
