package ru.Manager;

import ru.models.Task;
import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);
    ArrayList<Task> getHistory();
}
