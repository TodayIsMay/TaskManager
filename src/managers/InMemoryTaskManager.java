package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utilities.HistoryManager;
import utilities.Managers;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    private List<Task> tasks;
    private List<Epic> epics;
    Map<Task, String> prioritizedTasks;

    public InMemoryTaskManager() {
        tasks = new ArrayList<>();
        epics = new ArrayList<>();
        prioritizedTasks = new TreeMap<>((o1, o2) -> {
            if (o1.getStartTime().isAfter(o2.getStartTime())) {
                return 1;
            } else if (o1.getStartTime().isBefore(o2.getStartTime())) {
                return -1;
            }
            return 0;
        });
    }

    static public InMemoryTaskManager setLists(List<Task> tasks, List<Epic> epics) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.setTasks(tasks);
        manager.setEpics(epics);
        return manager;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setEpics(List<Epic> epics) {
        this.epics = epics;
    }

    @Override
    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public List<Epic> getEpics() {
        return epics;
    }

    @Override
    public void addEpic(Epic epic) {
        epics.add(epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (!doesIntersect(subtask)) {
            for (Epic epic : epics) {
                if (epic.getId() == subtask.getBelonging()) {
                    epic.addSub(subtask);
                    epic.getStartTime();
                    epic.getEndTime();
                    epic.getDuration();
                    break;
                }
            }
            prioritizedTasks.put(subtask, subtask.getName());
        } else {
            System.out.println("Пересечение по времени!");
        }
    }

    @Override
    public void addTask(Task task) {
        if(!doesIntersect(task)) {
            tasks.add(task);
            prioritizedTasks.put(task, task.getName());
        }else {
            System.out.println("Пересечение по времени!");
        }
    }

    @Override
    public List<Subtask> showAllSubtasksList() {
        List<Subtask> subtasks = new ArrayList<>();
        for (Epic epic : epics) {
            subtasks.addAll(epic.getSubtasks());
        }
        return subtasks;
    }

    @Override
    public List<Subtask> showSubtasksFromEpic(int id) {
        List<Subtask> subtasks = new ArrayList<>();
        for (Epic epic : epics) {
            if (epic.getId() == id) {
                subtasks = epic.getSubtasks();
                break;
            }
        }
        return subtasks;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic target = null;
        for (Epic epic : epics) {
            if (epic.getId() == id) {
                target = epic;
                break;
            }
        }
        if (target != null) {
            inMemoryHistoryManager.add(target);
        }
        return target;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask target = null;
        for (Epic epic : epics) {
            List<Subtask> list = epic.getSubtasks();
            for (Subtask subtask : list) {
                if (subtask.getId() == id) {
                    target = subtask;
                    break;
                }
            }
        }
        if (target != null) {
            inMemoryHistoryManager.add(target);
        }
        return target;
    }

    @Override
    public Task getTaskById(int id) {
        Task target = null;
        for (Task task : tasks) {
            if (task.getId() == id) {
                target = task;
                break;
            }
        }
        if (target != null) {
            inMemoryHistoryManager.add(target);
        }
        return target;
    }

    @Override
    public String updateEpic(Epic epic, int id) {
        Epic target = null;
        for (Epic oldEpic : epics) {
            if (oldEpic.getId() == id) {
                target = oldEpic;
                break;
            }
        }
        if (target != null) {
            target.update(epic);
            target.getStartTime();
            target.getEndTime();
            target.getDuration();
        } else {
            return "Эпик с таким ID не найден!";
        }
        return "Эпик обновлён!";
    }

    @Override
    public String updateSubtask(Subtask subtask, int id) {
        if(!doesIntersect(subtask)) {
            Subtask target = null;
            for (Epic epic : epics) {
                List<Subtask> list = epic.getSubtasks();
                for (Subtask sub : list) {
                    if (sub.getId() == id) {
                        target = sub;
                        break;
                    }
                }
            }
            if (target != null) {
                target.update(subtask);
            } else {
                return "Подзадача с таким ID не найдена!";
            }
        }else {
            return "Пересечение по времени!";
        }
        return "Подзадача обновлена!";
    }

    @Override
    public String updateTask(Task task, int id) {
        if(!doesIntersect(task)) {
            Task target = null;
            for (Task oldTask : tasks) {
                if (oldTask.getId() == id) {
                    target = oldTask;
                    break;
                }
            }
            if (target != null) {
                target.update(task);
            } else {
                return "Задача с таким ID не найдена!";
            }
        }else {
            return "Пересечение по времени!";
        }
        return "Задача обновлена!";
    }

    @Override
    public void deleteAllTasks() {
        epics.clear();
        tasks.clear();
        inMemoryHistoryManager.clearHistory();
    }

    @Override
    public String deleteSubtask(int id) {
        Subtask target = null;
        Epic targetEpic = null;
        for (Epic epic : epics) {
            for (Subtask subtask : epic.getSubtasks()) {
                if (subtask.getId() == id) {
                    target = subtask;
                    targetEpic = epic;
                    break;
                }
            }
        }
        if (target != null) {
            targetEpic.deleteSubtask(target);
        } else {
            return "Подзадача с таким ID не найдена!";
        }
        inMemoryHistoryManager.remove(id);
        return "Задача удалена!";
    }

    @Override
    public String deleteEpic(int id) {
        Epic target = null;
        for (Epic epic : epics) {
            if (epic.getId() == id) {
                target = epic;
                break;
            }
        }
        if (target != null) {
            epics.remove(target);
            inMemoryHistoryManager.remove(id);
            for (Subtask subtask : target.getSubtasks()) {
                inMemoryHistoryManager.remove(subtask.getId());
            }
        } else {
            return "Задача с таким ID не найдена!";
        }
        return "Задача удалена!";
    }

    @Override
    public String deleteTask(int id) {
        Task target = null;
        for (Task task : tasks) {
            if (task.getId() == id) {
                target = task;
                break;
            }
        }
        if (target != null) {
            tasks.remove(target);
            inMemoryHistoryManager.remove(id);
        } else {
            return "Задача с таким ID не найдена!";
        }
        return "Задача удалена!";
    }

    @Override
    public boolean doesIntersect(Task targetTask) {
        boolean result = false;
        for (Task task : prioritizedTasks.keySet()) {
            if (targetTask.getStartTime().isAfter(task.getStartTime()) &
                    targetTask.getStartTime().isBefore(task.getEndTime())) {
                return true;
            }
        }
        return result;
    }

    @Override
    public List<Task> showHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    public HistoryManager getHistoryManager() {
        return inMemoryHistoryManager;
    }

    public Map<Task, String> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}