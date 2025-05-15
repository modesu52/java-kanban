package ru.Manager;

import ru.models.Epic;
import ru.models.Subtask;
import ru.models.Task;

import java.util.ArrayList;

public interface TaskManager {
    int createTask(Task task);

    ArrayList<Task> getAllTasks();

    Task getTask(int id);
    void deleteTask(int id);
    void updateTask(Task task);

    int createEpic(Epic epic);
    ArrayList<Epic> getAllEpics();
    Epic getEpic(int id);

    void updateEpic(Epic epic);
    void deleteEpic(int id);
    void createSubtask(Subtask subtask);
    ArrayList<Subtask> getAllSubtasks();

    Subtask getSubtask(int id);
    void updateSubtask(Subtask subtask);
    void deleteSubtask(int id);
    void clearAllTasks();

    ArrayList<Subtask> getEpicSubtasks(int epicId);
    void updateEpicStatus(int epicId);

    ArrayList<Task> getHistory();
}
