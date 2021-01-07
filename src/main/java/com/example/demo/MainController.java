package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private ReminderRepository reminderRepository;

    @GetMapping("/")
    public String mainList(Model model) {
        Iterable<Reminder> reminders = reminderRepository.findAll();
        model.addAttribute("reminders", reminders);
        return "listReminders";
    }

    @GetMapping("/add")
    public String mainAdd(Model model) {
        model.addAttribute("reminder", new Reminder());
        return "addReminder";
    }

    @PostMapping("/add")
    public String add(Reminder reminder) {
        reminderRepository.save(reminder);
        return "redirect:/";
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
