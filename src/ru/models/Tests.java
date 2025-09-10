package ru.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.manager.HistoryManager;
import ru.manager.Managers;
import ru.manager.TaskManager;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class Tests {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    // Тесты для модели Task
    @Test
    void taskEqualityById() {
        Task task1 = new Task("Task 1", "Description");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Different");
        task2.setId(1);

        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }

    @Test
    void taskFieldsShouldNotChangeAfterAdding() {
        Task original = new Task("Original", "Desc");
        original.setStatus(Status.IN_PROGRESS);
        int id = taskManager.createTask(original);

        Task saved = taskManager.getTask(id);
        assertEquals("Original", saved.getName());
        assertEquals("Desc", saved.getDescription());
        assertEquals(Status.IN_PROGRESS, saved.getStatus());
    }

    @Test
    void epicStatusShouldUpdateAutomatically() {
        Epic epic = new Epic("Epic", "Desc");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub", "Desc", epicId);
        taskManager.createSubtask(subtask); // Убрано присваивание

        assertEquals(Status.NEW, taskManager.getEpic(epicId).getStatus());

        // Получаем ID добавленной подзадачи
        int subId = taskManager.getAllSubtasks().get(0).getId();
        subtask = taskManager.getSubtask(subId);
        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);

        assertEquals(Status.DONE, taskManager.getEpic(epicId).getStatus());
    }




    // Тесты для TaskManager
    @Test
    void shouldCreateAndRetrieveAllTaskTypes() {

        Task task = new Task("Task", "Desc");
        int taskId = taskManager.createTask(task);

        Epic epic = new Epic("Epic", "Desc");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub", "Desc", epicId);
        taskManager.createSubtask(subtask); // Убрано присваивание

        assertFalse(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    void generatedAndManualIdsShouldNotConflict() {
        Task task1 = new Task("Task 1", "Desc");
        int genId = taskManager.createTask(task1);

        Task task2 = new Task("Task 2", "Desc");
        task2.setId(genId); // Пытаемся использовать существующий ID

        int newId = taskManager.createTask(task2);
        assertNotEquals(genId, newId, "ID должны быть уникальными");
    }

    // Тесты для HistoryManager
    @Test
    void shouldAddTasksToHistory() {
        Task task = new Task("Task", "Desc");
        task.setId(1);
        historyManager.add(task);

        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void historyShouldNotExceedMaxSize() {
        for (int i = 1; i <= 15; i++) {
            Task task = new Task("Task " + i, "Desc");
            task.setId(i);
            historyManager.add(task);
        }

        assertEquals(10, historyManager.getHistory().size());
    }

    @Test
    void historyShouldMaintainOrder() {
        Task task1 = new Task("Task 1", "Desc");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Desc");
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);

        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(1, history.get(0).getId());
        assertEquals(2, history.get(1).getId());
    }

    // Интеграционные тесты
    @Test
    void taskManagerShouldTrackHistory() {
        Task task = new Task("Task", "Desc");
        int taskId = taskManager.createTask(task);

        Epic epic = new Epic("Epic", "Desc");
        int epicId = taskManager.createEpic(epic);

        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);

        ArrayList<Task> history = taskManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task, history.get(0));
        assertEquals(epic, history.get(1));
    }

    @Test
    void shouldReturnEmptyListsWhenNoTasks() {
        assertTrue(taskManager.getAllTasks().isEmpty());
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
        assertTrue(taskManager.getHistory().isEmpty());
    }
}