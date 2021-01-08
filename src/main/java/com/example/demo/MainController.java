package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    @Autowired
    private ReminderRepository reminderRepository;

    @GetMapping("/")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Reminder> reminders;

        if (filter != null && !filter.isEmpty()) {
            reminders = reminderRepository.findByText(filter);
        } else {
            reminders = reminderRepository.findAll();
        }

        model.addAttribute("filter", filter);
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
}
