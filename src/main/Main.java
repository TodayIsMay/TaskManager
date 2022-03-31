package main;

import managers.FileBackedTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import utilities.Creator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile("tasks.csv");
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private static Creator creator;

    public static void main(String[] args) {
        creator = new Creator(fileBackedTaskManager.getMaxIdFromFile());
        loop:
        while (true) {
            printMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 99:
                    test();
                    break;
                case 1:
                    addNewTask();
                    break;
                case 2:
                    showAllSubtasks();
                    break;
                case 3:
                    showAllEpics();
                    break;
                case 4:
                    showAllTasks();
                    break;
                case 5:
                    showSubtasksFromEpic();
                    break;
                case 6:
                    showTaskByID();
                    break;
                case 7:
                    updateTask();
                    break;
                case 8:
                    deleteTask();
                    break;
                case 9:
                    showHistory();
                    break;
                case 10:
                    getPrioritizedTasks();
                    break;
                case 0:
                    exit();
                    break loop;
                default:
                    System.out.println("Такой команды нет");
            }
        }
    }

    public static void addNewTask() {
        System.out.println("Какого типа будет эта задача?");
        System.out.println("1 - эпик");
        System.out.println("2 - подзадача");
        System.out.println("3 - простая задача");
        int type = scanner.nextInt();
        if (type == 1) {
            fileBackedTaskManager.addEpic(creator.createEpic());
        } else if (type == 2) {
            fileBackedTaskManager.addSubtask(creator.createSubtask());
        } else if (type == 3) {
            fileBackedTaskManager.addTask(creator.createTask());
        } else {
            System.out.println("Такой команды нет.");
        }
    }

    public static void showAllSubtasks() {
        List<Subtask> subtasks = fileBackedTaskManager.showAllSubtasksList();
        System.out.println("Список всех подзадач.");
        System.out.println("_____________________");
        for (Subtask sub : subtasks) {
            System.out.println("Название подзадачи: " + sub.getName() + ". Статус: " + sub.getStatus()
                    + ". (ID " + sub.getId() + ")");
        }
        System.out.println("_____________________");
    }

    public static void showAllEpics() {
        List<Epic> epics = fileBackedTaskManager.getEpics();
        System.out.println("Список всех эпиков.");
        System.out.println("___________________");
        for (Epic epic : epics) {
            System.out.println("Название эпика: " + epic.getName() + ". Статус: "
                    + epic.getStatus()
                    + ". (ID " + epic.getId() + ")");
        }
        System.out.println("___________________");
    }

    public static void showAllTasks() {
        List<Task> tasks = fileBackedTaskManager.getTasks();
        System.out.println("Список всех простых задач.");
        System.out.println("___________________________");
        for (Task task : tasks) {
            System.out.println("Название простой задачи: " + task.getName() + ". Статус: "
                    + task.getStatus()
                    + ". (ID " + task.getId() + ")");
        }
        System.out.println("___________________________");
    }

    public static void showSubtasksFromEpic() {
        System.out.println("Введите ID эпика:");
        int epicID = scanner.nextInt();
        System.out.println("Получить все подзадачи эпика " + epicID + ".");
        System.out.println("__________________________________________");
        for (Subtask subtask : fileBackedTaskManager.showSubtasksFromEpic(epicID)) {
            System.out.println(subtask.getName());
        }
        System.out.println("__________________________________");
    }

    public static void showTaskByID() {
        System.out.println("Какую задачу надо найти?");
        System.out.println("1 - эпик");
        System.out.println("2 - подзадачу");
        System.out.println("3 - простую задачу");
        int taskType = scanner.nextInt();
        int result;
        switch (taskType) {
            case 1:
                System.out.println("Введите ID эпика:");
                result = scanner.nextInt();
                try {
                    System.out.println(fileBackedTaskManager.getEpicById(result).getName());
                } catch (NullPointerException e) {
                    System.out.println("Задача с таким id не найдена!\n");
                }
                break;
            case 2:
                System.out.println("Введите ID подзадачи");
                result = scanner.nextInt();
                try {
                    System.out.println(fileBackedTaskManager.getSubtaskById(result).getName());
                } catch (NullPointerException e) {
                    System.out.println("Задача с таким id не найдена!\n");
                }
                break;
            case 3:
                System.out.println("Введите ID простой задачи.");
                result = scanner.nextInt();
                try {
                    System.out.println(fileBackedTaskManager.getTaskById(result).getName());
                } catch (NullPointerException e) {
                    System.out.println("Задача с таким id не найдена!\n");
                }
                break;
            default:
                System.out.println("Такой команды нет");
        }
    }

    public static void updateTask() {
        System.out.println("Какую задачу надо обновить?");
        System.out.println("1 - эпик");
        System.out.println("2 - подзадачу");
        System.out.println("3 - простую задачу");
        int command = scanner.nextInt();
        switch (command) {
            case 1:
                System.out.println("Введите ID эпика.");
                int id = scanner.nextInt();
                Epic epic = creator.createEpic();
                System.out.println(fileBackedTaskManager.updateEpic(epic, id));
                break;
            case 2:
                System.out.println("Введите ID подзадачи.");
                id = scanner.nextInt();
                Subtask subtask = creator.createSubtask();
                subtask.setStatus(chooseStatus());
                System.out.println(fileBackedTaskManager.updateSubtask(subtask, id));
                break;
            case 3:
                System.out.println("Введите ID простой задачи.");
                id = scanner.nextInt();
                Task task = creator.createTask();
                task.setStatus(chooseStatus());
                System.out.println(fileBackedTaskManager.updateTask(task, id));
                break;
            default:
                System.out.println("Такой команды нет");
        }
    }

    public static void deleteTask() {
        System.out.println("Удаление ранее добавленных задач.");
        System.out.println("Выберите действие: ");
        System.out.println("1 - удалить все задачи");
        System.out.println("2 - удалить подзадачу по ID");
        System.out.println("3 - удалить эпик по ID");
        System.out.println("4 - удалить простую задачу по ID");
        int newCommand = scanner.nextInt();
        switch (newCommand) {
            case 1:
                fileBackedTaskManager.deleteAllTasks();
                break;
            case 2:
                System.out.println("Введите ID подзадачи:");
                int id = scanner.nextInt();
                System.out.println(fileBackedTaskManager.deleteSubtask(id));
                break;
            case 3:
                System.out.println("Введите ID эпика");
                id = scanner.nextInt();
                fileBackedTaskManager.deleteEpic(id);
                break;
            case 4:
                System.out.println("Введите ID простой задачи.");
                id = scanner.nextInt();
                fileBackedTaskManager.deleteTask(id);
                break;
            default:
                System.out.println("Такой команды нет.");
        }
    }

    public static void showHistory() {
        System.out.println("История");
        System.out.println("___________________");
        for (Task task : fileBackedTaskManager.showHistory()) {
            System.out.println(task.getName());
        }
        System.out.println("___________________");
    }

    public static Status chooseStatus() {
        Status result = null;
        System.out.println("Выберите новый статус: " + "\n" + "1 - NEW"
                + "\n" + "2 - IN_PROGRESS" + "\n" + "3 - DONE");
        int variant = scanner.nextInt();
        switch (variant) {
            case 1:
                result = Status.NEW;
                break;
            case 2:
                result = Status.IN_PROGRESS;
                break;
            case 3:
                result = Status.DONE;
                break;
            default:
                System.out.println("Такой команды нет");
        }
        return result;
    }

    public static void getPrioritizedTasks() {
        for(Map.Entry<Task, String> entry: fileBackedTaskManager.getPrioritizedTasks().entrySet()) {
            System.out.println(entry.getKey().getStartTime().format(formatter) + " " + entry.getValue());
        }
    }

    public static void exit() {
        fileBackedTaskManager.save();
    }

    public static void test() {
        fileBackedTaskManager.addEpic(new Epic("Покормить кошку", "описание", 1, Status.NEW));
        fileBackedTaskManager.addSubtask(new Subtask(1, "найти кошку", 2, Status.NEW,
                Duration.ofHours(1), LocalDateTime.parse("22.03.2022 00:00", formatter)));
        fileBackedTaskManager.addSubtask(new Subtask(1, "открыть корм", 3, Status.NEW,
                Duration.ofHours(1), LocalDateTime.parse("22.03.2022 01:00", formatter)));
        fileBackedTaskManager.addSubtask(new Subtask(1, "насыпать корм в миску", 4, Status.NEW,
                Duration.ofHours(1), LocalDateTime.parse("22.03.2022 02:00", formatter)));
        fileBackedTaskManager.addEpic(new Epic("Второй эпик", "другое описание", 5, Status.NEW));
        fileBackedTaskManager.addTask(new Task("простая задача", 6, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("22.03.2022 03:00", formatter)));
        showAllSubtasks();
        showAllEpics();
        showAllTasks();
        for (Subtask subtask : fileBackedTaskManager.showSubtasksFromEpic(1)) {
            System.out.println(subtask);
        }
        fileBackedTaskManager.getEpicById(1);
        showHistory();
        fileBackedTaskManager.getSubtaskById(2);
        fileBackedTaskManager.getSubtaskById(3);
        fileBackedTaskManager.getSubtaskById(4);
        showHistory();
        fileBackedTaskManager.deleteEpic(1);
        showHistory();
        fileBackedTaskManager.addEpic(new Epic("Покормить кошку", "описание", 1, Status.NEW));
        fileBackedTaskManager.addSubtask(new Subtask(1, "найти кошку", 2, Status.NEW,
                Duration.ofHours(1), LocalDateTime.parse("22.03.2022 00:00", formatter)));
        fileBackedTaskManager.addSubtask(new Subtask(1, "открыть корм", 3, Status.NEW,
                Duration.ofHours(1), LocalDateTime.parse("22.03.2022 01:00", formatter)));
        fileBackedTaskManager.addSubtask(new Subtask(1, "насыпать корм в миску", 4, Status.NEW,
                Duration.ofHours(1), LocalDateTime.parse("22.03.2022 02:00", formatter)));
        fileBackedTaskManager.updateSubtask(
                new Subtask(1, "найти кошку", 2, Status.IN_PROGRESS, Duration.ofHours(1),
                        LocalDateTime.parse("22.03.2022 00:00", formatter)), 2);
        System.out.println(fileBackedTaskManager.getEpicById(1));
        fileBackedTaskManager.updateSubtask(
                new Subtask(1, "найти кошку", 2, Status.DONE, Duration.ofHours(1),
                        LocalDateTime.parse("22.03.2022 00:00", formatter)), 2);
        fileBackedTaskManager.updateSubtask(
                new Subtask(1, "открыть корм", 3, Status.DONE, Duration.ofHours(1),
                        LocalDateTime.parse("22.03.2022 01:00", formatter)), 3);
        fileBackedTaskManager.updateSubtask(
                new Subtask(1, "насыпать корм в миску", 4, Status.DONE, Duration.ofHours(1),
                        LocalDateTime.parse("22.03.2022 02:00", formatter)), 4);
        System.out.println(fileBackedTaskManager.getEpicById(1));
        fileBackedTaskManager.getSubtaskById(2);
        fileBackedTaskManager.getSubtaskById(3);
        fileBackedTaskManager.getSubtaskById(4);
        showHistory();
        System.out.println(isEqual());
    }

    public static boolean isEqual() {
        return fileBackedTaskManager.equals(FileBackedTaskManager.loadFromFile("tasks.csv"));
    }

    public static void printMenu() {
        System.out.println("Что вы хотите сделать?");
        System.out.println("99 - test");
        System.out.println("1 - добавить новую задачу");
        System.out.println("2 - посмотреть список подзадач.");
        System.out.println("3 - посмотреть список эпиков.");
        System.out.println("4 - посмотреть список простых задач.");
        System.out.println("5 - получить подзадачи конкретного эпика.");
        System.out.println("6 - получить задачу по ID.");
        System.out.println("7 - обновить задачу по ID");
        System.out.println("8 - удалить задачи");
        System.out.println("9 - история просмотров");
        System.out.println("10 - список задач по приоритету");
        System.out.println("0 - выход из программы.");
    }
}