package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ReminderServiseImpl implements ReminderServise {
    @Autowired
    private ReminderRepository reminderRepository;

    @Override
    public boolean checkAndAdd(Reminder reminder) {
        boolean isVerified = true;
        List<Reminder> reminders = reminderRepository.findAll();
        for (int i=0; i < reminders.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String listReminderDate = simpleDateFormat.format(reminders.get(i).getDate());
            String newReminderDate = simpleDateFormat.format(reminder.getDate());

            if (reminders.get(i).getText().equals(reminder.getText()) &&
                    listReminderDate.equals(newReminderDate)) {
                isVerified = false;
            }
        }
        if (isVerified) {
            reminderRepository.save(reminder);
        }

        return isVerified;
    }

    @Override
    public List<Reminder> getAll() {
        List<Reminder> foundReminders = reminderRepository.findAll();
        return foundReminders;
    }

    @Override
    public List<Reminder> getByText(String text) {
        List<Reminder> foundReminders = reminderRepository.findByText(text);
        return foundReminders;
    }

    @Override
    public List<Reminder> getFilter(String filter) {
        List<Reminder> freminders;
        if (filter != null && !filter.isEmpty()) {
            freminders = reminderRepository.findByText(filter);
        } else {
            freminders = reminderRepository.findAll();
        }
        return freminders;
    }
}
