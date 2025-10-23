package ru.tests;

import org.junit.jupiter.api.Test;
import ru.manager.*;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultReturnsInitializedTaskManager() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager);
        assertTrue(manager instanceof InMemoryTaskManager);
    }

    @Test
    void getDefaultHistoryReturnsInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
        assertTrue(historyManager instanceof InMemoryHistoryManager);
    }

    @Test
    void managersReturnDifferentInstances() {
        TaskManager manager1 = Managers.getDefault();
        TaskManager manager2 = Managers.getDefault();

        assertNotSame(manager1, manager2);
    }
}