package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

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

    String updateEpic(Epic epic, int id);

    String updateSubtask(Subtask subtask, int id);

    String updateTask(Task task, int id);

    void deleteAllTasks();

    String deleteSubtask(int id);

    String deleteEpic(int id);

    String deleteTask(int id);

    List<Task> showHistory();

    boolean doesIntersect(Task targetTask);
}
