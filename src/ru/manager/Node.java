package ru.manager;

import ru.models.Task;

public class Node {
    private final Task task;
    private Node prev;
    private Node next;

    public Node(Task task) {
        this.task = task;
        this.prev = null;
        this.next = null;
    }


    public Task getTask() {
        return task;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public int getTaskId() {
        return task.getId();
    }
}