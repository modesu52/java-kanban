package ru.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.manager.HistoryManager;
import ru.manager.Managers;
import ru.models.Task;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
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

        // Проверяем обратный порядок
        historyManager.add(task1);
        history = historyManager.getHistory();
        assertEquals(2, history.get(0).getId()); // task2
        assertEquals(1, history.get(1).getId()); // task1 (переместилась в конец)
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
    void removeFromEmptyHistory() {
        historyManager.remove(1); // Не должно падать
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void removeNonExistentTask() {
        Task task = new Task("Task", "Desc");
        task.setId(1);
        historyManager.add(task);

        historyManager.remove(999); // Не должно влиять на историю
        assertEquals(1, historyManager.getHistory().size());
    }
}