package ru.tests;

import org.junit.jupiter.api.Test;
import ru.models.Status;
import ru.models.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

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
        Task task = new Task("Original", "Desc");
        task.setStatus(Status.IN_PROGRESS);

        // Проверяем, что сеттеры работают корректно
        task.setName("New Name");
        task.setDescription("New Desc");

        assertEquals("New Name", task.getName());
        assertEquals("New Desc", task.getDescription());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void taskToString() {
        Task task = new Task("Test", "Desc");
        task.setId(1);
        task.setStatus(Status.DONE);

        String result = task.toString();
        assertTrue(result.contains("Test"));
        assertTrue(result.contains("1"));
        assertTrue(result.contains("DONE"));
    }
}