package com.example.demo;

import java.util.List;

public interface ReminderServise {
    boolean checkAndAdd(Reminder bank);
    List<Reminder> getAll();
    List<Reminder> getByText(String text);
    List<Reminder> getFilter(String filter);

}
