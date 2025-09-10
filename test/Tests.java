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
        taskManager.createSubtask(subtask);

        assertEquals(Status.NEW, taskManager.getEpic(epicId).getStatus());

        // Получаем ID добавленной подзадачи
        int subId = taskManager.getAllSubtasks().get(0).getId();
        subtask = taskManager.getSubtask(subId);
        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);

        assertEquals(Status.DONE, taskManager.getEpic(epicId).getStatus());
    }

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
        task2.setId(genId);

        int newId = taskManager.createTask(task2);
        assertNotEquals(genId, newId, "ID должны быть уникальными");
    }

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
    void historyShouldNotHaveLimit() {
        for (int i = 1; i <= 15; i++) {
            Task task = new Task("Task " + i, "Desc");
            task.setId(i);
            historyManager.add(task);
        }

        assertEquals(15, historyManager.getHistory().size());
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

    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", 1);
        subtask1.setId(1);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", 1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым ID должны быть равны");

        assertEquals(subtask2, subtask1, "Метод equals должен быть симметричным");

    }

    @Test
    void epicCannotBeItsOwnSubtask() {
        Epic epic = new Epic("Важный эпик", "Описание эпика");
        epic.setId(1);

        assertTrue(epic.getSubtaskIds().isEmpty(), "У нового эпика не должно быть подзадач");
    }

    @Test
    void duplicateTasksShouldBeRemoved() {
        Task task = new Task("Test", "Desc");
        task.setId(1);

        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void removeTaskFromHistory() {
        Task task1 = new Task("Task 1", "Desc");
        Task task2 = new Task("Task 2", "Desc");
        task1.setId(1);
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(1);

        assertEquals(1, historyManager.getHistory().size());
        assertEquals(2, historyManager.getHistory().get(0).getId());
    }

    @Test
    void getTaskAddsToHistory() {
        Task task = new Task("Test", "Desc");
        int taskId = taskManager.createTask(task);

        taskManager.getTask(taskId);

        assertEquals(1, taskManager.getHistory().size());
        assertEquals(taskId, taskManager.getHistory().get(0).getId());
    }

    @Test
    void deleteTaskRemovesFromHistory() {
        Task task = new Task("Test", "Desc");
        int taskId = taskManager.createTask(task);

        taskManager.getTask(taskId);
        taskManager.deleteTask(taskId);

        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void multipleGetsOfSameTask() {
        Task task = new Task("Test", "Desc");
        int taskId = taskManager.createTask(task);

        for (int i = 0; i < 5; i++) {
            taskManager.getTask(taskId);
        }

        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void emptyHistoryWhenNoTasksViewed() {
        Task task = new Task("Test", "Desc");
        taskManager.createTask(task); // Создаем, но не смотрим

        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void getNonExistentTask() {
        assertNull(taskManager.getTask(999)); // Несуществующий ID
        assertEquals(0, taskManager.getHistory().size());
    }
}