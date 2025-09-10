package ru.manager;

import ru.models.Epic;
import ru.models.Subtask;
import ru.models.Task;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        System.out.println("1. Неограниченная история:");
        for (int i = 1; i <= 10; i++) {
            Task task = new Task("Задача " + i, "Описание " + i);
            manager.createTask(task);
            manager.getTask(task.getId());
        }
        System.out.println("   Создано " + manager.getAllTasks().size() + " задач, все в истории");
        System.out.println("   Размер истории: " + manager.getHistory().size());

        System.out.println("\n2. Удаление дубликатов:");
        System.out.println("   Просматриваем задачу 5 несколько раз...");
        for (int i = 0; i < 5; i++) {
            manager.getTask(4);
        }
        System.out.println("   Размер истории после 5 дублирующих просмотров: " + manager.getHistory().size());
        System.out.println("   В истории осталась только одна копия задачи 5!");
        System.out.println(manager.getHistory());

        System.out.println("\n3. Порядок просмотров:");
        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);

        ArrayList<Task> history = manager.getHistory();
        System.out.println("   Последние 3 просмотра:");
        System.out.println(history);

        System.out.println("\n4. Автоматическое удаление из истории:");
        System.out.println("   Размер истории до удаления: " + manager.getHistory().size());
        manager.deleteTask(5);
        System.out.println("   Размер истории после удаления задачи 5: " + manager.getHistory().size());

        boolean task5InHistory = false;
        for (Task task : manager.getHistory()) {
            if (task.getId() == 5) {
                task5InHistory = true;
                break;
            }
        }
        System.out.println("   Задача 5 в истории: " + task5InHistory);

        System.out.println("\n5. Работа с эпиками и подзадачами:");
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        manager.getTask(1);
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());

        System.out.println("   История содержит разные типы задач:");
        for (Task task : manager.getHistory()) {
            String type = task.getClass().getSimpleName();
            System.out.println("   - " + type + " #" + task.getId() + ": " + task.getName());
        }

        System.out.println("\n6. Удаление эпика и его подзадач из истории:");
        System.out.println("   Размер истории до удаления эпика: " + manager.getHistory().size());
        manager.deleteEpic(epic1.getId());
        System.out.println("   Размер истории после удаления эпика: " + manager.getHistory().size());

        System.out.println("\n7. Финальная проверка:");
        System.out.println("   Все задачи в истории:");
        for (Task task : manager.getHistory()) {
            System.out.println("   - " + task.getName() + " (ID: " + task.getId() + ")");
        }
        System.out.println("   Итоговый размер истории: " + manager.getHistory().size());
    }
}