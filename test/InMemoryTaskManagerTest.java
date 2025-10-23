package ru.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.models.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldCreateAndRetrieveAllTaskTypes() {
        Task task = new Task("Task", "Desc");
        int taskId = taskManager.createTask(task);

        Epic epic = new Epic("Epic", "Desc");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub", "Desc", epicId);
        taskManager.createSubtask(subtask);

        assertNotNull(taskManager.getTask(taskId));
        assertNotNull(taskManager.getEpic(epicId));
        assertFalse(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    void epicStatusShouldUpdateAutomatically() {
        Epic epic = new Epic("Epic", "Desc");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub", "Desc", epicId);
        taskManager.createSubtask(subtask);

        assertEquals(Status.NEW, taskManager.getEpic(epicId).getStatus());

        int subId = taskManager.getAllSubtasks().get(0).getId();
        Subtask savedSubtask = taskManager.getSubtask(subId);
        savedSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(savedSubtask);

        assertEquals(Status.DONE, taskManager.getEpic(epicId).getStatus());
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
    void shouldReturnEmptyListsWhenNoTasks() {
        assertTrue(taskManager.getAllTasks().isEmpty());
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
        assertTrue(taskManager.getHistory().isEmpty());
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
    void getNonExistentTask() {
        assertNull(taskManager.getTask(999));
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void clearAllTasksRemovesFromHistory() {
        Task task1 = new Task("Task 1", "Desc");
        Task task2 = new Task("Task 2", "Desc");
        int id1 = taskManager.createTask(task1);
        int id2 = taskManager.createTask(task2);

        taskManager.getTask(id1);
        taskManager.getTask(id2);

        taskManager.clearAllTasks();

        assertEquals(0, taskManager.getAllTasks().size());
        assertEquals(0, taskManager.getHistory().size());
    }
}