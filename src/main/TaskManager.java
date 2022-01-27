package main;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpics();

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void addTask(Task task);

    List <Subtask> showAllSubtasksList();

    List<Subtask> showSubtasksFromEpic(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    Task getTaskById(int id);

    void updateEpic(Epic epic, int id);

    void updateSubtask(Subtask subtask, int id);

    void updateTask(Task task, int id);

    void deleteAllTasks();

    void deleteSubtask(int id);

    void deleteEpic(int id);

    void deleteTask(int id);

    List<Task> showHistory();
}

