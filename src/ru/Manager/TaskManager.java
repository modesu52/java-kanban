package ru.Manager;

import ru.models.Epic;
import ru.models.Status;
import ru.models.Subtask;
import ru.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private int nextId = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public int createTask(Task task) {
        int newId = nextId++;
        task.setId(newId);
        tasks.put(newId, task);
        return task.getId();
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void updateTask(Task task) {
        int selectedId = task.getId();
        if (tasks.containsKey(selectedId)) {
            tasks.replace(selectedId, task);
            updateEpicStatus(task.getId());
        }
    }

    public int createEpic(Epic epic) {
        int newId = nextId++;
        epic.setId(newId);
        epics.put(newId, epic);
        return epic.getId();
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void updateEpic(Epic epic) {
        int selectedId = epic.getId();
        if (epics.containsKey(selectedId)) {
            epics.replace(selectedId, epic);
        }
    }

    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : new ArrayList<>(epic.getSubtaskIds())) {
                subtasks.remove(subtaskId);
            }
        }
    }

    public void createSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            System.out.println("не получилось создать подзадачу");
        }
        int newId = nextId++;
        subtask.setId(newId);
        subtasks.put(newId, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(newId);
        updateEpicStatus(epic.getId());
    }


    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }



    public void updateSubtask(Subtask subtask) {
        int selectedId = subtask.getId();
        if (subtasks.containsKey(selectedId)) {
            subtasks.replace(selectedId, subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }


    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic.getId());
            }
        }
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> result = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    result.add(subtask);
                }
            }
        }
        return result;
    }

    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }

        ArrayList<Subtask> epicSubtasks = getEpicSubtasks(epicId);
        if (epicSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean hasNew = false;
        boolean hasInProgress = false;
        boolean hasDone = false;

        for (Subtask subtask : epicSubtasks) {
            switch (subtask.getStatus()) {
                case NEW:
                    hasNew = true;
                    break;
                case IN_PROGRESS:
                    hasInProgress = true;
                    break;
                case DONE:
                    hasDone = true;
                    break;
            }
        }

        if (hasInProgress || (hasNew && hasDone)) {
            epic.setStatus(Status.IN_PROGRESS);
        } else if (hasDone && !hasNew && !hasInProgress) {
            epic.setStatus(Status.DONE);
        } else if (hasNew && !hasDone && !hasInProgress) {
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskManager that = (TaskManager) o;
        return nextId == that.nextId && Objects.equals(tasks, that.tasks) && Objects.equals(epics, that.epics) && Objects.equals(subtasks, that.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nextId, tasks, epics, subtasks);
    }
}
