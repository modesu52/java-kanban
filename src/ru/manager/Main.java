package ru.manager;

import ru.models.Epic;
import ru.models.Status;
import ru.models.Subtask;
import ru.models.Task;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Первая задача", "Описание первой задачи");
        Task task2 = new Task("Вторая задача", "Описание второй задачи");
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Первый эпик", "Описание первого эпика");
        Epic epic2 = new Epic("Второй эпик", "Описание второго эпика");
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Первая подзадача", "Описание первой подзадачи", epic1.getId());
        Subtask subtask2 = new Subtask("Вторая подзадача", "Описание второй подзадачи", epic1.getId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);



        System.out.println("all tasks:");
        ArrayList<Task> allTasks = manager.getAllTasks();
        for (Task task : allTasks) {
            System.out.println(task.toString());
        }

        System.out.println("all epics:");
        ArrayList<Epic> allEpics = manager.getAllEpics();
        for (Epic epic : allEpics) {
            System.out.println(epic.toString());
        }

        System.out.println("all subtasks:");
        System.out.println(manager.getAllSubtasks());


        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.NEW);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());
        manager.getTask(task2.getId());

        System.out.println("\nИстория просмотров:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("updates:");
        System.out.println("task 1: " + task1.getStatus().toString());
        System.out.println("subtask 1: " + subtask1.getStatus().toString());
        System.out.println("subtask 2: " + subtask2.getStatus().toString());

        manager.deleteTask(task1.getId());
        manager.deleteEpic(epic2.getId());

        System.out.println("\nПосле удаления:");
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
        System.out.println("Все подзадачи: " + manager.getAllSubtasks());
    }
}