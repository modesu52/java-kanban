package ru.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicEqualityById() {
        Epic epic1 = new Epic("Epic 1", "Description");
        epic1.setId(1);
        Epic epic2 = new Epic("Epic 2", "Different");
        epic2.setId(1);

        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
    }

    @Test
    void epicCannotBeItsOwnSubtask() {
        Epic epic = new Epic("Важный эпик", "Описание эпика");
        epic.setId(1);

        assertTrue(epic.getSubtaskIds().isEmpty(), "У нового эпика не должно быть подзадач");
    }

    @Test
    void epicSubtaskManagement() {
        Epic epic = new Epic("Epic", "Desc");
        epic.setId(1);

        epic.addSubtaskId(2);
        epic.addSubtaskId(3);

        assertEquals(2, epic.getSubtaskIds().size());
        assertTrue(epic.getSubtaskIds().contains(2));
        assertTrue(epic.getSubtaskIds().contains(3));

        epic.removeSubtaskId(2);
        assertEquals(1, epic.getSubtaskIds().size());
        assertFalse(epic.getSubtaskIds().contains(2));
    }
}