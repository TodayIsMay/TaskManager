package managers;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utilities.HistoryManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static int maxIdFromFile = 0;
    private String file;

    public FileBackedTaskManager(String file) {
        super();
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(String file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        Map<Integer, Task> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String str = br.readLine();
                if (!str.isEmpty() | !str.isBlank()) {
                    Task task = Task.fromString(str);
                    if (task instanceof Epic) {
                        manager.addEpicUnsafe((Epic) task);
                        map.put(task.getId(), task);
                        if (task.getId() > maxIdFromFile) {
                            maxIdFromFile = task.getId();
                        }
                    } else if (task instanceof Subtask) {
                        manager.addSubtaskUnsafe((Subtask) task);
                        map.put(task.getId(), task);
                        if (task.getId() > maxIdFromFile) {
                            maxIdFromFile = task.getId();
                        }
                    } else if (task instanceof Task) {
                        manager.addTaskUnsafe(task);
                        map.put(task.getId(), task);
                        if (task.getId() > maxIdFromFile) {
                            maxIdFromFile = task.getId();
                        }
                    }
                } else {
                    String history = br.readLine();
                    for (Integer id : fromString(history)) {
                        manager.getHistoryManager().add(map.get(id));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return manager;
    }

    public int getMaxIdFromFile() {
        return maxIdFromFile;
    }

    public void save() {
        try (FileWriter out = new FileWriter(file, StandardCharsets.UTF_8)) {
            for (Epic epic : getEpics()) {
                out.write(epic.toString());
                for (Subtask subtask : epic.getSubtasks()) {
                    out.write(subtask.toString());
                }
            }
            for (Task task : getTasks()) {
                out.write(task.toString());
            }
            if (toString(super.getHistoryManager()) != null) {
                out.write("\n");
                out.write(toString(super.getHistoryManager()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при автосохранении!");
        }
    }

    private void addEpicUnsafe(Epic epic) {
        super.addEpic(epic);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    private void addSubtaskUnsafe(Subtask subtask) {
        super.addSubtask(subtask);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    private void addTaskUnsafe(Task task) {
        super.addTask(task);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public String updateEpic(Epic epic, int id) {
        String result = super.updateEpic(epic, id);
        save();
        return result;
    }

    @Override
    public String updateSubtask(Subtask subtask, int id) {
        String result = super.updateSubtask(subtask, id);
        save();
        return result;
    }

    @Override
    public String updateTask(Task task, int id) {
        String result = super.updateTask(task, id);
        save();
        return result;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public String deleteSubtask(int id) {
        String result = super.deleteSubtask(id);
        save();
        return result;
    }

    @Override
    public String deleteEpic(int id) {
        String result = super.deleteEpic(id);
        save();
        return result;
    }

    @Override
    public String deleteTask(int id) {
        String result = super.deleteTask(id);
        save();
        return result;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    public static String toString(HistoryManager manager) {
        if (!manager.getHistory().isEmpty()) {
            StringBuilder res = new StringBuilder();
            for (Task task : manager.getHistory()) {
                String id = String.valueOf(task.getId());
                res.append(id).append(", ");
            }
            return res.deleteCharAt(res.length() - 2).toString();
        }
        return null;
    }

    public static List<Integer> fromString(String value) {
        List<Integer> ids = new ArrayList<>();
        String[] idArray = value.split(",");
        for (int i = 0; i < idArray.length; i++) {
            ids.add(Integer.parseInt(idArray[i].strip()));
        }
        return ids;
    }

    @Override
    public boolean equals(Object o) {
        List<Subtask> thisSubtasks = new ArrayList<>();
        List<Subtask> oSubtasks = new ArrayList<>();
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        for (Epic epic : this.getEpics()) {
            thisSubtasks.addAll(epic.getSubtasks());
        }
        for (Epic epic : ((FileBackedTaskManager) o).getEpics()) {
            oSubtasks.addAll(epic.getSubtasks());
        }
        if (!this.getTasks().equals(((FileBackedTaskManager) o).getTasks()) &&
                !this.getEpics().equals(((FileBackedTaskManager) o).getEpics()) &&
                !this.showHistory().equals(((FileBackedTaskManager) o).showHistory()) &&
                !thisSubtasks.equals(oSubtasks)) return false;
        FileBackedTaskManager that = (FileBackedTaskManager) o;
        return Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
}