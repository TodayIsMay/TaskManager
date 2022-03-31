package utilities;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_LENGTH = 10;
    private final MyLinkedList<Task> listOfTasks = new MyLinkedList<>();
    private final HashMap<Integer, Node<Task>> map = new LinkedHashMap<>();

    public void add(Task task) {
        Node<Task> node = new Node<>(task);
        Node<Task> newNode = listOfTasks.linkLast(node);
        if (listOfTasks.size() > HISTORY_LENGTH) {
            remove(listOfTasks.getFirst().getData().getId());
        }
        if (!map.containsKey(task.getId())) {
            map.put(task.getId(), newNode);
        } else {
            remove(task.getId());
            listOfTasks.linkLast(newNode);
            map.put(task.getId(), newNode);
        }
    }

    public void remove(int id) {
        if (map.containsKey(id)) {
            listOfTasks.removeNode(map.get(id));
            map.remove(id);
        }
    }

    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        for (Node<Task> node : map.values()) {
            tasks.add(node.getData());
        }
        return tasks;
    }

    public void clearHistory() {
        listOfTasks.clear();
        map.clear();
    }

    static class MyLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        public Node<T> getFirst() {
            return head;
        }

        public Node<T> linkLast(Node<T> node) {
            final Node<T> oldTail = tail;
            node.setPrev(tail);
            node.setNext(null);
            tail = node;
            if (oldTail == null) {
                head = node;
                size++;
            } else {
                oldTail.setNext(node);
                size++;
            }
            return node;
        }

        public void removeNode(Node<T> node) {
            if (node.getPrev() != null) {
                node.getPrev().setNext(node.getNext());
            }
            if (node.getNext() != null) {
                node.getNext().setPrev(node.getPrev());
            }
            size--;
        }

        public void clear() {
            head = null;
            tail = null;
        }

        public int size() {
            return size;
        }
    }
}