package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ReminderServiseImpl reminderServiseImpl;

    @GetMapping("/")
    public String mainList(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        List<Reminder> reminders = reminderServiseImpl.getAll();
        List<Reminder> freminders = reminderServiseImpl.getFilter(filter);

        model.addAttribute("filter", filter);
        model.addAttribute("freminders", freminders);
        model.addAttribute("reminders", reminders);
        return "listReminders";
    }

    @GetMapping("/add")
    public String mainAdd(Model model) {
        if(!model.containsAttribute("reminder")) {
            model.addAttribute("reminder", new Reminder());
        }
        return "addReminder";
    }

    @PostMapping("/add")
    public String add(@Valid Reminder reminder, BindingResult result, RedirectAttributes redirectAttributes) {
        boolean isVerified = reminderServiseImpl.checkAndAdd(reminder);
        if (isVerified) {
            return "redirect:/";
        } else {
            result.rejectValue("text", "same.reminder", "Error! This reminder already exists");
            redirectAttributes.getFlashAttributes().clear();
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.reminder", result);
            redirectAttributes.addFlashAttribute("reminder", reminder);
            return "redirect:/add";
        }
    }
}
