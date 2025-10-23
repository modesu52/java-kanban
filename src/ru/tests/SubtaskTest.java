package ru.tests;

import org.junit.jupiter.api.Test;
import ru.models.Status;
import ru.models.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void subtaskEqualityById() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", 1);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", 1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым ID должны быть равны");
        assertEquals(subtask2, subtask1, "Метод equals должен быть симметричным");
    }

    @Test
    void subtaskHasEpicId() {
        Subtask subtask = new Subtask("Subtask", "Desc", 5);
        subtask.setId(1);

        assertEquals(5, subtask.getEpicId());
    }

    @Test
    void subtaskToString() {
        Subtask subtask = new Subtask("Test Subtask", "Desc", 1);
        subtask.setId(2);
        subtask.setStatus(Status.IN_PROGRESS);

        String result = subtask.toString();
        assertTrue(result.contains("Test Subtask"));
        assertTrue(result.contains("2"));
        assertTrue(result.contains("IN_PROGRESS"));
        assertTrue(result.contains("epicId=1"));
    }
}